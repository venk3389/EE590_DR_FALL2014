function mydisplay()
clc;
clear;
close all;

img = imread('Rthfloor.bmp');
% This creates the 'background' axes
figure;
imagesc(img);
colormap(hsv(128));
imagesc([-0.3 55], [-0.7 31.25], img);% corrections
hold on;

load('nodeCord.mat');
%figure;
%scrsz = get(0,'ScreenSize');
%figure('Position',[scrsz(3)/4 scrsz(4)/2 scrsz(3)/2 3*scrsz(4)/4]);
count = 1;
%for j = [2,5,7,8,12,17,22,26,28,29,31,33,40,42,46,48,52,56]
for j = [2,5,7,8,12,17,22,26,28,29,31,33,40,42,46,48,52,56,14,25,50,53,36,38]
%for j = [2,5,7,8,12,17,22,26,28,29,31,33,40,42,46,48,52,56,14,25]
%for j = [5,7,8,12,17,22,26,28,29,31,33,40,42,48,52,56]
%for j = [5,7,8,12,17,22,28,31,33,42,48,52]
%for j = [5,8,17,28,31,33,42,48]
%for j = [5,8,42,48]
            Xd(count) = X(j);
            Yd(count) = Y(j);
            count = count + 1;
end
h = plot(Xd/100,Yd/100,'b^',XW(1:8)/100,(YW(1:8) + 10)/100,'ro','LineWidth',4,'MarkerSize',15);
[hobj1, hobj2] = legend('Low Power 802.15.4','WiFi', 'Location','SouthWest');
textobj = findobj(hobj1, 'type', 'text');
set(textobj, 'Interpreter', 'latex', 'fontsize', 18);
textobj = findobj(hobj2, 'type', 'text');
set(textobj, 'Interpreter', 'latex', 'fontsize', 18);
%xlabel('x (m)','FontSize', 18);    %  add axis labels and plot title
%ylabel('y (m)','FontSize', 18);
%title('Equal RSS Lines','FontSize', 18);
axis equal;
axis([0 55 0 31]);
set(gca,'YTick',[0 5 10 15 20 25 30]);
set(gca,'XTick',[0 5 10 15 20 25 30 35 40 45 50 55]);
box on;
grid on;
grid minor;
%set(gca, 'Units','centimeters', 'Position',[1 1 20 20]);
hold on;
set(gca,'ydir','normal');
set(gca, 'LooseInset', get(gca, 'TightInset'));
set(gca,'DataAspectRatio',[1 1 1],...
        'PlotBoxAspectRatio',[1 1 1])
set(gcf,'PaperUnits','inches','PaperPosition',[0 0 12 7])
print -dpng RthFloorPlan.png -r100
%sort(intersectionPoints,'descend');
end

