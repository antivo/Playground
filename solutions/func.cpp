#include <cstring>
#include <set>
#include <iostream>
#include <string>
#include <algorithm>
#include <cstdlib>
#include <sstream>
#include <unordered_set>
#include <utility>



bool allUnique(const std::string& ss) {
  static const int ASCII = 256;
  char count[ASCII];
  
  bool result = true;
  for(int i = 0; i < ss.size(); ++i) {
    char c = ss.at(i);
    int n = ++count[c];
    if(n > 1) {
      result = false;
      break;
    } 
  }
  
  if(result) {
    for(int i = 0; i < ASCII; ++i) {
      if(!count[i]) {
        result = false;
        break;
      }
    }
  }
  
  return result;
}

void reverse(char* str) {
  if(NULL == str) {
    char* end = str;
    while(*end++) { } 
    
    while(str < end) {
      char temp = *str;
      *str = *end;
      *end = temp;

      ++str;
      --end;
    }
  }
}

bool isPermutation(const std::string& s1, const std::string& s2) {
  static const int ASCII = 256;
  
  bool result = true;
  if(s1.size() != s2.size()) {
    char count[ASCII];   
    
    for(int i = 0, c; i < s1.size(); ++i) {
      c = s1.at(i);
      ++count[c];
      
      c = s2.at(i);
      --count[c];
    }
    
    for(int i = 0; i < ASCII; ++i) {
      if(!count[i]) {
        result = false;
        break;
      }
    }
  } else {
    result = false;
  }
   
  return result;
}

bool isPermutationSort(std::string& s1, std::string& s2) {
  std::sort(s1.begin(), s1.end());
  std::sort(s2.begin(), s2.end());
  
  return !s1.compare(s2);
}

char* replaceSpace(char* ss, int n) {
  int length = n;
  for(int i = 0; i < n; ++i) {
    if(' ' == ss[i]) {
      length += 2;
    }
  }
  
  ss = (char*) realloc(ss, sizeof(char) * (length + 1));
  ss[length] = '\n';
 
  while(--n >= 0) {
    if(' ' == ss[n]) {
      ss[--length] = '0';
      ss[--length] = '2';
      ss[--length] = '%';      
    } else {
      ss[--length] = ss[n]; 
    }
  }
}

int compressedLength(const std::string& ss) {
  int less = 0;
  char prev = ss.at(0);
  int length = ss.size();
  bool sequenced = false;
  for(int i = 1; i < length; ++i) {
    const char c = ss.at(i);
    if(c == prev) {
      if(sequenced) {
        ++less;
      } else {
        sequenced = true;
      }
    } else {
      prev = c;
      sequenced = false;
    }
  }
  return length - less;
}

std::string compress(const std::string& ss) {
  if(ss.size() > compressedLength(ss)) {
    std::stringstream sstream;
    
    char prev = ss.at(0);
    for(int i = 1, count = 0; i < ss.size(); ++i) {
      const char c = ss.at(i);
      
      if(c == prev) {
        ++count;
      } else {
        sstream << prev;
        if(count) {
          sstream << count;
        }
        
        count = 0;
        prev = c;
      }
      
    }
    return sstream.str();
  } else {
    return ss;
  }
}

int** rotate(int** m, int n) {
  for(int layer = 0; layer < n/2; ++ layer) {
    int first = layer;
    int last = n - 1 - layer;
    
    for(int i = first; i < last; ++i) {
      const int offset = i - first;
      
      const int top = m[first][i];
      
      //left->top
      m[first][i] = m[last - offset][first];
      //down->left
      m[last - offset][first] = m[last][last - offset];
      // right->bottom
      m[last][last - offset] = m[i][last];
      //top->right
      m[i][last] = top;
    } 
  }
}

void setToZero(int** m, int n) {
  bool* rows = new bool[n];
  bool* colls = new bool[n];
  for(int i = 0; i < n; ++i) {
    rows[i] = false;
    colls[i] = false;
  }
  
  for(int i = 0; i < n; ++i) {
    for(int j = 0; j < n; ++j) {
      if(0 == m[i][j]) {
        rows[i] = true;
        colls[j] = true;
      }
    }
  }
  
  for(int i = 0; i < n; ++i) {
    for(int j = 0; j < n; ++j) {
      if(rows[i] || colls[j]) {
        m[i][j] = 0;
      }
    }
  }
  
  
  delete rows;
  delete colls;
}

struct LinkedListNode {
  int v;
  mutable LinkedListNode* next;
};

void deleteDuplicatesFromLinkedList(const LinkedListNode* list) {
  if(list) {
    std::unordered_set<int> set;
    const LinkedListNode* previous = NULL;
    while(list) {
      const int v = list->v;
      if(set.count(v)) {
        previous->next = list->next;
      } else {
        set.emplace(v);
        previous = list;
      }
      
      list = list->next;
    }
  }
}

void deleteDuplicatesFromLinkedListWithoutBuffer(LinkedListNode* list) {
  LinkedListNode* node = list;
  while(node) {
    LinkedListNode* next = node;
    while(next->next) {
      if(node->v == next->next->v) {
        LinkedListNode* f = next->next->next;
        delete next->next;
        next->next = f;
      } else {
        next = next->next;
      }
    }
  
    node = node->next;    
  }
}

LinkedListNode* getKthToLast(LinkedListNode* root, const int k, int& acc = 0) {
  if(root) {
    LinkedListNode* node = getKthToLast(root->next, k, acc);
    acc += 1;
    
    if(acc == k) {
      return root;
    }

    return node;
  } else {
    return -1;
  }
}

void deleteThis(LinkedListNode* node) {
  if(node && node->next) {
    node->v = node->next.v;
    node->next = node->next->next;
    delete node->next;
  }
}





bool nextPermuation(std::string& ss) {
  int i;   
  for(i = ss.size() - 2; i >= 0; --i) {
    if(ss.at(i) < ss.at(i + 1)) {
      break;
    }
  }
  
  if( -1 == i ) return false;
  
  int mini = i + 1;
  for(int j = mini + 1; j < ss.size(); ++j) {
    if(ss.at(j) < ss.at(mini) && ss.at(j) > ss.at(i)) {
      mini = j;
    }
  }
  
  std::swap(ss[i], ss[mini]);
  //ss[i] ^= ss[mini]; ss[mini] ^= ss[i]; ss[i] ^= ss[mini];
  
  for(i++; i < ss.size(); ++i) {
    for(int j = i + 1; j < ss.size();  ++j) {
      if(ss[j] < ss[i]) {
        std::swap(ss[i], ss[j]);
        //ss[i] ^= ss[j]; ss[j] ^= ss[i]; ss[i] ^= ss[j];
      }
    }
  }
  
  return true;
  
}























int matrixAt(const int** m, const int n, const int i, const int j) {
  int result = 0;
  if(i >= 0 && j >= 0 && i < n && j < n) {
    result = m[i][j];
  }
  return result;
}

int calculateFenceMaterials(const int** m, const int n, const int i, const int j) {
  return 4 - matrixAt(m, n, i, j - 1) - matrixAt(m, n, i, j + 1) - matrixAt(m, n, i - 1, j) - matrixAt(m, n, i + 1, j);
}

int calculateWrapper(const int** m, const int n, const std::pair<int,int>& coord, std::set<std::pair<int,int>> & forbiden);

int calculateFence(const int** m, const int n, const int i, const int j, std::set<std::pair<int,int>>& forbiden) {
  int result = 0;
  if(i >= 0 && j >= 0 && i < n && j < n) {
    if(m[i][j]) {
      result = calculateFenceMaterials(m,n,i,j);
      forbiden.insert(std::make_pair(i,j));
      
      std::pair<int, int> coord = std::make_pair(i - 1, j);
      result = calculateWrapper(m,n,coord,forbiden);
      
      coord = std::make_pair(i + 1, j);
      result = calculateWrapper(m,n,coord,forbiden);
      
      coord = std::make_pair(i, j - 1);
      result = calculateWrapper(m,n,coord,forbiden);
      
      coord = std::make_pair(i, j + 1);
      result = calculateWrapper(m,n,coord,forbiden);
    }
  }
  return result;
  
}

int calculateWrapper(const int** m, const int n, const std::pair<int,int>& coord, std::set<std::pair<int,int>> & forbiden) {
  int result = 0;
  if(!forbiden.count(coord)) {
    result += calculateFence(m, n, coord.first, coord.second, forbiden);
    forbiden.insert(coord);
  }
  return result;
}




bool nextP(std::string& ss) {
  int i;
  for(i = ss.size() - 2; i >= 0; --i) {
    if(ss.at(i) < ss.at(i+1)) {
      break;
    }
  }
  
  bool result = false;
  if(-1 != i) { 
    int mini = i + 1;
    for(int j = mini +1; j < ss.size(); ++j) {
      if(ss.at(j) < ss.at(mini) && ss.at(i) > ss.at(j)) {
        mini = j;
      }
    }
    
    std::swap(ss[i], ss[mini]);
    
    for(++i; i < ss.size() - 1; ++i) {
      for(int j = i + 1; j < ss.size(); ++j) {
        if(ss.at(j) < ss.at(i)) {
          std::swap(ss[i], ss[j]);
        }
      } 
    }
    
    result = true;
  }
  return result;
    
}

 

int main() {
  std::string ss("(())");
  while(nextP(ss))
    std::cout << ss << std::endl;

  return 0;   
}
