#include "board.h"

/*Board::Board() {}
Board::~Board() {}*/

void Board::make_move(const Board::Player mark, const int collumn) {
	if (collumn >= this->width || collumn < 0) return;
	this->horizontal[collumn].push_back(mark);
}

void Board::undo_move(const int collumn) {
	if (collumn >= this->width || collumn < 0) return;
	this->horizontal[collumn].pop_back();
}

int Board::get_height() const {
	int height = 0;
	for (int i = 0; i < this->width; ++i) {
		int h = this->horizontal[i].size();
		if (h > height) {
			height = h;
		}
	}
	return height - 1;
}

void Board::print(std::ostream& out) const {
	int height = get_height();

	while (height >= 0) {
		for (int i = 0; i < this->width; ++i) {
			if (this->horizontal[i].size() > height) {
				out << this->horizontal[i][height];
			}
			else {
				out << NONE;
			}
			out << "\t";
		}
		out << std::endl;
		--height;
	}
}

Board::Player Board::checkVertical(const int collumn) const {
	const Board::Player mark = this->horizontal[collumn].back();

	int counter = 1;
	Board::Player result = NONE;
	for (int i = this->horizontal[collumn].size() - 2; i >= 0; --i) {
		if (mark != this->horizontal[collumn][i]) {
			break;
		}

		++counter;

		if (winCondition == counter) {
			result = mark;
			break;
		}
	}

	return result;
}

Board::Player Board::checkHorizontal(const int collumn) const {
	const Board::Player mark = this->horizontal[collumn].back();
	const int height = this->horizontal[collumn].size() - 1;

	int counter = 1;
	Board::Player result = NONE;
	// to the left
	int leftCollumn = collumn - 1;
	while (leftCollumn >= 0) {
		if (this->horizontal[leftCollumn].size() < this->horizontal[collumn].size()) break;
		if (this->horizontal[leftCollumn][height] != mark) break;

		++counter;

		if (winCondition == counter) {
			result = mark;
			break;
		}
		--leftCollumn;
	}

	if (result == NONE) {
		// to the right
		int rightCollumn = collumn + 1;
		while (rightCollumn < this->width) {
			if (this->horizontal[rightCollumn].size() < this->horizontal[collumn].size()) break;
			if (this->horizontal[rightCollumn][height] != mark) break;

			++counter;

			if (this->winCondition == counter) {
				result = mark;
				break;
			}
			++rightCollumn;
		}
	}

	return result;
}

Board::Player Board::checkMainDiagonal(const int collumn) const {
	const Board::Player mark = this->horizontal[collumn].back();
	const int height = this->horizontal[collumn].size() - 1;

	int counter = 1;
	Board::Player result = NONE;
	// to the left
	int leftCollumn = collumn - 1;
	int leftIndex = height + 1;
	while (leftCollumn >= 0) {
		if (this->horizontal[leftCollumn].size() <= leftIndex) break;
		if (this->horizontal[leftCollumn][leftIndex] != mark) break;

		++counter;

		if (winCondition == counter) {
			result = mark;
			break;
		}
		--leftCollumn;
		++leftIndex;
	}

	if (result == NONE) {
		// to the right
		int rightCollumn = collumn + 1;
		int rightIndex = height - 1;
		while (rightCollumn < this->width && rightIndex >= 0) {
			if (this->horizontal[rightCollumn].size() <= rightIndex) break;
			if (this->horizontal[rightCollumn][rightIndex] != mark) break;

			++counter;

			if (winCondition == counter) {
				result = mark;
				break;
			}
			++rightCollumn;
			--rightIndex;
		}
	}

	return result;
}

Board::Player Board::checkSideDiagonal(const int collumn) const {
	const Board::Player mark = this->horizontal[collumn].back();
	const int height = this->horizontal[collumn].size() - 1;

	int counter = 1;
	Board::Player result = NONE;
	// to the left
	int leftCollumn = collumn - 1;
	int leftIndex = height - 1;
	while (leftCollumn >= 0 && leftIndex >= 0) {
		if (this->horizontal[leftCollumn].size() <= leftIndex) break;
		if (this->horizontal[leftCollumn][leftIndex] != mark) break;

		++counter;

		if (winCondition == counter) {
			result = mark;
			break;
		}
		--leftCollumn;
		--leftIndex;
	}

	if (result == NONE) {
		// to the right
		int rightCollumn = collumn + 1;
		int rightIndex = height + 1;
		while (rightCollumn < this->width) {
			if (this->horizontal[rightCollumn].size() <= rightIndex) break;
			if (this->horizontal[rightCollumn][rightIndex] != mark) break;

			++counter;

			if (winCondition == counter) {
				result = mark;
				break;
			}
			++rightCollumn;
			++rightIndex;
		}
	}

	return result;
}

Board::Player Board::isWon(const int collumn) const {
	if (collumn >= this->width || collumn < 0) return NONE;
	if (this->horizontal[collumn].size() == 0) return NONE;

	const Board::Player vertical = checkVertical(collumn);
	if (NONE != vertical) {
		return vertical;
	}

	const Board::Player horizontal = checkHorizontal(collumn);
	if (NONE != horizontal) {
		return horizontal;
	}

	const Board::Player mainDiagonal = checkMainDiagonal(collumn);
	if (NONE != mainDiagonal) {
		return mainDiagonal;
	}

	const Board::Player sideDiagonal = checkSideDiagonal(collumn);
	if (NONE != sideDiagonal) {
		return sideDiagonal;
	}

	return NONE;
}

Board Board::fromString(const std::string& ss) {
	Board board;
	int collumn = -1;
	for (int i = 0; i < ss.size(); ++i) {
		collumn = (++collumn) % 7;
		switch (ss[i]) {
		case '0': {
			break;
		}
		case '1': {
			board.make_move(Board::CPU, collumn);
			break;
		}
		case '2': {
			board.make_move(Board::HUMAN, collumn);
			break;
		}
		}
	}
	return board;
}

std::string Board::toString() const {
	std::string ss;

	int height = this->get_height();
	for (int h = 0; h <= height; ++h) {
		for (int i = 0; i < this->width; ++i) {
			if (this->horizontal[i].size() <= h) {
				ss.push_back('0');
			}
			else {
				if (this->horizontal[i][h] == CPU) {
					ss.push_back('1');
				}
				else {
					ss.push_back('2');
				}
			}
		}
	}

	return ss;
}

int Board::getWidth() {
	return WIDTH;
}