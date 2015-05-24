#include <iostream>
#include <string>

long long count[10] = { 0 };

void getMinimal(std::string& result, size_t pos) {
  
  for(int i = 0; i < 10; ++i) {
    while(count[i]--) {
      char c = i + '0';
      result[pos++] = c;
    }
  }
  //std::cout << nt + minimal << std::endl;
  
}

char getFirstLarger(int a) {
  for(int i = a + 1; i < 10; ++i) {
    if(count[i]) {
      return char(i + '0');
    }
  }
  return 0;
}

int main() {
  std::string ss;
  std::cin >> ss;
  size_t z_pos = ss.size() - 1;
 
  char max = ss[z_pos]; 
  bool finished = false;
 
  while(z_pos < ss.size()) {
    char z = ss[z_pos];
    ++count[z - '0'];
    
    
    if(max > z) {
      char larger = getFirstLarger(z - '0');
      --count[larger - '0'];
      ss[z_pos++] = larger;
  
      getMinimal(ss, z_pos);
      
      finished = true;
      break;
    } else {
      max = z;
    }
    
    --z_pos;
  }
  
  if(finished) {
    std::cout << ss.c_str() << std::endl;
  } else {
    std::cout << "impossible" << std::endl;
  }

  return 0;
}
