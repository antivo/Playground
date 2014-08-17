#include <iostream>
#include <vector>
#include <string>

struct prod {
public:
  int operator()(std::vector<int> input);
};

struct wprod {
public:
  wprod(double div);
  double operator()(std::vector<int> input);
  
private:
  double div;
};

class DataFrameR;

std::istream& operator>>(std::istream& in, DataFrameR& dataframer);
std::ostream& operator<<(std::ostream& out, const DataFrameR& dataframer);
bool operator==(const DataFrameR& dfrLeft, const DataFrameR& dfrRight);
DataFrameR operator+(const DataFrameR& dfrLeft, const DataFrameR& dfrRight);
std::vector<std::string>& operator~(DataFrameR& dataframer);

class DataFrameRIterator {
  friend DataFrameR;
public:
  DataFrameR operator*();
  void operator++(); 
  void operator--(); 
  bool operator==(const DataFrameRIterator& rhs);
  bool operator!=(const DataFrameRIterator& rhs);
private:
  DataFrameRIterator(DataFrameR* domain, unsigned long brojRedaka, unsigned long index);

  DataFrameR* domain;
  unsigned long brojRedaka;
  unsigned long index;
};

class DataFrameR {
  friend std::istream& operator>>(std::istream& in, DataFrameR& dataframer);
  friend std::ostream& operator<<(std::ostream& out, const DataFrameR& dataframer);
  friend bool operator==(const DataFrameR& dfrLeft, const DataFrameR& dfrRight);
  friend DataFrameR operator+(DataFrameR& dfrLeft, DataFrameR& dfrRight);
  friend std::vector<std::string>& operator~(DataFrameR& dataframer);
  template<class Functor>
  friend DataFrameR operator%(DataFrameR& dfr, Functor f); 
  
public:
  DataFrameR();
  
  int operator()(int i, int j);
  DataFrameR operator[](const std::string& ime);
  DataFrameR operator[](const std::vector<std::string>& sekvencaImena);
  
  
  typedef DataFrameRIterator iterator;
  
  iterator begin();
  iterator end();
  
  DataFrameR getRedak(unsigned long i);
  
  template <class Functor, class Rez>
  void apply(Functor f, Rez& rez) {
    for(int i = 0; i < this->brojRedaka; ++i) {
      double acc = f(this->vrijednosti[i]);
      rez.push_back(acc);
    }
  }
  
 
private:
  unsigned long brojRedaka;
  unsigned long brojStupaca;

  std::vector<std::vector<int>> vrijednosti; 
  std::vector<std::string> imena;
  
};

template<class Functor>
DataFrameR operator%(DataFrameR& dfr, Functor f) {
  DataFrameR rez;
  rez.brojRedaka = dfr.brojRedaka;
  rez.brojStupaca = 1;
  for(int i = 0; i < dfr.brojRedaka; ++i) {
    rez.imena.push_back(dfr.imena[i]);
      
    int acc = (int) f(dfr.vrijednosti[i]);
    std::vector<int> redak;
    redak.push_back(acc);
    rez.vrijednosti.push_back(std::vector<int>(redak));
  }
  return rez;
}