#ifndef ALIGNER
#define ALIGNER

#include "simmilarity_matrix.h"

#include <string>
#include <memory>
#include <ostream>
#include <functional>

namespace nw {
	class Aligner {
	private:
		enum direction{ NONE, HORIZONTAL, VERTICAL, DIAGONAL };
		static const char SEQUENCE_GAP = '-';

		static void cleanup(int** F, direction** trace, const int firstSequenceLength, const int secondSequenceLength);

	public:
		Aligner(const std::shared_ptr<const nw::SimmilarityMatrix>& similarityMatrix, int gap = -2);

		void align(bool vectorized,
				   bool print,
				   const std::string& firstSequence,
			       const std::string& secondSequence, 
				   std::ostream& out) const;
		
	private:
		const std::shared_ptr<const nw::SimmilarityMatrix> similarityMatrix;
		const int gap;

		void initialize(int*** adressF, direction*** adressTrace, const int firstSequenceLength, const int secondSequenceLength) const;

		void printAligment(direction** trace, const std::string& firstSequence,
			const std::string& secondSequence, std::ostream& out) const;


		void timeMeasurer(std::ostream& out, std::function<void(void)> block) const;
	};
}

#endif