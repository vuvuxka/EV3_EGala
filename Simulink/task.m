%%
clc
clear
close all

%% Parametros
mc = 1.5; % masa del carro
mp = 0.5; % masa del pendulo
g = 9.82; % gravedad
L = 1; % longitud del pendulo
d1 = 1e-2; % friccion del carro
d2 = 1e-2; % friccion en la union

%% Constantes
K1 = 25; % parametro del angulo
K2 = 1.3; % parametro de la velocidad angular
K3 = 350; % parametro de la posicion
K4 = 75; % parametro de la velocidad

%% Matrices

A = [0,                     0,          0,                             1;
     0,  (g*(mc + mp))/(L*mc), -d1/(L*mc),  -(d2*mc + d2*mp)/(L^2*mc*mp);
     0,                     0,          1,                             0;
     0,             (g*mp)/mc,     -d1/mc,                    -d2/(L*mc)];
 
B = [0; 1/(L*mc); 0; 1/mc];

K = [K1; K2; K3; K4];
 
%% Output 
C = [1;0;0;0]; % q1 es la salida

D = 0;

%% Systema
% sys = ss(A,B,C',D);
x0 = [5*pi/180; 0; 0; 0]; % empieza quieto y girado 5 grados
 
 
 
 