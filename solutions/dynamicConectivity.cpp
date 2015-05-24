#include <iostream>
#include <vector>
#include <memory>

using namespace std;

class UF {
public:
  virtual bool connected(int p, int q) const { return false; }
  virtual void unite(int p, int q) {}
  virtual void print() const {};
};

class QuickFind : public UF {
private:
  std::vector<int> id;  
public:
  QuickFind(const int n) : id(n) {
    for(int i = 0; i < n; ++i) {
      id.at(i) = i;
    }
  }
  
  bool connected(int p, int q) const {
    return id[p] == id[q];
  }
  
  void unite(int p, int q) {
    const int group = id[p];
    for(int i = 0; i < id.size(); ++i) {
      if(id[i] == group) {
        id[i] = id[q];
      }
    }
  }
  
  void print() const {
    std::cout << endl;
    for(int i = 0; i < id.size(); ++i) {
      std::cout << id[i] << " ";
    }
    std::cout << endl;
  }
};

class QuickUnion : public UF {
private:
  std::vector<int> id;  

  int root(int p) const {
    while(id[p] != p) {
      p = id[p];
    }
    return p;
  }

public:
  
  QuickUnion(const int n) : id(n) {
    for(int i = 0; i < n; ++i) {
      id.at(i) = i;
    }
  }
  
  bool connected(int p, int q) const {
    return root(p) == root(q);
  }
  
  void unite(int p, int q) {
    const int rootOfP = root(p);
    const int rootOfQ = root(q);
    id[rootOfP] = rootOfQ;
  }
  
  void print() const {
    std::cout << endl;
    for(int i = 0; i < id.size(); ++i) {
      std::cout << id[i] << " ";
    }
    std::cout << endl;
  }
};

class WeightedQuickUnion : public UF {
private:
  std::vector<int> id;
  std::vector<int> sz;
  
  int root(int p) const {
    while(id[p] != p) {
      p = id[p];
    }
    return p;
  }

public:
  
  WeightedQuickUnion(const int n) : id(n), sz(n) {
    for(int i = 0; i < n; ++i) {
      id.at(i) = i;
      sz.at(i) = 1;
    }
  }
  
  bool connected(int p, int q) const {
    return root(p) == root(q);
  }
  
  void unite(int p, int q) {
    const int rootOfP = root(p);
    const int rootOfQ = root(q);
    if(rootOfP == rootOfQ) return;
    
    if(sz[rootOfP] < sz[rootOfQ]) {
      id[rootOfP] = rootOfQ;
      sz[rootOfQ] += sz[rootOfP];
    } else {
      id[rootOfQ] = rootOfP;
      sz[rootOfP] += sz[rootOfQ];
    }
    
  }
  
  void print() const {
    std::cout << endl;
    for(int i = 0; i < id.size(); ++i) {
      std::cout << id[i] << " ";
    }
    std::cout << endl;
    for(int i = 0; i < sz.size(); ++i) {
      std::cout << sz[i] << " ";
    }
    std::cout << endl;
  }
  
};


int main() {
  UF* uf = new WeightedQuickUnion(10);

  int x = 0, y = 0;
  while(x >= 0 || y >= 0) {
    std::cin >> x;
    std::cin >> y;
  
    
  
    if(uf->connected(x,y)) {
      std::cout << x << " " << y << std::endl;
    } else {
      uf->unite(x, y);
    }
    uf->print();
  }

  

  delete uf;
  return 0;
}

