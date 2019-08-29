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
I = 0.006;%(M*L^2)/3; % friccion en la union
radio = 42/(2*1000);

%% Sistema
% todas las unidades de salida estan en radianes 
p1 = (M+m)/(I*(M+m)+L^2*m*M);
p2 = (I+L^2*m)/(I*(M+m+L^2*m*M));
A = [                        0, 1, 0,                0;
                      L*m*g*p1, 0, 0, (L*m*k*p1)/(M+m);
                             0, 0, 0,                1;
     (-(L*m)^2*g*p2)/(I+L^2*m), 0, 0,            -k*p2];

B = [0; (-L*m*p1)/(M+m); 0; p2];
C = eye(4);

D = zeros(4,1);

x0 = [0.2; 0; 0; 0]; % empieza quieto y girado 11 grados

polos = eig(A);

%% LQR
Q = eye(4);
Q(3,3) = 5;
Q(1,1) = 50;
R = 5;