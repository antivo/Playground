#ifndef INTEGRATOR
#define INTEGRATOR

#include <vector>

class Matrix;

namespace integrator {
	std::vector<const Matrix> rungeKutta(const Matrix& a, const Matrix& b, const Matrix& x0, double interval, double step);
	std::vector<const Matrix> trapezoidMethod(const Matrix& a, const Matrix& b, const Matrix& x0, double interval, double step);
}
#endif