#include "dataframer.h"
#include <iostream>
#include <vector>
#include <list>
#include <utility>

using namespace std;

int main()
{

        DataFrameR D,E;
        cin >> D >> E;

        cout << D;
        cout << "----" << endl;
        cout << E;
        cout << "----" << endl;

        cout << D[string("B")];
        cout << "----" << endl;

        vector<string> z;
        z.push_back("B");
        z.push_back("A");
        cout << D[z];
        cout << "----" << endl;

        cout << D(1,2) << endl;
        cout << "----" << endl;


        vector<string> imena = ~D;
        for(vector<string>::iterator i=imena.begin(); i!=imena.end(); ++i )
                cout << *i << endl;
        cout << "----" << endl;

        for(DataFrameR::iterator i=D.begin(); i!=D.end(); ++i)
                cout << *i;
        cout << "----" << endl;
        
        vector<int> rez;
        D.apply(wprod(1), rez);
        for(vector<int>::iterator i=rez.begin(); i!=rez.end(); ++i)
                cout << *i << endl;
        cout << "----" << endl;

        list<double> rez2;
        D.apply(wprod(2), rez2);
        for(list<double>::iterator i=rez2.begin(); i!=rez2.end(); ++i)
                cout << *i << endl;
        cout << "----" << endl;
        
        cout << D+E;
        cout << "----" << endl;
        
        vector<string> Z; 
        Z.push_back("X");
        Z.push_back("Y");
        Z.push_back("Z");
        Z.push_back("W");
        ~D = Z;
        cout << D;
        cout << "----" << endl;

        prod S;
        cout << D%S;
        cout << "----" << endl;
  return 0;
}