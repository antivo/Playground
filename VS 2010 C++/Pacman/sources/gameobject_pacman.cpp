#include "precomp.h"
#include "gameobject_pacman.h"
#include "world.h"

#include <exception>
#include <cmath>
#include <iostream>

GameObject_Pacman::GameObject_Pacman(int x, int y, World *world, int vision) :
	GameObject_Moving(x, y, world), vision(vision)
{
	qCounter = -1;

	memory.insert(std::pair<int, int>(1,1));
	int w = world->map->get_width();
	int h = world->map->get_height();
	memory.insert(std::pair<int, int>(1, h - 2));
	memory.insert(std::pair<int, int>(w - 2 , h - 2));
	memory.insert(std::pair<int, int>(w - 2 , 1));

	spr_pacman = CL_Sprite(world->gc, "Game/spr_pacman", world->resources);
	move_dir = 0;
	anim_pos = 0;
	anim_dir = 1;

	i_am_dead = false;
	got_powerup = false;

	set_speed(4.0);
}

void GameObject_Pacman::show(int view_x, int view_y, CL_GraphicContext &gc)
{
	int width = world->map->get_tile_width() - 6;
	int height = world->map->get_tile_height() - 6;
	int anim_length = spr_pacman.get_frame_count() / 4;
	spr_pacman.set_frame(anim_pos+anim_length*move_dir);
	spr_pacman.draw(gc, (int) (x*width)-view_x, (int) (y*height)-view_y);
}

bool GameObject_Pacman::hit_check(float hit_x, float hit_y)
{
	if (fabs(x-hit_x) < 0.25 && fabs(y-hit_y) < 0.25) return true;
	return false;
}

bool GameObject_Pacman::turn(float time_elapsed)
{
	if (got_powerup && CL_System::get_time() - powerup_starttime>20000) {
		got_powerup = false;
	}

	if (i_am_dead) {
		world->score -= 5000;
		if (world->score < 0) world->score = 0;
		return false;
	}	

	int anim_length = spr_pacman.get_frame_count() / 4;

	anim_pos += anim_dir;
	if (anim_pos >= anim_length-1 || anim_pos == 0) anim_dir*=-1;

	return move(time_elapsed);
}

GameObject_Pacman::direction GameObject_Pacman::searchFlourish() {
	int possible_moves = -1;
	GameObject_Pacman::direction possibleDirections[4] = {};
	GameObject_Pacman::direction tryDirections[4] = {LEFT, RIGHT, UP, DOWN};

	for(int i = 0; i <4; ++i) {
		GameObject_Pacman::direction& actualDirection = tryDirections[i];
		std::pair<int, int> tupple = getTuppleFromDirection(actualDirection);
		int dx = tupple.first;
		int dy = tupple.second;
		Map::TileType type = world->map->get_tile_type(dest_x + dx, dest_y + dy);
		if (type == Map::tile_egg || type == Map::tile_powerup) {
			++possible_moves;
			possibleDirections[possible_moves] = actualDirection;
		}
	}

	if(-1 == possible_moves) {
		throw new std::exception("GameObject_Pacman::searchFlourish no direction");
	}
	
	return possibleDirections[rand() % (++possible_moves)];
}

GameObject_Pacman::direction GameObject_Pacman::shorthestPath(int x, int y, int* steps) {
	if(dest_x == x && dest_y == y) {
		if (steps) {
			*steps = 0;
		}
		return STOP;
	}

	typedef std::vector<GameObject_Pacman::tripple> Branches;
	
	Branches branches;
	GameObject_Pacman::direction tryDirections[4] = {LEFT, RIGHT, UP, DOWN};
	for(int i = 0; i < 4; ++i) {
		GameObject_Pacman::direction& actualDirection = tryDirections[i];
		std::pair<int, int> tupple = getTuppleFromDirection(actualDirection);
		int x_coord = tupple.first + dest_x;
		int y_coord = tupple.second + dest_y;
		if (world->map->get_tile_type(x_coord, y_coord) != Map::tile_wall) {
			direction dir = actualDirection;
			std::vector<std::pair<int, int> > newNodes;
			newNodes.push_back(std::pair<int,int>(x_coord, y_coord));
			std::set<std::pair<int,int> > vis;
			vis.insert(std::pair<int,int>(dest_x, dest_y));
			tripple theTrippy(dir, vis, newNodes);
			branches.push_back(theTrippy);
		}
	}

	if(steps) {
		*steps = 1;
	}

	int width = world->map->get_width();
	int height = world->map->get_height();
	while(1) {
		if(steps) {
			++(*steps);
		}
		for(int i = 0; i < branches.size(); ++i) {
			std::vector<std::pair<int,int> >& newNodes = branches[i].newNodes;
			std::set<std::pair<int,int> >& visited = branches[i].visited;
			for (int j = 0; j < newNodes.size(); ++j) {
				const std::pair<int,int>& node = newNodes[j];
				if(node.first == x && node.second == y) {
					return branches[i].dir;
				}
				visited.insert(node);
			}


			std::vector<std::pair<int,int> > replacementForNewNodes;
			for (int j = 0; j < newNodes.size(); ++j) {
				const std::pair<int,int>& node = newNodes[j];
			
				for(int i = 0; i < 4; ++i) {
					GameObject_Pacman::direction& actualDirection = tryDirections[i];
					std::pair<int, int> tupple = getTuppleFromDirection(actualDirection);
					tupple.first += node.first;
					tupple.second += node.second;
					if(tupple.first < 0 || tupple.second < 0 || tupple.first >= width || tupple.second >= height) {
						continue;
					}
					if (world->map->get_tile_type(tupple.first, tupple.second) != Map::tile_wall) {
						if(!visited.count(tupple)) {
							replacementForNewNodes.push_back(tupple);
						}
					}

				}
			}
			newNodes = replacementForNewNodes;

		}
	}
	

}

GameObject_Pacman::direction GameObject_Pacman::searchDesperate() {
	if (0 != memory.size()) {
		std::pair<int, int> mem = *memory.begin();
		return shorthestPath(mem.first, mem.second);
	}

	return RAND;
}




GameObject_Pacman::direction GameObject_Pacman::searchPoor() {
	int width = world->map->get_width();
	int height = world->map->get_height();

	for(int i = -vision/2; i <= vision/2; ++i) {
		int x_coord = dest_x + i;
		if(x_coord < 0 || x_coord >= width) {
			continue;
		}
		for(int j = -vision/2; j <= vision/2; ++j) {
			int y_coord = dest_y + j;
			if(y_coord < 0 || y_coord >= height) {
				continue;
			}
			Map::TileType type = world->map->get_tile_type(x_coord, y_coord);
			if (type == Map::tile_egg || type == Map::tile_powerup) {
				return shorthestPath(dest_x + i, dest_y + j);
			}
		}
	}

	throw new std::exception("GameObject_Pacman::searchPoor no poor direction");
}

GameObject_Pacman::direction GameObject_Pacman::killEmAll() {
	int myPos_x = this->get_x();
	int myPos_y = this->get_y();
	int theirPos_x, theirPos_y, temp;
	int min = INT_MAX;
	int xOfMin, yOfMin;
	for (std::list<GameObject*>::iterator it = world->objects.begin(); it != world->objects.end(); ++it) {
		if(this != *it) {
			theirPos_x = (*it)->get_x();
			theirPos_y = (*it)->get_y();

			return shorthestPath(theirPos_x, theirPos_y, &temp);

			if(temp < min) {
				min = temp;
				xOfMin = theirPos_x;
				yOfMin = theirPos_y;
			}
		}
	}

	this->got_powerup = false;
	return searchDesperate();
}

GameObject_Pacman::direction GameObject_Pacman::stall() {
	bool danger = false;
	int myPos_x = this->get_x();
	int myPos_y = this->get_y();
	int theirPos_x, theirPos_y;
	for (std::list<GameObject*>::iterator it = world->objects.begin(); it != world->objects.end(); ++it) {
		if(*it != this) {
			theirPos_x = (*it)->get_x();
			theirPos_y = (*it)->get_y();

			if((abs(myPos_x - theirPos_x) <= 3) && (abs(myPos_y - theirPos_y) <= 3)){
				int possible_moves = -1;
				GameObject_Pacman::direction possibleDirections[4] = {};
				GameObject_Pacman::direction tryDirections[4] = {LEFT, RIGHT, UP, DOWN};
				for(int i = 0; i <4; ++i) {
					GameObject_Pacman::direction& actualDirection = tryDirections[i];
					std::pair<int, int> tupple = getTuppleFromDirection(actualDirection);
					int dx =  tupple.first;
					int dy = tupple.second;
					Map::TileType type = world->map->get_tile_type(dest_x + dx, dest_y + dy);
					if (type == Map::tile_powerup) {
						return shorthestPath(dest_x + dx, dest_y + dy);
					}
				}
			}
		}
	}

	return STOP;
}

GameObject_Pacman::direction GameObject_Pacman::flee() {
	
	
	int w = world->map->get_width();
	int h = world->map->get_height();

	std::vector<std::pair<int, int> > ghostPositions;

	int theirPos_x, theirPos_y, theirMin = INT_MAX, ghostTemp;
	for (std::list<GameObject*>::iterator it = world->objects.begin(); it != world->objects.end(); ++it) {
		if(*it != this) {
			theirPos_x = (*it)->get_x();
			theirPos_y = (*it)->get_y();

			ghostPositions.push_back(std::pair<int, int>(theirPos_x, theirPos_y));
		}
	}

	
	std::pair<int,int> min;
	int maximum = -INT_MAX;
	std::pair<int, int> minMax;
	for(int i = -vision/2; i <= vision/2; ++i) {
		for(int j = -vision/2; j <= vision/2; ++j) {
			int temp_x = dest_x + i;
			int temp_y = dest_y + j;

			if (temp_x < 0 || temp_x >= w || temp_y < 0 || temp_y >= h) {
				continue;
			}
			Map::TileType type = world->map->get_tile_type(temp_x, temp_y);
			if (type != Map::tile_wall) {
				int minimum = INT_MAX;
				for(int k = 0; k < ghostPositions.size(); ++k) {
					int udaljenost = sqrt((double)(temp_x - ghostPositions[k].first) * (temp_x - ghostPositions[k].first)
						                         + (temp_y - ghostPositions[k].second) * (temp_y - ghostPositions[k].second));
					if(minimum > udaljenost) {
						minimum = udaljenost;
						min.first = ghostPositions[k].first;
						min.second = ghostPositions[k].second;
					}
				}
				
				if (maximum < minimum) {
					maximum = minimum;
					minMax.first = temp_x;
					minMax.second = temp_y;
				}
				
				

			}
		}
	}

	return shorthestPath(minMax.first, minMax.second);
}


void GameObject_Pacman::assessSituation() {
	if (this->got_powerup && world->objects.size() > 1) {
		for (std::list<GameObject*>::iterator it = world->objects.begin(); it != world->objects.end(); ++it) {
			if(*it != this) {
				myState = GameObject_Pacman::KILL_EM_ALL;
				return;
			}
		}
	}
	
	// DANGER
	bool danger = false;
	int myPos_x = this->get_x();
	int myPos_y = this->get_y();
	int theirPos_x, theirPos_y;
	for (std::list<GameObject*>::iterator it = world->objects.begin(); it != world->objects.end(); ++it) {
		if(*it != this) {
			theirPos_x = (*it)->get_x();
			theirPos_y = (*it)->get_y();

			if((abs(myPos_x - theirPos_x) <= vision) && (abs(myPos_y - theirPos_y) <= vision)){
				danger = true;
				break;
			}
		}
	}
	if(danger) {
		int possible_moves = -1;
		GameObject_Pacman::direction possibleDirections[4] = {};
		GameObject_Pacman::direction tryDirections[4] = {LEFT, RIGHT, UP, DOWN};
		for(int i = 0; i <4; ++i) {
			GameObject_Pacman::direction& actualDirection = tryDirections[i];
			std::pair<int, int> tupple = getTuppleFromDirection(actualDirection);
			int dx =  tupple.first;
			int dy = tupple.second;
			Map::TileType type = world->map->get_tile_type(dest_x + dx, dest_y + dy);
			if (type == Map::tile_powerup) {
				myState = GameObject_Pacman::STALL;
				return;
			}
		}

		
		myState = GameObject_Pacman::FLEE;
		return;
	}



	// SEARCH
	// check FLOURISH
	int possible_moves = -1;
	GameObject_Pacman::direction possibleDirections[4] = {};
	GameObject_Pacman::direction tryDirections[4] = {LEFT, RIGHT, UP, DOWN};
	for(int i = 0; i <4; ++i) {
		GameObject_Pacman::direction& actualDirection = tryDirections[i];
		std::pair<int, int> tupple = getTuppleFromDirection(actualDirection);
		int dx =  tupple.first;
		int dy = tupple.second;
		Map::TileType type = world->map->get_tile_type(dest_x + dx, dest_y + dy);
		if (type == Map::tile_egg || type == Map::tile_powerup) {
			myState = GameObject_Pacman::SEARCH_FLOURISH;
			return;
		}
	}
	// checkPoor
	for(int i = -vision/2; i <= vision/2; ++i) {
		for(int j = -vision/2; j <= vision/2; ++j) {
			int w = world->map->get_width();
			int h = world->map->get_height();

			int temp_x = dest_x + i;
			int temp_y = dest_y + j;

			if (temp_x < 0 || temp_x >= w || temp_y < 0 || temp_y >= h) {
				continue;
			}
			Map::TileType type = world->map->get_tile_type(temp_x, temp_y);
			if (type == Map::tile_egg || type == Map::tile_powerup) {
				myState = GameObject_Pacman::SEARCH_POOR;
				return;
			}
		}
	}

	myState =  GameObject_Pacman::SEARCH_DESPERATE;
}

bool GameObject_Pacman::event_reached_dest()
{

	if (world->map->get_tile_type(dest_x, dest_y) == Map::tile_egg) // std
	{
		world->score += 125;
		world->map->eat_egg(dest_x, dest_y);
	}
	if (world->map->get_tile_type(dest_x, dest_y) == Map::tile_powerup) // powerup
	{
		world->score += 500;
		world->map->eat_egg(dest_x, dest_y);
		got_powerup = true;
		powerup_starttime = CL_System::get_time();
	}
	world->map->leave_trail(dest_x, dest_y);

	// insert into memory
	bool search = true;
	for(int i = -vision/2; (i <= vision/2) && search; ++i) {
		for(int j = -vision/2; (j <= vision/2) && search; ++j) {
			int w = world->map->get_width();
			int h = world->map->get_height();

			int temp_x = dest_x + i;
			int temp_y = dest_y + j;

			if (temp_x < 0 || temp_x >= w || temp_y < 0 || temp_y >= h) {
				continue;
			}
			if (world->map->get_tile_type(temp_x, temp_y) == Map::tile_egg) {
				if (rand() % 10 > 1) {
					memory.insert(std::pair<int, int>(temp_x, temp_y));
					search = false;
				}
			}
		}
	}

	std::pair<int, int> ovdje(dest_x, dest_y);
	if (memory.count(ovdje)) {
		memory.erase(ovdje);
	}
	// memory

	this->assessSituation();
	this->getNextMove();

	

	std::pair<int, int> moveCoordinates = getTuppleFromDirection(static_cast<direction>(move_dir));
	int dx = moveCoordinates.first;
	int dy = moveCoordinates.second;

	if(qCounter > 4) {
		qCounter = (++qCounter) % 5;
		if(q[0] == q[2] && q[2] == q[4] && q[0] != q[1] && q[1] == q[3]) {
			moveCoordinates = getTuppleFromDirection(static_cast<direction>(RAND));
	        dx = moveCoordinates.first;
	        dy = moveCoordinates.second;
		}
		q.pop_back();
	} else {
		++qCounter;
	}
	q.push_front(std::pair<int, int>(dx,dy));
	

	do_move(dx, dy);


	return true;
}