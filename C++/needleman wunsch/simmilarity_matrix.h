#ifndef SIMMILARITYMATRIX
#define SIMMILARITYMATRIX

#include <array>

namespace nw {
	class SimmilarityMatrix {
	public:
		SimmilarityMatrix(int mismatch = -1);

		void addEntry(const char compared, const char comparedWIth, const int value, bool symetrical = true);
		int getSimilarity(const char compared, const char comparedWIth) const;

	private:
		static const auto letterA = 65;
		static const auto letterZ = 90;

		static int alphaToKey(const char alpha);

		std::array<std::array<int, 26>, 26> similarityMatrix;
	};
}

#endif