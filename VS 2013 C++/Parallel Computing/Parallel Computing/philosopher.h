#ifndef PHILOSOPHER
#define PHILOSOPHER

#include <functional>
#include <memory>
#include <utility>
#include <set>


class Spoon;

class Philosopher {
public:
	Philosopher(const int myId, const int sizeOfGroup);
	~Philosopher();

	void iteration(std::ostream& out,
		std::function<void(int, int)> isend,
		std::function<bool(int, int)> irecv);

private:
	std::set<int> requests;

	std::unique_ptr<Spoon> Philosopher::decideSpoonFate(std::ostream& out, std::unique_ptr<Spoon> spoon, int neighbour, std::function<void(int, int)> isend);
	void askForSpoon(std::ostream& out, int neighbour, std::function<void(int, int)> isend);

	void react(std::ostream& out, int neighbour, int tag, std::function<void(int, int)> isend);
	void checkMail(std::ostream& out, std::function<void(int, int)> isend, std::function<bool(int, int)> irecv);

	void think(std::ostream& out, std::function<void(int, int)> isend, std::function<bool(int, int)> irecv);
	void starve(std::ostream& out, std::function<void(int, int)> isend, std::function<bool(int, int)> irecv);
	void eat(std::ostream& out);

	static const int requestTag = 0;
	static const int responseTag = 1;
	static const int maxSleep = 4;

	const int myId;
	std::unique_ptr<Spoon> leftSpoon;
	std::unique_ptr<Spoon> rightSpoon;
	int leftNeighbour;
	int rightNeighbour;
	std::string offset;

	Philosopher(const Philosopher&) = delete;
	Philosopher& operator=(const Philosopher&) = delete;
};


#endif
