#include "aligner.h"

#include <xmmintrin.h>
#include <emmintrin.h>

#include <sstream>
#include <algorithm>
#include <chrono>

namespace nw {
	Aligner::Aligner(const std::shared_ptr<const nw::SimmilarityMatrix>& similarityMatrix, int gap): 
		similarityMatrix(similarityMatrix), gap(gap) {}

	void Aligner::printAligment(direction** trace, const std::string& firstSequence,
		const std::string& secondSequence, std::ostream& out) const {
		auto i = secondSequence.length();
		auto j = firstSequence.length();
		auto k = 0;

		std::stringstream firstSS, secondSS;
		while (i > 0 || j > 0) {
			switch (trace[i][j]) {
			case VERTICAL: {
				firstSS << Aligner::SEQUENCE_GAP;
				secondSS << secondSequence.at(i - 1);
				--i;
				break;
			}
			case DIAGONAL: {
				firstSS << firstSequence.at(j - 1);
				secondSS << secondSequence.at(i - 1);
				--i;
				--j;
				break;
			}
			case HORIZONTAL: {
				firstSS << firstSequence.at(j - 1);
				secondSS << Aligner::SEQUENCE_GAP;
				--j;
			}
			}

			++k;
		}

		std::string firstSequenceAligned(firstSS.str()), secondSequenceAligned(secondSS.str());

		std::reverse(firstSequenceAligned.begin(), firstSequenceAligned.end());
		std::reverse(secondSequenceAligned.begin(), secondSequenceAligned.end());

		out << "First sequence aligned:" << std::endl << firstSequenceAligned << std::endl;
		out << "Second sequence aligned:" << std::endl << secondSequenceAligned << std::endl;
	}

	void Aligner::initialize(int*** adressF, direction*** adressTrace, const int firstSequenceLength, const int secondSequenceLength) const {
		int** F = new int*[secondSequenceLength + 1];
		for (auto i = 0; i <= secondSequenceLength; i++) {
			F[i] = new int[firstSequenceLength];
		}

		direction** trace = new direction*[secondSequenceLength + 1];
		for (auto i = 0; i <= secondSequenceLength; i++) {
			trace[i] = new direction[firstSequenceLength];
		}

		F[0][0] = 0;
		trace[0][0] = NONE;

		for (auto i = 1; i <= firstSequenceLength; i++)
		{
			F[0][i] = gap * i;
			trace[0][i] = HORIZONTAL;
		}

		for (auto i = 1; i <= secondSequenceLength; i++)
		{
			F[i][0] = gap * i;
			trace[i][0] = VERTICAL;
		}

		*adressF = F;
		*adressTrace = trace;
	}

	void Aligner::cleanup(int** F, direction** trace, const int firstSequenceLength, const int secondSequenceLength) {
		delete [] F;
		delete trace;
	}

	void Aligner::timeMeasurer(std::ostream& out, std::function<void(void)> block) const {
		using std::chrono::high_resolution_clock;
		using std::chrono::seconds;
		using std::chrono::microseconds;
		using std::chrono::nanoseconds;
		

		const auto t0 = high_resolution_clock::now();
		block();
		const auto t1 = high_resolution_clock::now();

		const auto diff = t1 - t0;
		nanoseconds total_ns = std::chrono::duration_cast<nanoseconds>(diff);
		microseconds total_ms = std::chrono::duration_cast<microseconds>(diff);
		seconds total_s = std::chrono::duration_cast<seconds>(diff);

		out << "seconds: " << total_s.count() << "s" << std::endl;
		out << "OR" << std::endl;
		out << "microseconds: " << total_ms.count() << "ms" << std::endl;
		out << "OR" << std::endl;
		out << "nanoseconds: " << total_ns.count() << "ns" << std::endl;
	}

	void Aligner::align(bool vectorized, bool print, const std::string& firstSequence, const std::string& secondSequence, std::ostream& out)  const{
		auto firstSequenceLength = firstSequence.length();
		auto secondSequenceLength = secondSequence.length();

		int** F = nullptr;
		direction** trace = nullptr;
		initialize(&F, &trace, firstSequenceLength, secondSequenceLength);

		std::function<void(void)> regularBlock = [&F, &trace, firstSequenceLength, secondSequenceLength, &firstSequence, &secondSequence, this]() {
			for (decltype(secondSequenceLength) i = 1; i <= secondSequenceLength; i++) {
				for (decltype(firstSequenceLength) j = 1; j <= firstSequenceLength; j++) {
					const auto compared = firstSequence.at(j - 1);
					const auto comparedWith = secondSequence.at(i - 1);

					int p = this->similarityMatrix->getSimilarity(compared, comparedWith);

					const auto fU = F[i - 1][j] + gap;
					const auto fD = F[i - 1][j - 1] + p;
					const auto fL = F[i][j - 1] + gap;

					if (fU >= fD && fU >= fL) {
						F[i][j] = fU;
						trace[i][j] = VERTICAL;
					}
					else if (fD > fL) {
						F[i][j] = fD;
						trace[i][j] = DIAGONAL;
					}
					else {
						F[i][j] = fL;
						trace[i][j] = HORIZONTAL;
					}
				}
			}
		};

		std::function<void(void)> vectorizedBlock = [&F, &trace, firstSequenceLength, secondSequenceLength, &firstSequence, &secondSequence, this]() {
			int a[4] = { 0, 0, 0, 0 };
			int b[4] = { gap, 0, gap, 0 };

			__m128i* pa = (__m128i*)a;
			__m128i* pb = (__m128i*)b;

			for (decltype(secondSequenceLength) i = 1; i <= secondSequenceLength; i++) {
				for (decltype(firstSequenceLength) j = 1; j <= firstSequenceLength; j++) {
					const auto compared = firstSequence.at(j - 1);
					const auto comparedWith = secondSequence.at(i - 1);

					b[1] = this->similarityMatrix->getSimilarity(compared, comparedWith);

					a[0] = F[i - 1][j];
					a[1] = F[i - 1][j - 1];
					a[2] = F[i][j - 1];
					*pa = _mm_add_epi32(*pa, *pb);

					if (a[0] >= a[1] && a[0] >= a[2]) {
						F[i][j] = a[0];
						trace[i][j] = VERTICAL;
					}
					else if (a[1] > a[2]) {
						F[i][j] = a[1];
						trace[i][j] = DIAGONAL;
					}
					else {
						F[i][j] = a[2];
						trace[i][j] = HORIZONTAL;
					}
				}
			}
		};

		if (vectorized) {
			timeMeasurer(out, vectorizedBlock);;
		} else {
			timeMeasurer(out, regularBlock);;
		}
		if (print) {
			printAligment(trace, firstSequence, secondSequence, out);
		}
		cleanup(F, trace, firstSequenceLength, secondSequenceLength);

	}
}