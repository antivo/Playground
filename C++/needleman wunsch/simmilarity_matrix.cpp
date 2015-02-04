#include "simmilarity_matrix.h"

namespace nw {
	SimmilarityMatrix::SimmilarityMatrix(int mismatch) {
		for (std::array<int, 26>& row : this->similarityMatrix) {
			row.fill(mismatch);
		}
	}

	int SimmilarityMatrix::alphaToKey(const char alpha) {
		const auto a = toupper(alpha);
		const auto key = static_cast<int>(a) - SimmilarityMatrix::letterA;

		if (key > letterZ) {
			throw std::runtime_error("Simmilarity Matrix uses only letters A - Z");
		}


		return key;
	}

	void SimmilarityMatrix::addEntry(const char compared, const char comparedWIth, const int value, bool symetrical) {
		const auto keyCompared = alphaToKey(compared);
		const auto keyComparedWith = alphaToKey(comparedWIth);

		similarityMatrix.at(keyCompared).at(keyComparedWith) = value;

		if (symetrical) {
			similarityMatrix.at(keyComparedWith).at(keyCompared) = value;
		}
	}

	int SimmilarityMatrix::getSimilarity(const char compared, const char comparedWIth) const {
		const auto keyCompared = alphaToKey(compared);
		const auto keyComparedWith = alphaToKey(comparedWIth);

		return similarityMatrix.at(keyCompared).at(keyComparedWith);
	}

}