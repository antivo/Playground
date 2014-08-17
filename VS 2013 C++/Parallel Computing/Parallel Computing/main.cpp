#include "philosopher.h"

#include <iostream>

#include <functional>
#include <mpi.h>

int main(int argc, char** argv) {
	MPI_Init(&argc, &argv);
	int rank, size;
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &size);

	static const char signal = 'a';
	static char reciever;
	
	std::function<void(int, int)> isend = [](int destination, int tag){
		MPI_Isend((void*) &signal, 1, MPI_CHAR, destination, tag, MPI_COMM_WORLD, nullptr);
	};
	
	std::function<bool(int, int)> irecv = [](int source, int tag) {
		MPI_Request request;
		MPI_Irecv(&reciever, 1, MPI_CHAR, source, tag, MPI_COMM_WORLD, &request);

		int flag;
		MPI_Status status;
		MPI_Test(&request, &flag, &status);
		
		return flag;
	};


	Philosopher philosopher(rank, size);
	while (true) {
		philosopher.iteration(std::cout, isend, irecv);
	}

	MPI_Finalize();
}