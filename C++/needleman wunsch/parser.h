#ifndef PARSER
#define PARSER 

#include <string>
#include <memory>

namespace nw {
	class SimmilarityMatrix;
	
	std::shared_ptr<const SimmilarityMatrix> parseSimilarityMatrix(const std::string& filename);

	std::string parseSequenceFile(const std::string& filename);
}

#endif