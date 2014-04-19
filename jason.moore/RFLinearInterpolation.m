x=csvread("Samples.csv");
latituderef1=min(x(:,1))
longituderef1=min(x(:,2))
latituderef2=max(x(:,1))
longituderef2=max(x(:,2))
interp=csvread("GridLayout-Team1.csv");
[xi,yi,zi]=griddata(x(:,2),x(:,1),x(:,3),interp(:,2),interp(:,1));

xout=xi(:);
yout=yi(:);
zout=zi(:);

#use path loss
frequency= 2.4*10^9;
wavelength=3*10^8/(2.4*10^9);
# find max for origin point

for i = 1:length(xout)
if (isnan(zout(i))==1)
[Mindex,d]=RFcompareORIG(x,xi(i),yi(i));
change=abs(20*log10(4*pi*d/wavelength))
zout(i)= x(Mindex,3)-change;
endif
endfor
output=[xout,yout,zout];
csvwrite("Output.csv",output);
