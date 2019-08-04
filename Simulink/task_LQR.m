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
K_angulo = 21.6; % parametro del angulo
K_rate = 1.1520; % parametro de la velocidad angular
K_pos = 0.1728; % parametro de la posicion
K_vel = 0.1152; % parametro de la velocidad

%% Matrices
p = I*(M + m) + M*m*L^2;
A = [             0, 1, 0,                   0;
     M*g*L*(M + m)/p, 0, 0,             M*L*b/p;
                  0, 0, 0,                   1;
       -g*M^2*L^2/p, 0, 0,     -b*(I + M*L^2)/p];
 
B = [0; -M*L/p; 0; (I+M*L^2)/p];

K = [K_angulo; K_rate; K_pos; K_vel];

%% Systema
x0 = [0.2; 0; 0; 0]; % empieza quieto y girado 5 grados
 
 
 
 