#include <iostream>
#include <vector>


class BinaryIndexTree {
private:
  int n;
  std::vector<int> A;
 
public:  
  BinaryIndexTree(int N) {
    n = N;
    A.resize( n + 1 );
    for( int i = 0; i <= n; ++i ) A[i] = 0;
  }
 
  int get(int lo) {
    int hi = n, ret = 0;
    for( ; hi > 0; hi -= hi & -hi ) ret += A[hi];
    for( ; lo > 0; lo -= lo & -lo ) ret -= A[lo];
    return ret;
  }
 
  void set(int i) {
    for( ; i <= n; i += i & -i ) A[i] += 1;
  }
};

int main() {
  int N, x;
  std::cin >> N;
  BinaryIndexTree bit(N);
  
  long long sum = 0;
  for(int i = 0; i < N; ++i) {
    std::cin >> x;
    sum += bit.get(x);
    bit.set(x);
  }
  std::cout << sum;
  return 0;
}
