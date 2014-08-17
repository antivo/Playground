#ifndef MATRIX
#define MATRIX

#include <vector>

class Matrix {
public:	
	static Matrix getZeroMatrix(int n);
	static Matrix getIdentityMatrix(int n);
	static Matrix getElemenaryVector(int n, int idxOfOne);

	Matrix(const int rows, const int colls, const std::vector<double>& data);
	Matrix(const int rows, const int colls, std::vector<double>&& data);
	Matrix(const Matrix& rhs);
	~Matrix() {}

	Matrix swapRow(const int i, const int j) const;
	Matrix replaceRowWithVector(const Matrix& v, const int i) const;

	//void operator=(const Matrix& rhs) const;
	bool operator==(const Matrix& rhs) const;
	Matrix operator+(const Matrix& rhs) const;
	void operator+=(const Matrix& rhs);
	Matrix operator-(const Matrix& rhs) const;
	void operator-=(const Matrix& rhs);
	Matrix operator*(const double rhs) const;
	void operator*=(const double rhs);
	Matrix operator*(const Matrix& rhs) const;
	void operator*=(const Matrix& rhs);
	Matrix transpose() const;

	double& at(const int i, const int j);
	const double& at(const int i, const int j) const;

	int getRows() const;
	int getColls() const;

	void print(std::ostream& output) const;

	Matrix luDecomposition() const;
	Matrix forwardSupstitution(const Matrix& b) const;
	Matrix forwardSupstitution(const Matrix& b, const std::vector<int>& p) const;
	Matrix backwardSupstitution(const Matrix& b) const;
	//Matrix+P lupDecomposition() const;
	std::vector<int> lupDecomposition();

	Matrix inverse() const;
protected:
	int colls, rows;
	std::vector<double> data;

private:
	void assertMatrix() const;
	void assertSqueareDimension() const;
	void assertEqualDimension(const Matrix& rhs) const;
	void assertMultiplication(const Matrix& rhs) const;
	void assertIsRowVector(const Matrix& v) const;
	void assertRowExists(const int i) const;
	void assertAccess(const int i, const int j) const;
	
};

#endif