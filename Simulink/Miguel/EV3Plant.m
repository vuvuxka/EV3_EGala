% Defining variables

M= 0.027;   % masa de la rueda   % antes 0.5
 
m= 0.7;   % masa del pendulo     % antes 0.5
 
b= 0.1;   % coeficiente de friccion
 
l=.15;   % longitud del pendulo al centro de masas  % antes 0.3
 
I= .09;  % momento del pendulo    % antes 0.06
 
g= 9.8;   % gravedad
 
d=I*(M+m)+M*m*l^2;
 


%  State space model
A=[0 1 0 0;
   m*g*l*(M+m)/d 0 0 m*l*b/d;
   0 0 0 1;
   -g*m^2*l^2/d 0 0 -b*(I+m*l^2)/d];
 
B=[0;-m*l/d;0;(I+m*l^2)/d];

    
C = [1 0 0 0;
     0 1 0 0;
     0 0 1 0;
     0 0 0 1]
D = [0;
     0;
     0;
     0]
 
 
 Q = [50 0 0 0;
     0 1 0 0;
     0 0 50 0;
     0 0 0 1]

R = 1
 
K = lqr(A,B,Q,R)%state-feedback control gain matrix
Ac = A-B*K;%control matrix


 Kp=1.5;
 Ki=0.5;
 Kd=0.005;
 