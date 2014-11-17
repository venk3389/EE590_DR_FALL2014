%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%											
%   "Copyright (c) 2004, 2005 The University of Southern California"				
%   All rights reserved.								
%											
%   Permission to use, copy, modify, and distribute this script and its		
%   documentation for any purpose, without fee, and without written agreement is	
%   hereby granted, provided that the above copyright notice, the following		
%   two paragraphs and the names in the credits appear in all copies of this software.		
%											
%   NO REPRESENTATIONS ARE MADE ABOUT THE SUITABILITY OF THE SCRIPT FOR ANY		
%   PURPOSE. IT IS PROVIDED "AS IS" WITHOUT EXPRESS OR IMPLIED WARRANTY. NO 
%   LIABILITY IS ASSUMED BY THE DEVELOPERS.
%											
%											
%   Authors:		Marco Zuniga, Rahul Urgaonkar
%   Director:       Prof. Bhaskar Krishnamachari
%   Autonomous Networks Research Group, University of Southern California
%   http://ceng.usc.edu/~anrg
%
%   Contact: marcozun@usc.edu
%   Previous Version: 1.0, 2004/07/02
%   Current Version 1.1
%   Date last modified: 2005/12/20							
%											
%   Anything following a "%" is treated as a comment.					
%											
%											
%   Description:									
%	LINKLAYERMODEL  generates an instance of the connectivity graph of a wireless 					
%                   sensor network  
%       All input parameters (except one, explained below) should be entered in inputFile.m.
%       Example
%       >> [ topology, prrM] = linklayermodel;
%           topology    provides the x and y coordinates in a Nx2 matrix where
%                       N is the number of nodes, first column provides X
%                       coordinates and second the Y coordinates.
%           prrM        Is the link quality matrix, quality between 0 and 1
%
%       Different topologies (Grid, Uniform, Random, File )can be generated in inputFile.m
%           if an specific topology is desired, it has to be given as an
%           input argument (Nx2 matrix). Example
%       >> [ topology, prrM] = linklayermodel(desiredTopology);
%											
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%function [topology, prrM] = LinkLayerModel(varargin)

function [topology, linkPRR,linkRSSI, linkPE] = LinkLayerModel(SIM_TIME_STEPS,TOPOLOGY_XY,PATH_LOSS_EXPONENT,SHADOWING_STANDARD_DEVIATION,MODULATION,ENCODING,PREAMBLE_LENGTH,FRAME_LENGTH,NUMBER_OF_NODES,TERRAIN_DIMENSIONS_X,TERRAIN_DIMENSIONS_Y,TOPOLOGY,GRID_UNIT,TEMPORAL_CORRELATION_COEFFICIENT,SPATIAL_CORRELATION_COEFFICIENT)

%numInputs = length(varargin);
%if (numInputs > 1)
%	error('Error: only input argument can be topology');
%end

%cord_xy = TOPOLOGY_XY
% Default Values
simTimeSteps = SIM_TIME_STEPS;
PL_D0 = 55.0;
D0 = 1.0;
OUTPUT_POWER = -7.0;
NOISE_FLOOR = -105.0;
ASYMMETRY = 1;
COVM = [3.7 -3.3; -3.3 6.0];

% load input file
%[nameInFile] = inputFile
%load(nameInFile);

%if ( (TOPOLOGY == 4) && (numInputs == 0) )
%	error('Error: enter topology as an argument');
%elseif ( (TOPOLOGY == 4) && (numInputs == 1) )
%    [inVar] = deal(varargin{1})
%    topology = inVar
%end

% some security checks
if(PATH_LOSS_EXPONENT < 0)
	error('Error: value of PATH_LOSS_EXPONENT must be positive');
end
if(SHADOWING_STANDARD_DEVIATION < 0)
	error('Error: value of SHADOWING_STANDARD_DEVIATION must be positive');
end
if(PL_D0 < 0)
	error('Error: value of PL_D0 must be positive');
end
if(D0 < 0)
	error('Error: value of D0 must be positive');
end
if(PREAMBLE_LENGTH < 0)
	error('Error: value of PREAMBLE_LENGTH must be positive');
end
if(FRAME_LENGTH < 0)
	error('Error: value of FRAME_LENGTH must be positive');
end
if(NUMBER_OF_NODES < 0)
	error('Error: value of NUMBER_OF_NODES must be positive');
end
if(TERRAIN_DIMENSIONS_X*TERRAIN_DIMENSIONS_Y < 0)
	error('Error: value of dimensions must be positive');
end
if( (COVM(1,2) ~= COVM(2,1)) || ( max(COVM(1,1), COVM(2,2)) < COVM(1,2) ) )
  	error('Error: COV must be positive-definite');
end
    
area = TERRAIN_DIMENSIONS_X*TERRAIN_DIMENSIONS_Y;

%%%%%%%%%%%%%%%%%%%%%
% Create Topology
%%%%%%%%%%%%%%%%%%%%%

if      (TOPOLOGY == 1) % GRID
    
    if (GRID_UNIT < D0)
        error('value of GRID_UNIT must be greater than D0');
    end
    
    if (sqrt(NUMBER_OF_NODES) ~= round(sqrt(NUMBER_OF_NODES)))
        error('Number of nodes should be a perfect square');
    end
    
    for i=1:NUMBER_OF_NODES
        % X coordinate
        topology(i,1) = GRID_UNIT*rem(i-1, sqrt(NUMBER_OF_NODES));
        % Y coordinate
        topology(i,2) = GRID_UNIT*floor((i-1)/(sqrt(NUMBER_OF_NODES)));
    end
    
elseif  (TOPOLOGY == 2) % UNIFORM
    
    if (( TERRAIN_DIMENSIONS_X < 0) || (TERRAIN_DIMENSIONS_Y < 0))
        error('Terrain dimensions must be positive');
    end
    
    cellLength = sqrt(area/NUMBER_OF_NODES);
    nodesX = ceil(TERRAIN_DIMENSIONS_X/cellLength);
    cellLength = TERRAIN_DIMENSIONS_X/nodesX;
    % 1.4 (below) is an arbitrary number chosen to decrease the probability
    % that nodes get closer than D0 to one another.
    if (cellLength < D0*1.4)
        error('UNIFORM topology: density too high, increase area');
    end
    for i=1:NUMBER_OF_NODES
        topology(i,1) = ((rem(i-1, nodesX))*cellLength) + rand(1)*cellLength;
        topology(i,2) = ((floor((i-1)/nodesX))*cellLength) + rand(1)*cellLength;
        % rest of for loop checks that no internode distance is smaller than d0
        wrongPlacement = 1;
        while (wrongPlacement == 1)
            for j = 1:i
                xdist = topology(i,1) - topology(j,1);
                ydist = topology(i,2) - topology(j,2);
                dist = sqrt(xdist^2 + ydist^2);
                if ((dist < D0) && (i ~= j))  
                    topology(i,1) = ((rem(i-1, nodesX))*cellLength) + rand(1)*cellLength;
                    topology(i,2) = ((floor((i-1)/nodesX))*cellLength) + rand(1)*cellLength;
                    wrongPlacement = 1;
                    break;
                end
            end       
            if (j==i)
                wrongPlacement = 0;
            end         
        end
    end

elseif  (TOPOLOGY == 3) % RANDOM
    
    if (( TERRAIN_DIMENSIONS_X < 0) || (TERRAIN_DIMENSIONS_Y < 0))
        error('Terrain dimensions must be positive');
    end
    cellLength = sqrt(area/NUMBER_OF_NODES);
    % 1.4 (below) is an arbitrary number chosen to decrease the probability
    % that nodes get closer than D0 to one another.
    if (cellLength < D0*1.4)
        error('RANDOM topology: density too high, increase area');
    end
    for i=1:NUMBER_OF_NODES
        topology(i,1) = TERRAIN_DIMENSIONS_X*rand(1);
        topology(i,2) = TERRAIN_DIMENSIONS_Y*rand(1);
        % rest of for loop checks that no internode distance is smaller than d0
        wrongPlacement = 1;
        while (wrongPlacement == 1)
            for j=1:i
                xdist = topology(i,1) - topology(j,1);
                ydist = topology(i,2) - topology(j,2);
                dist = sqrt(xdist^2 + ydist^2);
               	if ((dist < D0) && (i ~= j))
               		topology(i,1) = TERRAIN_DIMENSIONS_X*rand(1);
               		topology(i,2) = TERRAIN_DIMENSIONS_Y*rand(1);
               		wrongPlacement = 1;
               		break;
                end
            end 
            if (j==i)
         	    wrongPlacement = 0;
            end
        end
    end
    
elseif(TOPOLOGY==4)

% The input matrix topology has dimensions NUMBER_OF_NODESx2 
% where
%       topology(i,1) denotes the x coordinates of node i
%       topology(i,2) denotes the y coordinate of node i
    for nodeIdx = 1:NUMBER_OF_NODES
      topology(nodeIdx,:) = TOPOLOGY_XY{nodeIdx}(:,:); 
    end
    
    %topology{1}(:,:)
    [p, q] = size(topology);
    if (p ~= NUMBER_OF_NODES)
        error('Number of nodes in file topology.m does not agree with NUMBER_OF_NODES');
    end
    if (q ~= 2)
        error('Wrong format of file topology.m');
    end
    
    for i=1:NUMBER_OF_NODES
        for j=1:i
            xdist = topology(i,1) - topology(j,1);
            ydist = topology(i,2) - topology(j,2);
            dist = sqrt(xdist^2 + ydist^2);
            if ( (dist < D0) && (i ~= j) )
                error('Error: topology.m contains internode distances less than D0');
            end
        end
    end
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Determine Output Power and Noise Floor
%       both follow a gaussian distribution
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
noiseFloor = zeros(1,NUMBER_OF_NODES);
outputPower = zeros(1,NUMBER_OF_NODES);
for i=1:NUMBER_OF_NODES
    if  (ASYMMETRY == 1)
        % Cholesky Decomposition is used to generate multivariate random
        % variables:
        %   covariance matrix COVM = T' x T
        %   T is a 2x2 upper triangular 
        %       P_T   = P_T + T(1,1)* rn1
        %       P_N   = P_N + T(1,2) * rn1 + T(2,2) * rn2
        %  where rn1 and rn2 are normal(0,1) random variables.
        
        T11 = sqrt(COVM(1,1));
        T12 = COVM(1,2)/sqrt(COVM(1,1));
        %T21 = 0;
        T22 = sqrt( (COVM(1,1)*COVM(2,2)-COVM(1,2)^2) / COVM(1,1) );
       
        rn1 = randn;
        rn2 = randn;
       
        noiseFloor(i)  = NOISE_FLOOR + T11 * rn1;
        outputPower(i) = OUTPUT_POWER + T12 * rn1 + T22 * rn2;
    else
        
        noiseFloor(i)  = NOISE_FLOOR;
        outputPower(i) = OUTPUT_POWER;
    end
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Obtain RSSI
%       use topology information and
%       channel parameters
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% declare two cells for the Gaussian fading variable
% let's run 10 times of simulation for each location
psi = cell(1,simTimeSteps);
gfv = cell(1,simTimeSteps);
alpha = TEMPORAL_CORRELATION_COEFFICIENT; %can be any value between 0 and 1
% alpha is a parameter for the temporal correlation of channels
% when alpha is small, the channels are very highly correlated
% when it is large, they are essentially independent at each time
rssi = cell(1,simTimeSteps); %accordingly, our rssi should also be a cell
%create MU, SIGMA here
MU = zeros(1,NUMBER_OF_NODES^2);
SIGMA = zeros(NUMBER_OF_NODES^2,NUMBER_OF_NODES^2);
dmin = GRID_UNIT/10; % in order to generate a sharper covariance function
dmax = sqrt(2)*sqrt(NUMBER_OF_NODES)*GRID_UNIT;
beta = SPATIAL_CORRELATION_COEFFICIENT; %can be any value between 0 and 1
delta = beta*dmin + (1-beta)*dmax;
% delta can be any value between dmin and dmax
% when delta is large, the covariance for the same distance will be larger
% therefore when beta is small the covariance will be larger
% therefore when beta is small the links will be more spatially correlated
% and when beta is large they will be less correlated

%figure out dist
for m=1:NUMBER_OF_NODES^2
    im = floor(m/NUMBER_OF_NODES)+1;
    jm = mod(m,NUMBER_OF_NODES);
    if (jm==0)
        jm = NUMBER_OF_NODES;
        im = im-1;
    end
    imx = floor(im/sqrt(NUMBER_OF_NODES))+1;
    imy = mod(im,sqrt(NUMBER_OF_NODES));
    if (imy==0)
        imy = sqrt(NUMBER_OF_NODES);
        imx = imx-1;
    end
    jmx = floor(jm/sqrt(NUMBER_OF_NODES))+1;
    jmy = mod(jm,sqrt(NUMBER_OF_NODES));
    if (jmy==0)
        jmy = sqrt(NUMBER_OF_NODES);
        jmx = jmx-1;
    end
    a = (imx+jmx)/2; %x-coordinate of the midpoint
    b = (imy+jmy)/2; %y-coordinate of the midpoint
    for n=1:NUMBER_OF_NODES^2
        in = floor(n/NUMBER_OF_NODES)+1;
        jn = mod(n,NUMBER_OF_NODES);
        if (jn==0)
            jn = NUMBER_OF_NODES;
            in = in-1;
        end
        inx = floor(in/sqrt(NUMBER_OF_NODES))+1;
        iny = mod(in,sqrt(NUMBER_OF_NODES));
        if (iny==0)
            iny = sqrt(NUMBER_OF_NODES);
            inx = inx-1;
        end
        jnx = floor(jn/sqrt(NUMBER_OF_NODES))+1;
        jny = mod(jn,sqrt(NUMBER_OF_NODES));
        if (jny==0)
            jny = sqrt(NUMBER_OF_NODES);
            jnx = jnx-1;
        end
        c = (inx+jnx)/2; %x-coordinate of the midpoint
        d = (iny+jny)/2; %y-coordinate of the midpoint
        dist = sqrt((a-c)^2+(b-d)^2);
        SIGMA(m,n) = (exp(-dist/delta)); 
    end
end

for t=1:simTimeSteps
       R = mvnrnd(MU, SIGMA);
   for i=1:NUMBER_OF_NODES
       for j=i+1:NUMBER_OF_NODES  
           xdist = topology(i,1) - topology(j,1);
           ydist = topology(i,2) - topology(j,2);
           dist = sqrt(xdist^2 + ydist^2);                        
           gfv{1,t}(i,j) = R(NUMBER_OF_NODES*(i-1)+j)*SHADOWING_STANDARD_DEVIATION;
           % initialization for each point
           if (t==1)
               psi{1,t}(i,j) = gfv{1,t}(i,j);
           else % generate fading variable based on time correlation
               psi{1,t}(i,j) = alpha*gfv{1,t}(i,j) + (1-alpha)*psi{1,t-1}(i,j);
           end
           % mean rssi decay dependent on distance 
           pathLoss = - PL_D0 - (10*PATH_LOSS_EXPONENT*(log(dist/D0)/log(10))) + psi{1,t}(i,j);
           % assymetric links are given by running two different
           % R.V.s for each unidirectional link.
           %   NOTE: this approach is not accurate, assymetry is due mainly to
           %   to hardware imperfections and not for assymetric paths
           rssi{1,t}(i,j) = outputPower(i) + pathLoss;
           rssi{1,t}(j,i) = outputPower(j) + pathLoss;
        end
    end
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Obtain Prob. of bit Error
%       use rssi and modulation chosen
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
pe = cell(1,simTimeSteps);
for i=1:NUMBER_OF_NODES
    for j=1:NUMBER_OF_NODES
        for t=1:simTimeSteps
            if (i==j)
                pe{1,t}(i,j) = 0;
            else
                snr = ( 10^((rssi{1,t}(i,j) - noiseFloor(j))/10) ) / .64;  % division by .64 converts from Eb/No to RSSI
                                                                  % this is specific for each radio (read paper: Data-rate(R) / Bandwidth-noise(B)) 
                if (MODULATION == 1)    % NCASK
                    pe{1,t}(i,j) = 0.5*( exp(-0.5*snr) + Q( sqrt(snr) ) );
                elseif(MODULATION == 2) % ASK
                    pe{1,t}(i,j) = Q( sqrt(snr/2) );
                elseif(MODULATION == 3) % NCFSK
                    pe{1,t}(i,j) = 0.5*exp(-0.5*snr);
                elseif(MODULATION == 4) % FSK
                    pe{1,t}(i,j) = Q( sqrt(snr) );
                elseif(MODULATION == 5) % BPSK
                    pe{1,t}(i,j) = Q( sqrt(2*snr) );
                elseif(MODULATION == 6) % DPSK
                    pe{1,t}(i,j) = 0.5*exp(-snr);
                else
                    error('MODULATION is not correct');
                end
            end
        end
    end
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Obtain PRR
%   use prob. of error and encoding scheme
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
prrM = cell(1,simTimeSteps);
for i = 1:NUMBER_OF_NODES
    for j = 1:NUMBER_OF_NODES
        for t = 1:simTimeSteps
            if (i == j)
                prrM{1,t}(i,j) = 1;
            else
                preseq = (1-pe{1,t}(i,j))^(8*PREAMBLE_LENGTH);
                if (ENCODING == 1)      % NRZ
                    prrM{1,t}(i,j) = preseq*((1-pe{1,t}(i,j))^(8*(FRAME_LENGTH-PREAMBLE_LENGTH)));
                elseif (ENCODING == 2)  % 4B5B
                    prrM{1,t}(i,j) = preseq*((1-pe{1,t}(i,j))^(8*1.25*(FRAME_LENGTH-PREAMBLE_LENGTH)));
                elseif (ENCODING == 3)  % MANCHESTER
                    prrM{1,t}(i,j) = preseq*((1-pe{1,t}(i,j))^(8*2*(FRAME_LENGTH-PREAMBLE_LENGTH)));
                elseif (ENCODING == 4)  % SECDED
                    prrM{1,t}(i,j) = ((preseq*((1-pe{1,t}(i,j))^8)) + (8*pe{1,t}(i,j)*((1-pe{1,t}(i,j))^7)))^((FRAME_LENGTH-PREAMBLE_LENGTH)*3);
                else
                    error('ENCODING is not correct');
                end
            end
        end
    end
end

% Overall result
linkPRR = zeros(NUMBER_OF_NODES*NUMBER_OF_NODES,simTimeSteps);
linkRSSI = zeros(NUMBER_OF_NODES*NUMBER_OF_NODES,simTimeSteps);
linkPE = zeros(NUMBER_OF_NODES*NUMBER_OF_NODES,simTimeSteps);

for t = 1:simTimeSteps
    linkPRR(:,t) = reshape(prrM{1,t}',[],1);
    linkRSSI(:,t) = reshape(rssi{1,t}',[],1);
    linkPE(:,t) = reshape(pe{1,t}',[],1);
end

%linkimg = mat2gray(link);
%imshow(linkimg)

end