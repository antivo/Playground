#include "parser.h"
#include "matrix.h"

#include <fstream>

/*Matrix parser::makeMatrixFromFile(std::string& input) {
	static const auto MAX_CHARS_PER_LINE = 100;
	static const auto MAX_TOKENS_PER_LINE = 10;
	static const auto DELIMITER = " ";

	std::ifstream fin;
	fin.open(input);

	if (!fin.good()) {
		throw std::exception("Given path can not be opened parser::makeMatrixFromFile");
	}
	auto line = 0;
	int rows, colls;
	std::vector<double> matrixData;
	while (!fin.eof()) {
		char buf[MAX_CHARS_PER_LINE];
		fin.getline(buf, MAX_CHARS_PER_LINE);

		int n = 0;

		const char* token[MAX_TOKENS_PER_LINE] = {};
		
		token[0] = strtok(buf, DELIMITER);
		if (token[0]) {
			for (n = 1; n < MAX_TOKENS_PER_LINE; n++) {
				token[n] = strtok(0, DELIMITER);
				if (!token[n]) {
					break;
				}
			}
		}

		switch (line) {
		case 0: {
					if (nullptr != token[1]) {
						throw std::exception("Matrix file corrupt. First line must contain 2 parameters - matrix dimension");
					}

					break;
		}
		default: {
					 break;
		}
		}

		for (int i = 0; i < n; i++) {
			token[i];
		}

		++line;
	}
	if (rows != line + 1) {
		throw std::exception("Matrix file corrupt. Expected number of rows does not match the number of rows defined in matrix file");
	} else {
		return Matrix(rows, colls, matrixData);
	}
}*/
