#!/usr/bin/python
# -*- coding: utf8 -*-
# origen: https://www.pobot.org/Outil-de-generation-d-images-pour.html
# autor: Eric P.
import sys
import os
import argparse
import logging
import datetime
import math
from struct import pack

from PIL import Image

JAVA_CODE_PACKAGE = 'package %s;\n'
JAVA_CODE_PROLOG = '''import javax.microedition.lcdui.Image;

// Generated on %(date)s from file %(infile)s

public class %(classname)s {
    static public final Image image = new Image(%(width)d, %(height)d, new byte[]{
'''
JAVA_CODE_EPILOG = '''    });
}
'''
JAVA_CODE_BYTE = '(byte)0x%02x'

logging.basicConfig(
    #stream=sys.stdout,
    level=logging.INFO,
    format='[%(levelname).1s] %(message)s'
)

class Converter(object):
    def run(self, run_args):
        try:
            method = getattr(self, 'export_as_' + run_args.output_format)
        except AttributeError:
            raise ConvertError('format not yet implemented : %s' % run_args.output_format)
        else:
            try:
                logging.info('Loading image from %s...', run_args.infile)
                img = Image.open(run_args.infile)
            except Exception as e:
                raise ConvertError(e)
            else:
                method(img, run_args)
                logging.info('Done.')

    def execute(self, img, emit_byte):
        img_w, img_h = img.size
        line_padcnt = img_w % 8

        byte = 0
        for y in range(img_h):
            for x in range(img_w):
                byte = (byte >> 1) | (0x80 if img.getpixel((x, y)) > 0 else 0)
                if x % 8 == 7:
                    emit_byte(byte)
                    byte = 0
            if line_padcnt:
                emit_byte(byte >> line_padcnt)
                byte = 0

    def export_as_java(self, img, run_args):
        img_w, img_h = img.size
        line_padcnt = img_w % 8

        outfile = run_args.class_name + '.java'
        logging.info('Generating file %s...', outfile)
        out = open(os.path.join(run_args.output_dir, outfile), 'wt')

        try:
            if run_args.package_name:
                out.write(JAVA_CODE_PACKAGE % run_args.package_name)
            out.write(JAVA_CODE_PROLOG % {
                'date':datetime.datetime.now().isoformat(' ')[:-7],
                'infile':run_args.infile,
                'classname':run_args.class_name,
                'width':img_w,
                'height':img_h
                }
            )

            src = []
            self.execute(img, lambda b : src.append(JAVA_CODE_BYTE % b))

            lines = []
            for offs in range(0, len(src), 8):
                lines.append('        ' + ','.join(src[offs:offs+8]))
            out.write(',\n'.join(lines) + '\n')
            out.write(JAVA_CODE_EPILOG)

        finally:
            out.close()

    def export_as_resource(self, img, run_args):
        img_w, img_h = img.size
        line_padcnt = img_w % 8

        outfile = run_args.res_fname
        logging.info('Generating file %s...', outfile)
        out = open(os.path.join(run_args.output_dir, outfile), 'wb')

        try:
            lg = int(math.ceil(img_w * img_h / 8.))
            out.write(pack('>HHI', img_w, img_h, lg))
            self.execute(img, lambda b : out.write(chr(b)))

        finally:
            out.close()

class ConvertError(Exception):
    pass

if __name__ == '__main__':
    parser = argparse.ArgumentParser(
        description='Converts a graphic file into an EV3 image.',
        formatter_class=argparse.ArgumentDefaultsHelpFormatter
    )
    parser.add_argument(
        '--class-name',
        help='the name of the generated class (if --output-format is "java")',
        dest='class_name',
        default='EV3Image'
    )
    parser.add_argument(
        '--resource-filename',
        help='the name of the generated resource file (if --output-format is "resource")',
        dest='res_fname',
        default='image.bin'
    )
    parser.add_argument(
        '--package',
        help='name of the parent package',
        dest='package_name'
    )
    parser.add_argument(
        '--output-format',
        help='output format selector',
        dest='output_format',
        choices=['java','resource'],
        default='java'
    )
    parser.add_argument(
        '--output-dir',
        help='output directory path',
        dest='output_dir',
        default='.'
    )

    parser.add_argument(
        help='input graphic file',
        dest='infile'
    )

    args = parser.parse_args()
    try:
        Converter().run(args)
    except ConvertError as e:
        logging.error(e)
        sys.exit(2)
