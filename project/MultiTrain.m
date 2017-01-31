%train multiple data of certain word
%Input is transition matrix guess A, emission matrix guess B, Initial state probability guess P
%and obeservation sequence O, which is stored in cell structure 1*#number
%of this word
function [A,B] = MultiTrain(A,B,I,O)
      noword=size(O,2);%number of this word
      N = size(A,1);%state
      M = size(B,2);%level
      gamma = cell(1,noword);
      kexi = cell(1,noword);
      flag = 0;%flag for convergence
      while( flag == 0)
        for s=1:noword
          O_now = O{1,s}';
          T = size(O_now,2);%length of observation          
          % Forward Variable alpha
          aph = zeros(T,N);
          kexi_now = zeros(T-1,N,N);
          for i = 1:N
              aph(1, i) = I(i) * B(i, O_now(1));
          end
          for t = 1:T-1
              aph(t+1, :) = aph(t, :) * A .* (B(:, O_now(t+1))');
          end
          PfO = sum(aph(end, :));
          % Backward Variable beta
          beta = zeros(T, N);
          beta(T, :) = 1;
          for t = T-1:-1:1
               beta(t, :) = (A * (B(:, O_now(t+1)) .* (beta(t+1, :))'))';
          end          
          % Variable gamma
          gamma{1,s} = (aph .* beta) / PfO;
          % Variable kexi
          for t=1:T-1
              kexi_now(t,:,:) = aph(t,:)'*(B(:,O_now(t+1))'.*beta(t+1,:)).*A;
          end
          kexi{1,s} = kexi_now(:,:,:)/ PfO;
        end
      
          %update B
          for k = 1:M
              sum1=zeros(1,N);sum2=zeros(1,N);
              for i = 1:noword
                 sum1 = sum1 + sum(gamma{1,i}' .* (ones(N,1)*((O{1,i} == k)')),2)';
                 sum2 = sum2 + sum(gamma{1,i}, 1);
              end
              B_new(:, k) = sum1 ./ sum2;
          end
          %update A
          for i = 1:N
              sum1=zeros(1,N);sum2=zeros(1,N);
              for s = 1:noword
                  temp = sum(kexi{1,s},1);
                  tempp(1,:) = temp(1,i,:);
                  sum1 = sum1 + tempp;
                  sum2 = sum2 + sum(tempp);
              end
              A_new(i,:) = sum1./sum2;
          end
          
          if(sum(sum((A_new-A).^2))>=1e-10)
             A = A_new;
             B = B_new;
          else flag = 1;
          end
      end
      B(B==0) = 1e-10;
      A(A==0) = 1e-10;
end
  

