#include <cstdio>

long long lcm(long long m, long long n) {  
  long long a = m;
  long long b = n;
  while (a != b)  
    if (a < b)
      a += m;
    else
      b += n;
  return a;
}

int main() {
  long* sequence = new long[100005];

  long n;
  scanf("%ld", &n);
  long x;
  for(long i = 1; i <= n; ++i) {
    scanf("%ld", &x);
    if(x == i) {
      sequence[i] = 0;
    } else {
      sequence[x] = i;
    }
  }

  long long T = 1;
  long long newT;
  long amortize;
  for(long i = 1; i <= n; ++i) {
    const long& destination = i;
    if(sequence[destination]) {
      newT = 0;
      long dest = destination;
      do {
        ++newT;
        amortize = dest;
        dest = sequence[dest];
        sequence[amortize] = 0;
      } while(dest != destination);
      T = lcm(T, newT);
    }
  }
  
  printf("%lld", T);
  
  delete sequence;
  return 0;
}