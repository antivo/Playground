#include <iostream>
#include <string>

using namespace std;

template<typename T>
class Stack {
public:
  virtual void push(T item) = 0;
  virtual T pop() = 0;
  virtual bool isEmpty() const = 0;
  virtual int size() const = 0;
  virtual void print() const = 0;

  virtual ~Stack() {}
};

template<typename T>
class StackLinked : public Stack<T> {
public:
  
  StackLinked() : first(NULL), count(0) {
  }
  
  void print() const {
    std::cout << "STACK : " << std::endl;
    Node* iterate = first; 
    while(iterate) {
      std::cout << iterate->item << std::endl;
      iterate = iterate->next;
    }  
    
    std::cout << std::endl;
  }
  
  void push(T item) {
    Node* old = this->first;
 
    first = new Node();
    first->item = item;
    first->next = old;
    
    ++count;
  }
  
  T pop() {
    T value;
    if(first) {
    
      Node* next = first->next;
      
      value = first->item;
      delete first;
      first = next;
      
      --count;
    }
    return value;
  }
  
  bool isEmpty() const {
    return first == NULL;
  }
  
  int size() const {
    return this->count;
  }
  
  ~StackLinked() {
    std::cout << "~StackLinked()" << std::endl;
    
    while(first) {
      Node* next = first->next;
      
      std::cout << "DELETED item: " << first->item << std::endl;
      delete first;
      
      first = next;
    }
    
    std::cout << std::endl;
  }
  
private:
  struct Node {
    T item;
    Node* next;
  };
  
  Node* first;
  int count;
};

template<typename T>
class StackArray : public Stack<T> {
public:
  StackArray(): array(new T[1]), count(0), length(1) {
    
  }

  void push(T item) {
      if(this->isFull()) {
        this->resizeUp();
      }
      this->array[count++] = item;
  }

  T pop() {
    T value;
    
    if(count > 0) {
      value = this->array[count--];
      
      std::cout << value << std::endl;
      
      if(this->isBelowQuarter()) {
        this->resizeDown();
      }
    }
    
    return value;
  }

  bool isEmpty() const {
    return false;
  }

  int size() const {
    return this->count;
  }

  void print() const {
    std::cout << "STACK: " << std::endl;
    for(int i = count; i > 0; --i) {
      std::cout << this->array[i - 1] << std::endl;
    }
    std::cout << "SIZE: " << this->size() << std::endl;
    std::cout << "CAPACITY: " << this->length << std::endl;
    std::cout << std::endl;
  }

  ~StackArray() {
    delete [] array;
  }

private:
  T* array;
  int count;
  int length;
  
  void resize(float k) {
    const int newLength = length * k;
    T* newArray = new T[newLength];
    for(int i = 0 ; i < count; ++i) {
      newArray[i] = array[i];
    }
    
    delete [] array;
    array = newArray;
    
    length = newLength;
  }
  
  void resizeUp() {
    resize(2);
  }
  
  void resizeDown() {
    resize(0.5);
  }
  
  bool isBelowQuarter() const {
    return (4 * count) < length;
  }
  
  bool isFull() const {
    return count == length;
  }
};


double infixCalculator(const std::string& expression) {
  double result = 0;
  
  Stack<double>* valueStack = new StackArray<double>();
  Stack<char>* operatorStack = new StackLinked<char>();
  
  for(int i = 0; i < expression.size(); ++i) {
    
  }
  
  delete valueStack;
  
  return result;
}


int main() {
  Stack<int>* stack = new StackArray<int>();
  stack->isEmpty();
  while(true) {
    std::string decision;
    std::cin >> decision;

    int item;    
       
    const std::string pop("pop");
    const std::string push("push");
    if(!decision.compare(pop)) {
      std::cout << "POPPED : " << stack->pop() << std::endl; 
    }
    
    if(!decision.compare(push)) {
      std::cin >> item;
      std::cout << "PUSHED : " << item << std::endl;
      stack->push(item);
    }
   
   stack->print();
  }

  delete stack;
  return 0;
}

