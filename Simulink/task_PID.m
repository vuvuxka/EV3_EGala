%%
clc
clear
close all

%% Parametros
M = 0.7; % masa del carro
m = 0.027; % masa del pendulo
g = 9.81; % gravedad
L = 0.145; % longitud del pendulo
k = 0.1; % friccion del carro
I = 0.09;%(M*L^2)/3; % friccion en la union
radio = 42/(2*1000);

%% Constantes
K_angulo = 25; % parametro del angulo
K_rate = 1.3; % parametro de la velocidad angular
K_pos = 350; % parametro de la posicion
K_vel = 75; % parametro de la velocidad

Kp = 0.4; 
Ki = 14;
Kd = 0.005;

deg2rad = pi / 180;
rad2deg = 1/deg2rad;
rad2m = radio;
m2rad = 1/rad2m;

%% Matrices 
%% obtiene los ángulos en rad y el desplazamiento en metros
p1 = (M+m)/(I*(M+m)+L^2*m*M);
p2 = (I+L^2*m)/(I*(M+m+L^2*m*M));
A = [             0, 1, 0,                   0;
     L*m*g*p1, 0, 0,             (L*m*k*p1)/(M+m);
                  0, 0, 0,                   1;
       (-(L*m)^2*g*p2)/(I+L^2*m), 0, 0,     -k*p2];
 
B = [0; (-L*m*p1)/(M+m); 0; p2];

T1 = diag([deg2rad; deg2rad; 1; 1]); % cambio del controlador al del modelo

T2 = diag([rad2deg; rad2deg; 1; 1]); % cambio del modelo al controlador 

K = [K_angulo; K_rate; K_pos; K_vel];

%% Systema
x0 = [5*deg2rad; 0; 0; 0]; % empieza quieto y girado 5 grados
 