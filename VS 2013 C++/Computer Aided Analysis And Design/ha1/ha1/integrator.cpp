#include "integrator.h"
#include "matrix.h"

#include <vector>

std::vector<const Matrix> integrator::rungeKutta(const Matrix& a, const Matrix& b, const Matrix& x0, double interval, double step) {
	static const auto precision = 10;

	Matrix x = x0;
	const int k = interval / step;
	std::vector<const Matrix> points;
	for (auto i = 0; i <= k; ++i) {
		Matrix m1 = (a * x) + b;
		Matrix m2 = a * (x + (m1*(step / 2))) + b;
		Matrix m3 = a * (x + (m2*(step / 2))) + b;
		Matrix m4 = a * (x + (m3*step)) + b;
		x = x + (m1 + (m2 * 2) + (m3 * 2) + m4) * (step / 6);

		if ((0 == i % precision) || (k == i)) {
			points.push_back(x);
		}
	}

	return points;
}


std::vector<const Matrix> integrator::trapezoidMethod(const Matrix& a, const Matrix& b, const Matrix& x0, double interval, double step) {
	static const auto precision = 10;
	
	Matrix x = x0;
	Matrix u = Matrix::getIdentityMatrix(x0.getRows());
	std::vector<const Matrix> points;
	const int k = interval / step;
	for (auto i = 0; i < k; ++i) {
		Matrix r1 = u - (a * (step / 2));
		Matrix _r1 = r1.inverse(); 
		Matrix r2 = u + (a * (step / 2));
		Matrix r = _r1 + r2;

		Matrix s1 = u - (a * (step / 2));
		Matrix _s1 = s1.inverse();
		Matrix s2 = b * (step / 2);
		Matrix s = s1 + s2;

		x = (r*x) + s;
		if ((0 == i % precision) || (k == i)) {
			points.push_back(x);
		}
	}

	return points;
}