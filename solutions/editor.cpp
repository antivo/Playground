#include <iostream>
#include <string>

#include <stack>

const std::string LEFT("left");
const std::string RIGHT("right");
const std::string TYPE("type");
const std::string UNDO("undo");


struct Entry {
  int v;
  char l;
  enum {LEFT, RIGHT, TYPE, UNDO} action;
};

int main(){
  std::stack<Entry>* stack = new std::stack<Entry>;
  
  int n;
  std::cin >> n;
  
  std::string input;
  while(n--) {
    std::cin >> input;
    
    Entry e;
    if(!input.compare(LEFT)) {
      e.action = Entry::LEFT;
      std::cin >> e.v;
    } else if(!input.compare(RIGHT)) {
      e.action = Entry::RIGHT;
      std::cin >> e.v;
    } else if(!input.compare(TYPE)) {
      e.action = Entry::TYPE;
      std::cin >> e.l;
      
    } else if(!input.compare(UNDO)) {
      e.action = Entry::UNDO;
      std::cin >> e.v;
    }
    
    stack->push(e);
  }
  
  std::stack<Entry>* myStack = new std::stack<Entry>;
  
  while(stack->size()) {
    Entry e = stack->top();
    stack->pop();
    if (e.action == Entry::UNDO) {
      int f = e.v;
      while(e.v--) {
        stack->pop();
      }  
    } else {
      myStack->push(e);
    }
  }
  delete stack;
  
  std::string result;
  size_t it = 0;
  while(myStack->size()) {
    Entry e = myStack->top();
    myStack->pop();
    
    if(Entry::LEFT == e.action) {
      if(it <= e.v) {
        it = 0;
      } else {
        it -= e.v;
      }
    } else if(Entry::RIGHT == e.action) {
      const int size = result.size();
      it += e.v;
      if (it > size) {
        it = size;
      }
    } else if(Entry::TYPE == e.action) {
      
      result.insert(it, 1, e.l);
      ++it;
    } 
    //std::cout << "RESULT: " << result << std::endl;
  }
  
  std::cout << result.c_str();
  
  delete myStack;
  
  return 0;
}
