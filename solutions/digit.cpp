#include <iostream>

typedef long long int ll;

int main() {
  ll x;
  std::cin >> x;

  ll l = 9;
  int i = 1;
  while(x > l * i) {
    x -= l * i++;
    l *= 10;
  }
  
  l /= 9;
  ll num = (x - 1) / i + l; 
  ll pos = (x-1) % i;
  pos = i - 1 - pos; 
  while(pos--) {
    num /= 10;
  }
  
  std::cout << num % 10 ;
  

  return 0;
}
