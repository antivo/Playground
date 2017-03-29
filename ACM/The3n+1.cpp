/*

ACM 100 - The 3n + 1 problem

*/

#include <iostream>

const long MAX_INPUT = 10000;
long memo[MAX_INPUT];

void problem(long x) {
    long counter = 0;
	long n = x;
	while(MAX_INPUT < n || 0 == memo[n]) {
		++counter;
		if(1 == n % 2) {
			n = 3*n + 1;
		} else {
			n = n / 2;
		}
	}
	memo[x] = counter + memo[n];
}

int main() {
	memo[0] = 1;
	memo[1] = 1;
	for(long m = 2; m < MAX_INPUT; ++m) {
		memo[m] = 0;
	}

	for(long x = 1; x <= 10000; ++x) {
		problem(x);
	}	
	
	long i, j;
	while (std::cin >> i) {
		std::cin >> j;
		
		for(long x = i; x <= j; ++x) {
			problem(x);
		}
			
		long max = 0;
		for(long x = i; x <= j; ++x) {
			if(memo[x] > max) {
				max = memo[x];
			}
		}		
		std::cout << i << " " << j << " " << max << std::endl;
	}
	
	return 0;
}