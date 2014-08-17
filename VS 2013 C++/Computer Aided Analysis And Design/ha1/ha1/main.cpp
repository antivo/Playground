#include "matrix.h"
//#include "parser.h"

#include "integrator.h"

#include <iostream>
#include <fstream>
#include <string>


int main() {
	try {

		Matrix A(2, 2, { 0, 1, -200, -102 });
		Matrix B(2, 1, { 0, 0 });
		Matrix X(2, 1, { 1, -2 });

		//const auto p = A.lupDecomposition();


		const auto p1 = integrator::rungeKutta(A, B, X, 1, 0.01);

		for (const auto& m : p1) {
			m.print(std::cout);
		}

		std::cout << std::endl << std::endl;

		const auto p2 = integrator::trapezoidMethod(A, B, X, 1, 0.1);
		for (const auto& m : p2) {
			m.print(std::cout);
		}

		/*
		const auto p1 = integrator::rungeKutta(a, b, x, 1, 0.01);
		
		for (const auto& m : p1) {
			m.print(std::cout);
		}		

		std::cout << std::endl << std::endl;

		const auto p2 = integrator::rungeKutta(a, b, x, 1, 0.01);
		for (const auto& m : p2) {
			m.print(std::cout);
		}*/
		/*Matrix m(3, 3, { 1,1,1, 1,1,3, 1,3,3 });

		//m.lupDecomposition();
		//m.transpose().print(std::cout);

		//Matrix m(3, 3, { -24, 18, 5, 20, -15, -4, -5, 4, 1 });



		(m*m.inverse()).print(std::cout);*/
	}
	catch (std::exception e) {
		std::cout << e.what();
	}


	return 0;
}