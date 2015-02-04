#include "parser.h"
#include "simmilarity_matrix.h"

#include <fstream>
#include <vector>
#include <sstream>
#include <cctype>

#include <string>
#include <fstream>
#include <streambuf>




namespace nw {
	std::shared_ptr<const SimmilarityMatrix> parseSimilarityMatrix(const std::string& filename) {
		std::string line;
		std::ifstream myfile(filename, std::ifstream::in);

		if (myfile.is_open()) {
			std::shared_ptr<SimmilarityMatrix> similarityMatrix = std::make_shared<SimmilarityMatrix>();
			while (getline(myfile, line)) {
				std::istringstream input(line);
				std::vector<std::string> words {std::istream_iterator<std::string>{input}, std::istream_iterator<std::string>{} };
				auto counter = 0;
				char firstWord, secondWord;
				int thirdWord;
				bool fourthWord = true;
				for (const std::string& word : words) {
					++counter;
					switch(counter) {
					case	1:	{
									firstWord = word.at(0);
									break; 
					}
					case	2:	{
									secondWord = word.at(0);
									break;
					}
					case	3:	{
									thirdWord = std::stoi(word);
									break;
					}
					case	4:	{
									const auto choice = std::toupper(word.at(0));
									if ('S' == choice) {
										fourthWord = true;
									}
									else if ('A' == choice) {
										fourthWord = false;
									}
									else {
										throw std::exception("Fourth word in simmilarity matrix can be either letter 'A' or letter 'S'");
									}
									break;
					}
					default: throw std::exception("Similartiy Matrix file format corrupt");
					}
				}
				similarityMatrix->addEntry(firstWord, secondWord, thirdWord, fourthWord);
			}
			myfile.close();

			return similarityMatrix;
		}

		throw std::exception("Similarty Matrix file can not be opened");
	}

	std::string parseSequenceFile(const std::string& filename) {
		std::ifstream t(filename);
		std::string str;

		t.seekg(0, std::ios::end);
		str.reserve(t.tellg());
		t.seekg(0, std::ios::beg);

		str.assign((std::istreambuf_iterator<char>(t)),
			std::istreambuf_iterator<char>());

		return str;
	}

	

}