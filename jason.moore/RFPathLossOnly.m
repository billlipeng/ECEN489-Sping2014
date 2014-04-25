x=csvread("team1.csv");
interp=csvread("team1tobeinterp.csv");
xout=interp(:,2);
yout=interp(:,1);
zout=interp(:,2);

#use path loss
frequency= 2.4*10^9;
wavelength=3*10^8/(2.4*10^9);
# find max for origin point

for i = 1:length(xout)
[Mindex,d]=RFcompareORIG(x,xout(i),yout(i));
change=abs(20*log10(4*pi*d/wavelength))
zout(i)= x(Mindex,3)-change;
zout(1)=x(1,3);
endfor
output=[xout,yout,zout];
csvwrite("OutputPathloss.csv",output);
error=mean((zout-x(:,3)).^2)