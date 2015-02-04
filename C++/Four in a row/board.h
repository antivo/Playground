#ifndef BOARD 
#define BOARD

#define WIDTH 7

#include <ostream>
#include <vector>

class Board {
public:
	enum Player {
		NONE = 0,
		CPU = 1,
		HUMAN = 2,
	};

	int getWidth();

	static Board fromString(const std::string& ss);
	std::string toString() const;

	void make_move(const Board::Player mark, const int collumn);
	void undo_move(const int collumn);

	Player	isWon(const int lastMove) const;
	void print(std::ostream& out) const;

private:
	static const int width = 7;
	static const int winCondition = 4;

	int get_height() const;

	Player checkVertical(const int collumn) const;
	Player checkHorizontal(const int collumn) const;
	Player checkMainDiagonal(const int collumn) const;
	Player checkSideDiagonal(const int collumn) const;

	std::vector<Player> horizontal[7];

};

#endif
