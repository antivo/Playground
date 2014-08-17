/*
Author: Stjepan Antivo Ivica
E-mail: stjepan-antivo.ivica@fer.hr

Description:
-> input:
  in the first line number N representing number of nodes, 1 <= N < 2 000 000
  in each of the next N lines numbers X Y representing conected nodes, 0 <= X,Y <= 2 000 000
  
-> output:
  in the first line number K representing a number of different groups.
  in each of the next K lines writes a nodes belonging to one group
*/

#include <cstdio>
#include <cstring>
#include <vector>
#include <map>

#include <iostream>

#define MAX_NODES 2000000

void propagate(long* groupMemo, long& observedGroup, long designatedGroup) {
  if(designatedGroup != observedGroup) {
    if(-1 == observedGroup) {
      observedGroup = designatedGroup;
    } else {
      long current = observedGroup;
      if (current > designatedGroup) {
        observedGroup = designatedGroup;
        auto& newObservedGroup = groupMemo[current];
        propagate(groupMemo, newObservedGroup, designatedGroup);
      } else {
        auto& newObservedGroup = groupMemo[designatedGroup];
        propagate(groupMemo, newObservedGroup, current);
      }
    }
  }
}

int main() {
  int n;
  scanf("%d", &n);
  long newGroup = 0;
  auto nodes = new long[MAX_NODES];
  memset(nodes, -1, MAX_NODES);
  auto groups = new long[MAX_NODES];
  memset(groups, -1, MAX_NODES);
  
  int max_index = 0;
  for(auto i = 0; i < n; ++i) {
    int first, second;
    scanf("%d %d", &first, &second);
    if(max_index < first) {
      max_index = first;
    }
    if(max_index < second) {
      max_index = second;
    }
    auto& x = nodes[first];
    auto& y = nodes[second];
    if(x == y) {
      if(-1 == x) {
        x = newGroup;
        y = newGroup;
        ++newGroup;
      } // else ignore      
    } else {
      if(-1 == x) {
        x = y;
      } else if(-1 == y) {
        y = x;
      } else {
        if(x > y) {
          auto& thisGroup = groups[x];
          propagate(groups, thisGroup, y);
        } else {
          auto& thisGroup = groups[y];
          propagate(groups, thisGroup, x);
        }
      }
    }
  }

  long numberOfGroups = 0; 
  for(auto i = 0; i < newGroup; ++i) {
    auto& selected = groups[i];
    if(-1 != selected) {
      if(0 != selected) {
        auto trans = groups[selected];
        if(-1 !=  trans) {
          selected = trans;
        }
      }
    } else {
      ++numberOfGroups;
    }
  }
  
  // From now on I did not cared about optimizing the speed
  auto output = new std::map<int, std::vector<int>>;
  for(auto i = 0; i <= max_index; ++i) {
    auto origin = nodes[i]; 
    if(-1 != origin) {
      auto trueOrigin = groups[origin];
      if(-1 == trueOrigin) {
        (*output)[origin].push_back(i);
      } else {
        (*output)[trueOrigin].push_back(i);
      }
    }
  }
  
  std::cout << numberOfGroups << std::endl;
  for(auto outer_iter = output->begin(); outer_iter != output->end(); ++outer_iter) {
    for(auto inner_iter = outer_iter->second.begin(); inner_iter != outer_iter->second.end(); ++inner_iter) {
      std::cout << *inner_iter << " ";
    }
    std::cout << std::endl;
  }
  
  delete output;
  delete groups;
  delete nodes;
  return 0;
}