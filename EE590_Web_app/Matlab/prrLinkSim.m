function [prrlink] = prrLinkSim(NumNodes,SimTime)

    NUMBER_OF_NODES = NumNodes
    time = SimTime
    prrM = cell(1,time);
    count = 0;
    for i=1:NUMBER_OF_NODES
        for j=1:NUMBER_OF_NODES
            for t=1:time
              count = count + 1;
              prrM{1,t}(i,j) = count;
            end
        end
    end

    % Overall result
    prrlink = zeros(NUMBER_OF_NODES*NUMBER_OF_NODES,time);
    prrlinks_t1 = prrM{1,1}(:,:)
    prrlinks_t2 = prrM{1,2}(:,:)
    prrlinks_t3 = prrM{1,3}(:,:)
    prrlinks_t4 = prrM{1,4}(:,:)

    for t = 1:time
        %prrlink(:,t) = reshape(prrM{1,t},[],1);
        prrlink(:,t) = reshape(prrM{1,t}',[],1);
    end

    prrlink(:,:)
end

% NumNodes = 4, SimTime = 4

% prrlinks_t1 =
%       l1    l2    l3   l4
%
% l1     1     5     9    13
% l2    17    21    25    29
% l3    33    37    41    45
% l4    49    53    57    61
% 
% 
% prrlinks_t2 =
% 
%      2     6    10    14
%     18    22    26    30
%     34    38    42    46
%     50    54    58    62
% 
% 
% prrlinks_t3 =
% 
%      3     7    11    15
%     19    23    27    31
%     35    39    43    47
%     51    55    59    63
% 
% 
% prrlinks_t4 =
% 
%      4     8    12    16
%     20    24    28    32
%     36    40    44    48
%     52    56    60    64
% 
% 
% ans =

%          t1    t2    t3    t4
% 
% l1-l1     1     2     3     4
% l1-l2     5     6     7     8
% l1-l3     9    10    11    12
% l1-l4    13    14    15    16
% l2-l1    17    18    19    20
% l2-l2    21    22    23    24
% l2-l3    25    26    27    28
% l2-l4    29    30    31    32
% l3-l1    33    34    35    36
% l3-l2    37    38    39    40
% l3-l3    41    42    43    44
% l3-l4    45    46    47    48
% l4-l1    49    50    51    52
% l4-l2    53    54    55    56
% l4-l3    57    58    59    60
% l4-l4    61    62    63    64