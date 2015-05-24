#include <iostream>

int main() {
  int N, T;
  std::cin >> N;
  std::cin >> T;
  
  int A = 0, t = 0, a, b;
  while(N--) {
    std::cin >> a;
    std::cin >> b;
    
    if(t <= T) {
      if(t <= b) {
        t += a;
        ++A;
      }
    }
  }

  std::cout << A;
  
  return 0;
}
