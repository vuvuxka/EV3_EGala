%%
clc
clear
close all

%% Parametros
M = 0.7; % masa del carro
m = 0.027; % masa del pendulo
g = 9.81; % gravedad
L = 0.145; % longitud del pendulo
b = 0.1; % friccion del carro
I = 0.09;%(M*L^2)/3; % friccion en la union

%% Constantes
K_angulo = 15; % parametro del angulo
K_rate = 0.8; % parametro de la velocidad angular
K_pos = 0.12; % parametro de la posicion
K_vel = 0.08; % parametro de la velocidad

Kp = 1.5; 
Ki = 0.5;
Kd = 0.005;


%% Matrices
p = I*(M + m) + M*m*L^2;
A = [             0, 1, 0,                   0;
     M*g*L*(M + m)/p, 0, 0,             M*L*b/p;
                  0, 0, 0,                   1;
       -g*M^2*L^2/p, 0, 0,     -b*(I + M*L^2)/p];
 
B = [0; -M*L/p; 0; (I+M*L^2)/p];

K0 = [K_angulo; K_rate; K_pos; K_vel];
LQR = [-25.6476, -1.3224, -0.1, -0.7695];
Q = [50 0 0 0; 0 1 0 0; 0 0 50 0; 0 0 0 1];
R = 1;
 
%% Output 
C = [0,0,1,0;
     1,0,0,0]; % q1 es la salida

D = [0;0];

%% Systema
% sys = ss(A,B,C',D);
x0 = [0.2; 0; 0; 0]; % empieza quieto y girado 5 grados
 
 
 
 