function [Mindex, d]= RFcompareORIG(x0,z1,z2)
R=6371;
[vector,Vindex]=max(x0(:,3));
vector = [x0(Vindex,2)-z1,x0(Vindex,1)-z2];
vector = vector.*pi./180;
vector= abs(vector);

distance= abs(sin(vector(:,2)./2).*sin(vector(:,2)./2)-sin(vector(:,1)./2).*sin(vector(:,1)./2).*cos(z2).*cos(vector(:,1)));
distance = 2.*atan2(sqrt(distance),sqrt(1-distance));
distance=R.*distance;
d=distance;
Mindex=Vindex;
endfunction