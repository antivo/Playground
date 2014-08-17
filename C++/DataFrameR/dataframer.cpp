#include "dataframer.h"
#include <algorithm>

int prod::operator()(std::vector<int> input) {
  int acc = 1;
  for(int i = 0; i < input.size(); ++i) {
    acc *= input[i];
  }
  return acc;
}

wprod::wprod(double div) {
  this->div = div;
}

double wprod::operator()(std::vector<int> input) {
  prod p;
  return p(input)/div;
}

DataFrameR::DataFrameR() {
  brojRedaka = 0;
  brojStupaca = 0;
}

DataFrameRIterator::DataFrameRIterator(DataFrameR* domain, unsigned long brojRedaka, unsigned long index) {
  this->domain = domain;
  this->brojRedaka = brojRedaka;
  this->index = index;
}

DataFrameR DataFrameRIterator::operator*() {
  return this->domain->getRedak(this->index);
 
}

void DataFrameRIterator::operator++() {
  ++this->index;
}

void DataFrameRIterator::operator--() {
  --this->index;
}

bool DataFrameRIterator::operator==(const DataFrameRIterator& rhs) {
  return this->index == rhs.index;
}

bool DataFrameRIterator::operator!=(const DataFrameRIterator& rhs) {
  return this->index != rhs.index;
}
 
std::istream& operator>> (std::istream& in, DataFrameR& dataframer) {
  in >> dataframer.brojRedaka >> dataframer.brojStupaca;
  std::string imeRedka;
  for(int i = 0; i < dataframer.brojRedaka; ++i) {
    in >> imeRedka;
    dataframer.imena.push_back(imeRedka);
  }
  int vrijednostElementa;
  for(int i = 0; i < dataframer.brojRedaka; ++i) {
    std::vector<int> redak;
    for(int j = 0; j < dataframer.brojStupaca; ++j) {
      in >> vrijednostElementa;
      redak.push_back(vrijednostElementa);
    }
    dataframer.vrijednosti.push_back(redak);
  }
  return in;
}

std::ostream& operator<<(std::ostream& out, const DataFrameR& dataframer) {
  for(int i = 0; i < dataframer.brojRedaka; ++i) {
    out << dataframer.imena[i];
    for(int j = 0; j < dataframer.brojStupaca; ++j) {
      out << "\t" << dataframer.vrijednosti[i][j];
    }
    out << std::endl;
  }
  return out;
}

bool operator==(const DataFrameR& dfrLeft, const DataFrameR& dfrRight) {
  if((dfrLeft.brojRedaka != dfrRight.brojRedaka) || (dfrLeft.brojStupaca != dfrRight.brojStupaca)) {
    return false;
  }
  for(int i = 0; i < dfrLeft.brojRedaka; ++i) {
    for(int j = 0; j < dfrLeft.brojStupaca; ++j) {
      if(dfrLeft.vrijednosti[i][j] != dfrRight.vrijednosti[i][j]) {
        return false;
      }
    }
  }
  return true; 
}

DataFrameR operator+(DataFrameR& dfrLeft, DataFrameR& dfrRight) {
  DataFrameR wtf;
  if(dfrLeft.brojStupaca == dfrRight.brojStupaca) {
    for(int i = 0; (i < dfrLeft.brojRedaka) && (i < dfrRight.brojRedaka); ++i) {
      bool isti = true;
      if(dfrLeft.imena[i] == dfrRight.imena[i]) {
        for(int j = 0; j < dfrLeft.brojStupaca; ++j) {
          if(dfrLeft(i,j) != dfrRight(i,j)) {
            isti = false;
            break;
          }
        }
      } else {
        isti = false;

      }
      if(isti) {
        ++wtf.brojRedaka;
        wtf.brojStupaca = dfrLeft.brojStupaca * 2;
        wtf.imena.push_back(dfrLeft.imena[i]);
        std::vector<int> redak;
        for(int dvaput = 0; dvaput < 2; ++dvaput) {
          for(int k = 0; k < dfrLeft.brojStupaca; ++k) {
            redak.push_back(dfrLeft.vrijednosti[i][k]);
          }
        }
        wtf.vrijednosti.push_back(redak);
      }
    }
  }
  
  return wtf;
}

std::vector<std::string>& operator~(DataFrameR &dataframer) {
  return dataframer.imena;
}

int DataFrameR::operator()(int i, int j) {
  if((i < 0) || (j < 0) || (i > this->brojRedaka) || (j > this->brojStupaca)) {
    return 0;
  }
  return this->vrijednosti[i][j];
}

DataFrameR DataFrameR::operator[](const std::string& ime) {
  int index = -1;
  for(int i = 0; i < imena.size(); ++i) {
    if (imena[i] == ime) {
      index = i;
      break;
    }
  }
  
  DataFrameR rez;
  if (-1 != index) {
    rez.brojRedaka = 1;
    rez.brojStupaca = this->brojStupaca;
    
    rez.imena.push_back(ime);
    
    std::vector<int> redak;
    for(int i = 0; i < rez.brojStupaca; ++i) {
      redak.push_back(this->vrijednosti[index][i]);
    }
    rez.vrijednosti.push_back(redak);
  }
  
  return rez;
}

DataFrameR DataFrameR::operator[](const std::vector<std::string>& sekvencaImena) {
  std::vector<int> indexes;
  for(int i = 0; i < sekvencaImena.size(); ++i) {
    std::string ime = sekvencaImena[i];
    for(int i = 0; i < imena.size(); ++i) {
      if (imena[i] == ime) {
        indexes.push_back(i);
        break;
      }
    }
  }
  
  int broj_rezultata = indexes.size();
  DataFrameR rez;
  if (broj_rezultata) {
    rez.brojRedaka = broj_rezultata;
    rez.brojStupaca = this->brojStupaca;
    
    for(int i = 0; i < broj_rezultata; ++i) {
      rez.imena.push_back(this->imena[indexes[i]]);
      std::vector<int> redak;
      int index = indexes[i];
      for(int j = 0; j < rez.brojStupaca; ++j) {
        redak.push_back(this->vrijednosti[index][j]);
      }
      rez.vrijednosti.push_back(redak);
    }
  }
  return rez;
}

DataFrameR::iterator DataFrameR::begin() {
  return iterator(this, this->brojRedaka, 0);
  
}

DataFrameR::iterator DataFrameR::end() {
  return iterator(this, this->brojRedaka, this->brojRedaka);
}

DataFrameR DataFrameR::getRedak(unsigned long index) {
  DataFrameR rez;
  
  rez.brojRedaka = 1;
  rez.brojStupaca = this->brojStupaca;
    
  rez.imena.push_back(this->imena[index]);
    
  std::vector<int> redak;
  for(int i = 0; i < rez.brojStupaca; ++i) {
    redak.push_back(this->vrijednosti[index][i]);
  }
  rez.vrijednosti.push_back(redak);
  return rez;
}
