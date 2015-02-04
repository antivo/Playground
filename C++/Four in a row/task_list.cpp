#include "task_list.h"

TaskList::TaskList() {
	const int x = 7;
	for (int i = 0; i < x; ++i) {
		for(int j = 0; j < x; ++j) {
			this->taskList.push(std::make_pair(i, j));
		}
	}

	for (int i = 0; i < 7; ++i) {
		for (int j = 0; j < 7; ++j) {
			this->assignedTo[i][j] = -1;
		}
	}
}

TaskList::~TaskList() {}

std::string TaskList::taskToString(const std::pair<int, int>& task) {
	std::string ss(std::to_string(task.first));
	ss.append(",");
	ss.append(std::to_string(task.second));
	return ss;
}

std::pair<int, int> TaskList::taskFromString(const std::string& task) {
	std::size_t found = task.find(",");
	std::string x = task.substr(0, found);
	std::string y = task.substr(found + 1);

	return std::make_pair(std::stoi(x), std::stoi(y));
}

bool TaskList::isEmpty() const {
	return this->taskList.size() == 0;
}

std::pair<int, int> TaskList::getNextTaskForProcess(int id) {
	std::pair<int, int> task = this->taskList.top(); // get
	this->taskList.pop(); // remove

	this->assignedTo[task.first][task.second] = id;
	
	return task;
}

void TaskList::writeResult(int id, double result) {
	for (int i = 0; i < 7; ++i) {
		for (int j = 0; j < 7; ++j) {
			if (this->assignedTo[i][j] == id) {
				this->results[i][j] = result;

				this->assignedTo[i][j] = -1;
				return;
			}
		}
	}
}

int TaskList::getMax() const {
	double quality[7] = { 0 };
	for (int i = 0; i < 7; ++i) {
		double sum = 0;
		for (int j = 0; j < 7; ++j) {
			sum += results[i][j];
		}
		quality[i] = sum / 7;
	}

	double max = quality[0];
	int idx = 0;
	for (int l = 1; 1 < 7; ++l) {
		if (max < quality[l]) {
			idx = l;
			max = quality[l];
		}
	}

	return idx;
}