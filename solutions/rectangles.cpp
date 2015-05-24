#include <cstdio>
#include <algorithm>
#include <climits>

int main() {


  int N;
  scanf("%d", &N);
  
  int XL = INT_MIN, XR = INT_MAX, YD = INT_MIN, YU = INT_MAX, xl, xr, yd, yu;
  bool exists = true;
  
  while(N--) {
    scanf("%d %d %d %d", &xl, &yd, &xr, &yu);
    
    if(exists) {
      // L < R, D < U
      if(xl > xr) {
        std::swap(xl, xr);
      }
      if(yd > yu) {
        std::swap(yd, yu);
      }

      // left to right, right to left, down to up, up to down;
      if(xl > XL) {
        XL = xl;
      }
      if(XR > xr) {
        XR = xr;
      }
      if(yd > YD) {
        YD = yd;
      }      
      if(YU > yu) {
        YU = yu;
      }

      // solution does not exist
      if(XL > XR || YD > YU) {
        exists = false;
      }
      
      
      
    }
    
  }
  
  if(!exists) {
    printf("0");
  }else {
    int x = XR - XL;
    int y = YU - YD;
    printf("%d", x*y);
  }

  return 0;
}
