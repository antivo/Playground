#include "parser.h"
#include "simmilarity_matrix.h"
#include "aligner.h"

#include <iostream>
#include <cstring>

#include <xmmintrin.h>
#include <emmintrin.h>


bool getBoolFromArg(char* arg) {
	if (!strcmp(arg, "1")) {
		return true;
	} else if (!strcmp(arg, "0")) {
		return false;
	} else {
		// throw std::exception("Invalid input for argument. Expected '1' or '0'");
		return false;
	}
}

int main(int argc, char** argv) {
	if (6 == argc) {
		try {
			const auto similarityMatrix = nw::parseSimilarityMatrix(argv[1]);
			nw::Aligner aligner(similarityMatrix);

			std::string firstSequence = nw::parseSequenceFile(argv[2]);
			std::string secondSequence = nw::parseSequenceFile(argv[3]);

			bool vectorized = getBoolFromArg(argv[4]);
			bool print = getBoolFromArg(argv[5]);

			aligner.align(vectorized, print, firstSequence, secondSequence, std::cout);

		}
		catch (const std::exception& e) {
			std::cout << e.what();
		}
	} else {
		std::cout << "Invalid number of arguments. 6 arguments needed Similarity Matrix File, First Sequence File, Second Sequence File, 0/1 indicating vectorization, 0/1 indicating printing of the sequences" << std::endl;
	}

	return 0;
}