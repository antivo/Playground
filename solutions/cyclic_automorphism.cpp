#include <iostream>
#include <string>

int getNumOfCyclicAutomorphism(const std::string& S) {
  int size = 1, idx = 0;
  for(int i = 1; i < S.size(); ++i) {
    idx %= size;
    if(S[i] == S[idx]) {
      ++idx;   
    } else {
      size = i + 1;
      idx = 0;
    } 
  }
  return S.size() / size;
}

int main() {
  std::string ss;
  std::cin >> ss;
  std::cout << getNumOfCyclicAutomorphism(ss);
  return 0;
}
