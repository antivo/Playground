#include "matrix.h"

#include <string>
#include <cmath>

#include <algorithm>


Matrix::Matrix(const int rows, const int colls, const std::vector<double>& data):
rows(rows), colls(colls), data(data) {
	assertMatrix();
}

Matrix::Matrix(const int rows, const int colls, std::vector<double>&& data) :
rows(rows), colls(colls), data(data) {
	assertMatrix();
}

void Matrix::assertMatrix() const {
	if (rows < 1 || colls < 1 || data.size() != rows * colls) {
		throw std::exception("Badly formed matrix.");
	}
}

void Matrix::assertSqueareDimension() const {
	if (rows != colls) {
		throw std::exception("Given matrix is not square matrix.");
	}
}

void Matrix::assertEqualDimension(const Matrix& rhs) const {
	if (this->rows != rhs.rows || this->colls != rhs.colls) {
		throw std::exception("Matrices can not be used in addition.");
	}
}

void Matrix::assertMultiplication(const Matrix& rhs) const {
	if (this->colls != rhs.rows) {
		throw std::exception("Matrices can not be used in multiplication.");
	}
}

void Matrix::assertIsRowVector(const Matrix& v) const {
	if (1 != v.colls) {
		throw std::exception("Provided matrix is not vector");
	}

	if (this->colls != v.rows) {
		throw std::exception("Provided vector does not match as the possible row of given matrix");
	}
}

void Matrix::assertRowExists(const int i) const {
	if (i < 0 || i >= rows) {
		throw std::exception("Assigned row does not exist in a given Matrix");
	}
}

void Matrix::assertAccess(const int i, const int j) const {
	if (i >= rows || j >= colls) {
		std::exception("Index out of bounds.");
	}
}



bool Matrix::operator==(const Matrix& rhs) const {
	assertEqualDimension(rhs);
	auto size = this->data.size();
	const double e = 1.0E-9;
	double distance;
	for (decltype(size) i = 0; i < size; ++i) {
		distance = this->data[i] - rhs.data[i];
		if (abs(distance) > e) {
			return false;
		}
	}
	return true;
}

Matrix Matrix::operator+(const Matrix& rhs) const {
	assertEqualDimension(rhs);
	Matrix m(*this);
	m += rhs;
	return m;
	/*auto size = this->data.size();
	std::vector<double> newData(size);
	
	for (decltype(size) i = 0; i < size; ++i) {
		newData[i] = this->data[i] + rhs.data[i];
	}

	return Matrix(this->rows, this->colls, std::move(newData));*/
}

void Matrix::operator+=(const Matrix& rhs) {
	assertEqualDimension(rhs);
	auto size = this->data.size();
	for (decltype(size) i = 0; i < size; ++i) {
		this->data[i] += rhs.data[i];
	}
}

Matrix Matrix::operator-(const Matrix& rhs) const {
	const auto matrix = rhs * (-1);
	return operator+(matrix);
}

void Matrix::operator-=(const Matrix& rhs) {
	const auto matrix = rhs * (-1);
	operator+=(matrix);
}

Matrix Matrix::operator*(const double rhs) const {
	auto size = this->data.size();
	std::vector<double> newData(size);
	for (decltype(size) i = 0; i < size; ++i) {
		newData[i] = this->data[i] * rhs;
	}

	return Matrix(this->rows, this->colls, std::move(newData));
}

void Matrix::operator*=(const double rhs) {
	auto size = this->data.size();
	for (decltype(size) i = 0; i < size; ++i) {
		this->data[i] *= rhs;
	}
}

Matrix Matrix::operator*(const Matrix& rhs) const {
	assertMultiplication(rhs);
	auto size = this->rows * rhs.colls;
	Matrix matrix(this->rows, rhs.colls, std::move(std::vector<double>(size)));
	for (decltype(rows) i = 0; i < this->rows; ++i) {
		for (decltype(colls) j = 0; j < rhs.colls; ++j) {
			matrix.at(i, j) = 0;
			for (decltype(colls) k = 0; k < this->colls; ++k) {
				
				matrix.at(i, j) += this->at(i, k) * rhs.at(k, j);
			}
		}
	}

	return matrix;
}

void Matrix::operator*=(const Matrix& rhs) {
	assertMultiplication(rhs);
	this->data.swap(operator*(rhs).data);
	this->colls = rhs.colls;
}

Matrix Matrix::transpose() const {
	auto size = this->data.size();
	Matrix matrix(this->colls, this->rows, std::vector<double>(size));
	for (decltype(rows) i = 0; i < rows; ++i) {
		for (decltype(colls) j = 0; j < colls; ++j) {
			matrix.at(j, i) = this->at(i, j);
		}
	}
	return matrix;
}

double& Matrix::at(const int i, const int j) {
	return const_cast<double&>(static_cast<const Matrix*>(this)->at(i, j));
}

const double& Matrix::at(const int i, const int j) const {
	assertAccess(i, j);
	return this->data.at(i*colls + j);
}

int Matrix::getRows() const {
	return rows;
}

int Matrix::getColls() const {
	return colls;
}

void Matrix::print(std::ostream& output) const {
	const std::string separator(" ");
	const std::string newLine("\n");
	
	for (decltype(rows) i = 0; i < rows; ++i) {
		auto first = true;
		for (decltype(colls) j = 0; j < colls; ++j) {
			if (first) {
				first = false;
			} else {
				output << separator;
			}
			output << this->at(i, j);
		}
		output << newLine;
	}
}

Matrix Matrix::luDecomposition() const {
	assertSqueareDimension();
	Matrix matrix(*this);
	const auto n = this->rows;
	for (decltype(this->rows) i = 0; i < n - 1; ++i) {
		for (decltype(i) j = i + 1; j < n; ++j) {
			matrix.at(j, i) /= matrix.at(i, i);
			for (decltype(i) k = i + 1; k < n; ++k) {
				matrix.at(j, k) -= matrix.at(j, i) * matrix.at(i, k);
			}
		}
	}
	return matrix;
}

Matrix::Matrix(const Matrix& rhs) : colls(rhs.colls), rows(rhs.rows), data(rhs.data) {}

Matrix Matrix::swapRow(const int i, const int j) const {
	assertRowExists(i);
	assertRowExists(j);
	const auto n = colls;
	auto copy = Matrix(*this);
	std::swap_ranges(copy.data.begin() + n * i, copy.data.begin() + n * (1 + i), copy.data.begin() + n * j);

	return copy;
}

Matrix Matrix::replaceRowWithVector(const Matrix& v, const int i) const {
	assertIsRowVector(v);
	Matrix composition(*this);
	const auto n = this->colls;
	const auto from = n * i;
	for (int j = 0; j < n; ++j) {
		composition.data.at(from + j) = v.data.at(j);
	}

	return composition;
}

Matrix Matrix::forwardSupstitution(const Matrix& b) const {
	assertSqueareDimension();
	assertMultiplication(b);
	Matrix matrix(b);
	const auto n = this->rows;
	for (decltype(this->rows) i = 0; i < n - 1; ++i) {
		for (decltype(i) j = i + 1; j < n; ++j) {
			matrix.data.at(j) -= this->at(j, i) * matrix.data.at(i);
		}
	}
	return matrix;
}


Matrix Matrix::forwardSupstitution(const Matrix& b, const std::vector<int>& p) const {
	// TODO assert EQ_DIM(b,p)
	std::vector<double> ordered(p.size());
	
	for (int i = 0; i < p.size(); i++) {
		ordered.at(i) = b.data[p[i]];
	}

	return this->forwardSupstitution(Matrix(b.rows, b.colls, ordered));
}

Matrix Matrix::backwardSupstitution(const Matrix& b) const {
	assertSqueareDimension();
	assertMultiplication(b);
	Matrix matrix(b);
	const auto n = this->rows;
	for (decltype(this->rows) i = n - 1; i >= 0; --i) {
		matrix.data.at(i) /= this->at(i, i);
		for (decltype(i) j = 0; j < i; ++j) {
			matrix.data.at(j) -= this->at(j, i) * matrix.data.at(i);
		}
	}
	return matrix;
}

Matrix Matrix::getIdentityMatrix(int n){
	//TODO assert n > 0
	const auto size = n * n;
	std::vector<double> data(size);
	for (decltype(n) i = 0; i < size; i += n + 1) {
		data[i] = 1;
	}
	return Matrix(n, n, data);
}

Matrix Matrix::getZeroMatrix(int n){
	//TODO assert n > 0
	const auto size = n * n;
	std::vector<double> data(size);
	return Matrix(n, n, data);
}

Matrix Matrix::getElemenaryVector(int n, int idxOfOne) {
	//TODO assert n > 0
	//TODO assert n > idxOfOne
	static const int one = 1;
	std::vector<double> d(n);
	d.at(idxOfOne) = one;
	Matrix e(n, 1, d);
	return e;
}



std::vector<int> Matrix::lupDecomposition() {
	std::vector<int> p(this->colls);
	for (int i = 0; i < this->colls; i++)
	{
		p[i] = i;
	}
	for (int i = 0; i < this->colls - 1; i++)
	{
		int pivot = i;
		for (int j = i + 1; j < this->colls; j++)
		{
			if (abs(this->at(j, i) > abs(this->at(pivot, i))))
			{
				pivot = j;
			}
		}
		int tmp = p[i];
		p[i] = p[pivot];
		p[pivot] = tmp;
		*this = this->swapRow(i, pivot);
		if ((this->at(i, i) < 1E-20))
		{
			throw std::runtime_error("lupDecomposition::ZERO_PIVOT");
		}
		for (int j = i + 1; j < this->colls; j++)
		{
			this->at(j, i) /= this->at(i, i);
			for (int k = i + 1; k < this->colls; k++)
			{
				this->at(j, k) -= this->at(j, i) * this->at(i, k);
			}
		}
	}

	if (abs(this->at(this->colls - 1, this->rows - 1) < 1E-20))
	{
		throw std::runtime_error("lupDecomposition::ZERO_PIVOT");
	}

	return	p;
	
	
	/*static const auto eps = 10e-12;
	
	assertSqueareDimension();
	Matrix matrix(*this);
	const auto n = matrix.rows;
	std::vector<int> P(n);
	for (auto i = 0; i < n; ++i) {
		P.at(i) = i;
	}

	for (decltype(matrix.rows) i = 0; i < n - 1; ++i) {
		auto pivot = i;
		for (decltype(i) j = i + 1; j < n; ++j) {
			if (abs(matrix.at(pivot, i)) < abs(matrix.at(j, i))) {
				pivot = j;
			}
		}
		if (pivot != i) {
			matrix.swapRow(i, pivot);
			//P.swapRow(i, pivot);
		}
		if (abs(matrix.at(i, i)) < eps) {
			throw std::runtime_error("lupDecomposition::ZERO_PIVOT");
		}
		for (decltype(i) j = i + 1; j < n; ++j) {
			matrix.at(j, i) /= matrix.at(i, i);
			for (decltype(i) k = i + 1; k < n; ++k) {
				matrix.at(j, k) -= matrix.at(i, k) * matrix.at(j, i);
			}
		}
	}

	return matrix;*/
}

Matrix Matrix::inverse() const {
	assertSqueareDimension();
	Matrix lup(*this);
	const auto p = lup.lupDecomposition();
	const auto n = lup.rows;
	Matrix invert = Matrix::getZeroMatrix(n);
	
	for (auto i = 0; i < n; ++i) {
		Matrix b = Matrix::getElemenaryVector(n, i);
		
		Matrix y = lup.forwardSupstitution(b, p);
		Matrix x = lup.backwardSupstitution(y);

		invert = invert.replaceRowWithVector(x, i);
	}

	Matrix transposedInvert = invert.transpose();
	return transposedInvert;
}