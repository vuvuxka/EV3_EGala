%--------System Modeling-------%
 
% Defining variables
 
clc;
 
clear;
 
M= 0.027;   % masa de la rueda   % antes 0.5
 
m= 0.7;   % masa del pendulo     % antes 0.5
 
b= 0.1;   % coeficiente de friccion
 
l=.15;   % longitud del pendulo al centro de masas  % antes 0.3
 
I= .09;  % momento del pendulo    % antes 0.06
 
g= 9.8;   % gravedad
 
d=I*(M+m)+M*m*l^2;
 
% State-space model
 
disp('====================================')
 
disp('state space model of the system is:')
 
disp('====================================')
 
A=[0 1 0 0;
   m*g*l*(M+m)/d 0 0 m*l*b/d;
   0 0 0 1;
   -g*m^2*l^2/d 0 0 -b*(I+m*l^2)/d];
 
B=[0;-m*l/d;0;(I+m*l^2)/d];
 
    
C = [1 0 0 0;
     0 1 0 0;
     0 0 1 0;
     0 0 0 1];
D = [0;
     0;
     0;
     0];
 
system=ss(A,B,C,D)

% Transfer function of the system
 
disp('=======================================')
 
disp('The transfer function of the system is:')
 
disp('=======================================')
 
inputs = {'u'};
 
outputs = {'x';'tetha'; 'x2' ; 'tetha2'};
 
G=tf(system)
 
set(G,'InputName',inputs)
 
set(G,'OutputName',outputs)

%--------System Analysis-------%
 
%------Checking stability of the system--------%
 
%open-Loop impulse response
 
%inputs = {'u'};
 
%outputs = {'x';'theta'};
 
%set(G,'InputName',inputs)
 
%set(G,'OutputName',outputs)
 
%figure(1);clf;subplot(221)
 
%t=0:0.01:1;

% impulse(G,t);
 
%grid;
 
%title('Open-Loop Impulse Response of the system')
 
%Open-Loop step response
 
%subplot(222)
 
%t = 0:0.05:10;
 
%u = ones(size(t));
 
%[y,t] = lsim(G,u,t);
 
%%plot(t,y)
 
%title('Open-Loop Step Response of the system')
 
%axis([0 2 -50 50])
 
%legend('x','theta', 'x2', 'tetha2')
 
%grid;
 

% controlling using LQR Method
 
%-----------------------------
 
disp('===============================================')
 
disp('The weight matrices Q and R and the vector K:')
 
disp('===============================================')
 
%Q = C'*C;
 
%Q(1,1) = 80;%increasing the weight on the pendulum's angle(tetha)
 
%Q(3,3) = 400% increasing the weight on the cart's position(x)
 

Q = [50 0 0 0;
     0 1 0 0;
     0 0 50 0;
     0 0 0 1];

R = 1;
 
K = lqr(A,B,Q,R)%state-feedback control gain matrix
K = -[23.0811,2.5151,0.3162,1.0021];
Ac = A-B*K;%control matrix
 
system_c = ss(Ac,B,C,D);%the controlled system state space model
 
t = 0:0.01:10;
 
r =0.2*ones(size(t));
 
figure(3);clf
 
[y,t,x]=lsim(system_c,r,t);
 
[AX,H1,H2] = plotyy(t,y(:,1),t,y(:,2),'plot');
 
set(get(AX(1),'Ylabel'),'String','cart position (m)')
 
set(get(AX(2),'Ylabel'),'String','pendulum angle (radians)')
 
title('Step Response using LQR')
 
grid
 
 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%    PID  %%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


 Kp=1.5;
 Ki=0.5;
 Kd=0.005;

