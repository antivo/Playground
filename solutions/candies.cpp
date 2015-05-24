#include <iostream>
#include <climits>

int main() {
  int n;
  std::cin >> n;

  auto candyCounter = 0;
  auto child = 0;
  decltype(child) asLastReward = 0, lastPerformance = 0, rewardBeforeStreak = INT_MAX;
  decltype(n) startOfStreak = 0;
  for(decltype(n) i = 0; i < n; ++i) {
    std::cin >> child;
    if (child > lastPerformance) {
      ++asLastReward;
    } else if (child == lastPerformance) {
      startOfStreak = i;
      rewardBeforeStreak = INT_MAX;
      asLastReward = 1;
    } else {
      if(asLastReward > 1) {
        startOfStreak = i;
        rewardBeforeStreak = asLastReward;
      }
      if(1 == asLastReward) {
        const auto length = i - startOfStreak;
        candyCounter += length;
        if(length + 1 == rewardBeforeStreak) {
          ++candyCounter;
          ++rewardBeforeStreak;
        } 
      }
      asLastReward = 1;
    }
    candyCounter += asLastReward;
    lastPerformance = child;
  }
    
  std::cout << candyCounter ;   
  return 0;
}