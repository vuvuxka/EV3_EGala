clc
clear
close all

M = 0.7;
m = 0.027;
b = 0.1;
g = 9.8;
l = 0.3;
I = 0.006; %M*l^2/3; %0.006;
radio = 42/(2*1000);
vol = 9; % Motor de 9 V
Torque = 17.3/100; % Torque del motor de 17.3 N*cm

deg2rad = pi / 180;
rad2deg = 1/deg2rad;
rad2m = radio;
m2rad = 1/rad2m;
T1 = diag([deg2rad; deg2rad; rad2m; rad2m]); % cambio del controlador al del modelo
perc2N = (1/100)*Torque/radio;

p = I*(M+m)+M*m*l^2; %denominator for the A and B matrices

A = [0      1              0           0;
     0 -(I+m*l^2)*b/p  (m^2*g*l^2)/p   0;
     0      0              0           1;
     0 -(m*l*b)/p       m*g*l*(M+m)/p  0];
B = [     0;
     (I+m*l^2)/p;
          0;
        m*l/p];
C = [1 0 0 0;
     0 0 1 0];
D = [0;
     0];

states = {'x' 'x_dot' 'phi' 'phi_dot'};
inputs = {'u'};
outputs = {'x'; 'phi'};

sys_ss = ss(A,B,C,D,'statename',states,'inputname',inputs,'outputname',outputs);

poles = eig(A);
x0 = [0.2; 0; 0; 0];

Q = C'*C;
Q(1,1) = 5000;
Q(3,3) = 300;
R = 1;
K = lqr(A,B,Q,R)
% K_angulo = 21.6; % parametro del angulo
% K_rate = 1.1520; % parametro de la velocidad angular
% K_pos = 0.1728; % parametro de la posicion
% K_vel = 0.1152; % parametro de la velocidad
% K = [K_angulo K_rate K_pos K_vel];

% Kp = 0.4; 
% Ki = 14;
% Kd = 0.005;

Kp = 1; Ki = 1; Kd = 1;

% Ac = [(A-B*K)];
% Bc = [B];
% Cc = [C];
% Dc = [D];
% 
% states = {'x' 'x_dot' 'phi' 'phi_dot'};
% inputs = {'r'};
% outputs = {'x'; 'phi'};
% 
% sys_cl = ss(Ac,Bc,Cc,Dc,'statename',states,'inputname',inputs,'outputname',outputs);
% 
% t = 0:0.01:5;
% r =0.2*ones(size(t));
% [y,t,x]=lsim(sys_cl,r,t);
% [AX,H1,H2] = plotyy(t,y(:,1),t,y(:,2),'plot');
% set(get(AX(1),'Ylabel'),'String','cart position (m)')
% set(get(AX(2),'Ylabel'),'String','pendulum angle (radians)')
% title('Step Response with LQR Control')