#include <iostream>
#include <string>
#include <stack>

bool isBalanced(const std::string& sentence) {
  bool result = false;
  if(sentence.size()) {
    std::stack<char>* stack = new std::stack<char>;
    for(int i = 0; i < sentence.length(); ++i) {
      const char&c = sentence.at(i);
     
      if('(' == c || '[' == c || '{' == c) {
        stack->push(c);
      } else if(')' == c || ']' == c || '}' == c) {
        if(stack->empty()) {
          return false;
        }
        
        char t = stack->top();
        
        if (!((t == '(' && c == ')') || (t == '[' && c == ']') || (t == '{' && c == '}'))) {
          break;
        }
        
        stack->pop();      
      }
    }
    
    if(stack->empty()) {
      result = true;
    }
    
    
    delete stack;
  }
  return result;
}

int main() {
  int N;
  std::cin >> N;
  std::string sentence;
  while(N--) {
    std::cin >> sentence;
    if(isBalanced(sentence)) {
      std::cout << "ispravno" << std::endl;
    } else {
      std::cout << "neispravno" << std::endl;
    }
  }
  return 0;
}
