#include "philosopher.h"
#include "spoon.h"

#include <mpi.h>

#include <ostream>
#include <thread>
#include <chrono>

Philosopher::Philosopher(const int myId, const int sizeOfGroup) :
myId(myId),
leftSpoon(nullptr),
rightSpoon(nullptr),
leftNeighbour(-1),
rightNeighbour(-1),
offset(std::string(myId, '\t'))
{

	const auto firstRank = 0;
	const auto lastRank = sizeOfGroup - 1;

	if (firstRank == myId) {
		leftNeighbour = lastRank;
		leftSpoon = std::make_unique<Spoon>(Spoon::Placement::ME);
	}
	else {
		leftNeighbour = myId - 1;
		leftSpoon = std::make_unique<Spoon>(Spoon::Placement::NEIGHBOUR);
	}

	if (lastRank == myId) {
		rightNeighbour = firstRank;
		rightSpoon = std::make_unique<Spoon>(Spoon::Placement::NEIGHBOUR);
	}
	else {
		rightNeighbour = (myId + 1);
		rightSpoon = std::make_unique<Spoon>(Spoon::Placement::ME);
	}
}

Philosopher::~Philosopher() {}


std::unique_ptr<Spoon> Philosopher::decideSpoonFate(std::ostream& out, std::unique_ptr<Spoon> spoon, int neighbour, std::function<void(int, int)> isend) {
	if (myId != neighbour) {
		if (Spoon::Placement::ME == spoon->getPlacement()) {
			if (Spoon::Condition::DIRTY == spoon->getCondition()) {
				spoon->setPlacement(Spoon::Placement::NEIGHBOUR);
				out << "Sending spoon to " << neighbour;

				isend(neighbour, responseTag);
			}
		}
	}
	return std::move(spoon);
}

void Philosopher::react(std::ostream& out, int neighbour, int tag, std::function<void(int, int)> isend) {
	switch (tag) {
	case responseTag: {
		if (neighbour == leftNeighbour) {
			leftSpoon->setCondition(Spoon::Condition::CLEAN);
		} else {
			rightSpoon->setCondition(Spoon::Condition::CLEAN);
		}

		break;
	}
	case requestTag: {
		if (neighbour == leftNeighbour) {
			decideSpoonFate(out, std::move(leftSpoon), neighbour, isend);
		}
		else {
			decideSpoonFate(out, std::move(rightSpoon), neighbour, isend);
		}


		break;
	}
	default: {
		// throw runtime
	}
	}
}

void Philosopher::checkMail(std::ostream& out, std::function<void(int, int)> isend, std::function<bool(int, int)> irecv) {
	for (auto neighbour : requests) {
		react(out, neighbour, requestTag, isend);
	}

	// clean requests
	requests.clear();

	if (irecv(leftNeighbour, requestTag)) {
		react(out, leftNeighbour, requestTag, isend);
	}

	if (irecv(leftNeighbour, responseTag)) {
		react(out, leftNeighbour, responseTag, isend);
	}

	if (irecv(rightNeighbour, requestTag)) {
		react(out, rightNeighbour, requestTag, isend);
	}

	if (irecv(rightNeighbour, responseTag)) {
		react(out, rightNeighbour, responseTag, isend);
	}
}

void Philosopher::askForSpoon(std::ostream& out, int neighbour, std::function<void(int, int)> isend) {
	out << offset.c_str() << myId << " Process is asking " << neighbour << " for spoon." << std::endl;
	isend(neighbour, requestTag);
}

void Philosopher::think(std::ostream& out, std::function<void(int, int)> isend, std::function<bool(int, int)> irecv) {
	auto sleepingTime = 3; // = rand.Next(maxSleep);
	out << this->offset.c_str() << "Process " << myId << " is thinking." << std::endl;

	for (decltype(sleepingTime) counter = 0; counter < sleepingTime; ++counter) {
		checkMail(out, isend, irecv);
		
		std::chrono::milliseconds duration(1000);
		std::this_thread::sleep_for(duration);
	}
}

void Philosopher::starve(std::ostream& out, std::function<void(int, int)> isend, std::function<bool(int, int)> irecv) {
	out << this->offset.c_str() << "Process " << myId << " is starving." << std::endl;

	while (true) {
		auto haveTools = true;

		if(Spoon::Placement::NEIGHBOUR == this->leftSpoon->getPlacement()) {
			haveTools = false;
			askForSpoon(out, leftNeighbour, isend);
		}

		if ((Spoon::Placement::NEIGHBOUR == this->rightSpoon->getPlacement())) {
			haveTools = false;
			askForSpoon(out, rightNeighbour, isend);
		}
		
		checkMail(out, isend, irecv);

		// sleep
		std::chrono::milliseconds duration(300);
		std::this_thread::sleep_for(duration);

		if (haveTools) {
			break;
		}
	}

}

void Philosopher::eat(std::ostream& out) {
	out << this->offset.c_str() << "Process " << myId << " is eating." << std::endl;

	auto sleepFor = 3; // randNext
	std::chrono::milliseconds duration(sleepFor * 1000);
	std::this_thread::sleep_for(duration);

	this->leftSpoon->setCondition(Spoon::Condition::DIRTY);
	this->rightSpoon->setCondition(Spoon::Condition::DIRTY);

	out << this->offset.c_str() << "Process " << myId << " ate." << std::endl;
}

void Philosopher::iteration(std::ostream& out, std::function<void(int, int)> isend,	std::function<bool(int, int)> irecv) {
	think(out, isend, irecv);
	starve(out, isend, irecv);
	eat(out);
}
