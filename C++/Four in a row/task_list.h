#ifndef TASK_LIST
#define TASK_LIST

#include <utility>
#include <string>
#include <stack>

class TaskList {
public:	
	TaskList();
	~TaskList();

	static std::string taskToString(const std::pair<int, int>& task);
	static std::pair<int, int> taskFromString(const std::string& task);

	bool isEmpty() const;
	int getMax() const;

	std::pair<int, int> getNextTaskForProcess(int id);
	void writeResult(int id, double result);

private:
	std::stack<std::pair<int, int>> taskList;
	int assignedTo[7][7];
	double results[7][7];

};

#endif