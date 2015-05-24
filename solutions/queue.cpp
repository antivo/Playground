#include <iostream>
#include <string>

using namespace std;

template<typename T>
class Queue {
public:
  virtual void enqueue(T item) = 0;
  virtual T dequeue() = 0;
  virtual bool isEmpty() const = 0;
  virtual int size() const = 0;
  virtual void print() const = 0;

  virtual ~Queue() {}
};

template<typename T>
class QueueLinked : public Queue<T> {
public:
  
  QueueLinked() : count(0), first(NULL), last(NULL) {
  }
  
  void enqueue(T item) {
    Node* node = new Node;
    node->item = item;
    node->next = NULL;
    
    if(last) {
      last->next = node;
    }
    
    if(!first) {
      first = node; 
    } 
    
    last = node;
    ++count;
  }
  
  T dequeue() {
    T value;
    if(first) {
      value = first->item;
      if(first == last) {
        delete first;
        first = last = NULL;
      } else {
        Node* next = first->next;
        delete first;
        first = next;
      }
      --count;
    }
    return value;
  }
  
  bool isEmpty() const {
    return first == NULL;
  }
  
  int size() const {
    return count;
  }
  
  void print() const {
    std::cout << "QUEUE: " << std::endl;
    Node* iterator = first;
    while(iterator) {
      std::cout << iterator->item << std::endl;
      iterator = iterator->next;
    }  
    std::cout << std::endl;
  }

  ~QueueLinked() {
    while(first) {
      Node* next = first->next;
      delete first;
      first = next;
    }  
  }
  
private:
  struct Node {
    T item;
    Node* next;
  };
  
  Node* first;
  Node* last;
  int count;
};


template<typename T>
class QueueArray : public Queue<T> {
public:
  QueueArray() : array(new T[1]), count(0), length(1) {  
  }

  void enqueue(T item) {
    if(isFull()) {
      resizeUp();
      std::cout << "resizing" << std::endl;
    }
    
    array[count++] = item;
  }
  
  T dequeue() {
    T value;
    if(count) {
      value = array[count--];
      
      if(isBelowQuarter()) {
        resizeDown();
      }     
    }
  }
  
  bool isEmpty() const {
    return count == 0;
  }
  
  int size() const {
    return count;
  }
  
  void print() const {
    std::cout << "QUEUE: " << std::endl;
    for(int i = 0; i < count; ++i) {
      std::cout << array[i] << std::endl;
    }
    std::cout << "COUNT: " << count << std::endl;
    std::cout << "LENGTH: " << length << std::endl;
    std::cout << std::endl;
  }

  ~QueueArray() {
    delete [] array;
  }

private:
  T* array;
  int count;
  int length;
  
  void resize(double k) {
    int newLength = k * length;
    T* newArray = new T[newLength];
    for(int i = 0; i < count; ++i) {
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
  
  bool isBelowQuarter() {
    return (count * 4)  < length;
  }
  
  bool isFull() {
    return count == length;
  }
};

int main() {
   Queue<int>* queue = new QueueArray<int>();
   while(true) {
    std::string decision;
    std::cin >> decision;

    int item;    
       
    const std::string pop("d");
    const std::string push("e");
    if(!decision.compare(pop)) {
      std::cout << "DEQUEUED : " << queue->dequeue() << std::endl; 
    }
    
    if(!decision.compare(push)) {
      std::cin >> item;
      std::cout << "ENQUEUED : " << item << std::endl;
      queue->enqueue(item);
    }
   
   queue->print();
  }
  return 0;
}

