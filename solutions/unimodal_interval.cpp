#include <cstdio>
#include <vector>
#include <climits>
#include <iostream>
#include <utility>
#include <cmath>

std::vector<int> v;

long double squareDistance(const long double b) {
  long double c, sum = 0;
  for(int i = 0; i < v.size(); ++i) {
    c = (v[i] - b) * 1e6;
    sum += c * c;
  }
  
  return sum / 1e12;
}

double ter_search(double l, double h) {
  std::pair<double, double> lo, hi, mid_left, mid_right;
  lo.first = l;
  lo.second = squareDistance(lo.first);
  
  hi.first = h;
  hi.second = squareDistance(hi.first);
  
  double d;
  while(hi.second - lo.second >= 0.000000001 || hi.first - lo.first >= 0.000000001) {
    d = (hi.first - lo.first) / 3;
    
    mid_left.first = lo.first + d;
    mid_left.second = squareDistance(mid_left.first);
    
    mid_right.first = hi.first - d;
    mid_right.second = squareDistance(mid_right.first);
    
    
    if(mid_left.second > mid_right.second) {
      lo = mid_left;
    } else {
      hi = mid_right;
    }
  }

  return hi.second;  
}

long double phi = (1 + sqrt(5)) / 2;
long double resphi = 2 - phi;
long double tau = 10e-12;
 
long double goldenSectionSearch(long double a, long double b, long double c) {
    long double x;
    if (c - b > b - a) {
      x = b + resphi * (c - b);
    } else {
      x = b - resphi * (b - a);
    }
    
    if (c - a < tau * (b + x)) { 
      return (c + a) / 2; 
    }
    
    if (squareDistance(x) < squareDistance(b)) {
      if (c - b > b - a) {
        return goldenSectionSearch(b, x, c);
      } else { 
        return goldenSectionSearch(a, x, b);
      }
    } else {
      if (c - b > b - a) {
        return goldenSectionSearch(a, b, x);
      } else {
        return goldenSectionSearch(x, b, c);
      }
    }
  }

int main() {
  int N, x, lo = INT_MAX, hi = INT_MIN;
  scanf("%d", &N);
  
  for(int i = 0; i < N; ++i) {
    scanf("%d", &x);
    v.push_back(x);
    
    if(x < lo) {
      lo = x;
    }
    
    if(x > hi) {
      hi = x;
    }
  }


  long double g = goldenSectionSearch( lo, (hi-lo)/ 2 + lo, hi);
  printf("%.3LF", squareDistance(g));

  return 0;
}
