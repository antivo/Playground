#include <cstdio>

long long x = 0;
long long y = 1;
long long gcd;

void ext_gcd(long long value1, long long value2) {
    long long prevx = 1, prevy = 0;
    long long holdValue, quotient;
    
    while (value2 != 0) {
        holdValue = value2;
        quotient = value1 / value2;
        value2 = value1 % value2;
        value1 = holdValue;
        holdValue = x;
        x = prevx - quotient * x;
        prevx = holdValue;
        holdValue = y;
        y = prevy - quotient * y;
        prevy = holdValue;
    }
 
    x = prevx;
    y = prevy;
    gcd = value1;
}

int main() {
  long long l;
  scanf("%lld", &l);
  long long t, m;
  scanf("%lld %lld", &t, &m);
  long long vt, vm;
  scanf("%lld %lld", &vt, &vm);
  
  vt %= l;
  vm %= l;
  
  if(t == m) {
    printf("%d", 0);
  } else if (vt == vm) {
    printf("INF");
  } else {
    long long dv = vt - vm;
    long long ds = t -  m;
  
    if(dv < 0) {
      dv = -dv;
      ds = -ds;
    }
    
    ds = -ds;
    l = -l; 

    ext_gcd(dv, l);
    if(ds % gcd == 0) {
      long long multiply = ds / gcd;
      
      x *= multiply;
      y *= multiply;
      
      long long x_modif = l / gcd; 
      long long y_modif = dv / gcd; 
      
      long long r;
      if(y < 0) {
        if(0 == y_modif) {
          printf("INF");
          return 0;
        }
        
        r = -y / y_modif;
        if(y % y_modif) {
          ++r;
        }
        
        y += r * y_modif;
        x -= r * x_modif;
      }
      
      if(x < 0) {
        if(x_modif == 0) {
          printf("INF");
          return 0;
        }
        
        r = x / x_modif;
        if(x % x_modif) {
          ++r;
        }
        
        y += r * y_modif;
        if(y < 0) {
          printf("INF");
          return 0;
        }
      
        x -= r * x_modif;
      }
      
      if(x > 0) {
        if(x_modif == 0) {
          printf("%lld", x);
          return 0;
        } 
        
        long long siq = 0;
        if (x_modif > 0) {
          siq = 1;
        } else {
          siq = -1;
        }
        
        x_modif *= siq;
        y_modif *= siq;
        while(1) {
          y += y_modif;
          if(y >= 0 && x > x_modif) {
            x -= x_modif;
          } else {
            break;
          }
        }
      }
      
      printf("%lld", x);
    } else {
      printf("INF");
    }
  }
  return 0;
}