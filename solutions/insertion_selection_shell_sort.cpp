#include <iostream>

using namespace std;

template<typename T>
void swap(T& a, T& b) {
  T c(a);
  a = b;
  b = c;
}

struct IntComparator {
  bool operator()(const int &a, const int &b) const {
    return a < b;
  }
} comparator;

bool intCompare(const int &a, const int &b) {
  return a < b;
}

template<typename T>
void print(T* A, int length) {
  for(int i = 0; i < length; ++i) {
    std::cout << A[i] << " ";
  }
  std::cout << std::endl;
}

template<typename T, typename Compare>
void selectionSort(T* A, const int length, Compare comparator) {
  print(A, length);
  for(int i = 0; i < length; ++i) {
    
    T min(A[i]);
    int idx = i;
    for(int j = i; j < length; ++j) { 
      if(!comparator(min, A[j])) {
        min = A[j];
        idx = j;
      }
    }
    ::swap(A[i], A[idx]);
  
    print(A, length);

  }
}

template<typename T, typename Compare>
void insertionSort(T* A, const int length, Compare comparator) {
  print(A, length);
  for(int i = 0; i < length; ++i) {
    for(int j = i; j > 0; --j) {
      if(comparator(A[j], A[j - 1])) {
      //std::cout << A[j] << " " << A[j - 1] << std::endl;
        ::swap(A[j], A[j - 1]);
      }  
    }
    print(A, length);
  }
}

template<typename T, typename Compare>
void shellSort(T* A, const int length, Compare comparator) {
  print(A, length);
  
  int h = 1;
  while(h < length/3) h = 3*h + 1; // 1, 4, 13, 40, 121, ...
  
  while(h >= 1) { // h-sort
    for(int i = h; i < length; ++i) { // INSERTION SORT
      for(int j = i; j >= h && comparator(A[j], A[j - h]); j -= h) {
        ::swap(A[j], A[j - h]);
      }
      print(A, length);
    }
    
    h /= 3; // go to next step
  }
}

int main() {

  int A[] = {6,3,5,2,0,9,4,8,1,7,12,14,22,34,56,78,90, 96};
  int B[] = {6,3,5,2,0,9,4,8,1,7,12,14,22,34,56,78,90, 96};
  int C[] = {6,3,5,2,0,9,4,8,1,7,12,14,22,34,56,78,90, 96};
  std::cout << "SELECTION SORT: " << std::endl;
  selectionSort(A, 10, intCompare);
  std::cout << std::endl;
  std::cout << "INSERTION SORT: " << std::endl;
  insertionSort(B, 10, intCompare);
  std::cout << std::endl;
  std::cout << "SHELL SORT: " << std::endl;
  shellSort(C, 10, intCompare);
  
  
  return 0;
}

