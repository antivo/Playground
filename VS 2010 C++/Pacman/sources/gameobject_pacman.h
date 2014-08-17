#ifndef header_gameobject_pacman
#define header_gameobject_pacman

#if _MSC_VER > 1000
#pragma once
#endif

#include "gameobject_moving.h"

#include <set>
#include <deque>

class GameObject_Pacman : public GameObject_Moving
{
//!Construction:
public:
	//: Construct a pacman at position (x,y).
	GameObject_Pacman(int x, int y, World *world, int vision);

//!Attributes:
public:
	//: Returns true if pacman got the powerup.
	bool get_got_powerup() { return got_powerup; }

//!Operations:
public:
	//: Draw pacman onto graphic context.
	virtual void show(int view_x, int view_y, CL_GraphicContext &gc);

	//: Move pacman.
	virtual bool turn(float time_elapsed);

	//: Kill the pacman.
	void you_are_dead() { i_am_dead = true; }

	//: Check if pacman is at specified location.
	virtual bool hit_check(float x, float y);

//!Implementation:
private:

	// IMS 
	std::set<std::pair<int, int>> memory;

	const int vision;

	typedef enum {SEARCH_FLOURISH, SEARCH_POOR, SEARCH_DESPERATE, FLEE, STALL, KILL_EM_ALL} state;
	typedef enum {STOP = -1, LEFT = 0, RIGHT = 1, UP = 2, DOWN = 3, RAND = 4} direction;

	std::pair<int,int> getTuppleFromDirection(direction d) {
		switch(d) {
		case(RAND): return getTuppleFromDirection(static_cast<direction>(rand() % 4));
		case(STOP): return std::pair<int, int>(0,0);
		case(LEFT): return std::pair<int, int>(-1,0);
		case(RIGHT): return std::pair<int, int>(1,0);
		case(DOWN): return std::pair<int, int>(0,1);
		case(UP): return std::pair<int, int>(0,-1);
		default: throw new std::exception("Unsuported directory");
		}
	}

	state myState;
	void assessSituation();

	direction shorthestPath(int x, int y, int* steps = 0);
	direction searchFlourish();
	direction searchPoor();
	direction searchDesperate();
	direction killEmAll();
	direction stall();
	direction flee();

	std::deque<std::pair<int,int>> q;
	int qCounter;

	void getNextMove() {
		switch (this->myState) {
		case SEARCH_FLOURISH: { move_dir = static_cast<int>(searchFlourish());
								return;
							  }
		case SEARCH_POOR: { move_dir = static_cast<int>(searchPoor());
						    return;
						  };
		case SEARCH_DESPERATE: {  move_dir = static_cast<int>(searchDesperate());
								  return;
							   }
		case KILL_EM_ALL: {  move_dir = static_cast<int>(killEmAll());
							 return;
						  }
		case STALL: {  move_dir = static_cast<int>(stall());
					   return;
					}
		case FLEE: { move_dir = static_cast<int>(flee());
					 return;
				   }

		default: throw new std::exception("GameObject_Pacman::getNextMove::switch(this->state) default value.");
		}
	}

	typedef struct tripple {
		tripple(direction& d, std::set<std::pair<int,int> >& v, std::vector<std::pair<int,int> >& n) {
			dir = d;
			visited = v;
			newNodes = n;
		}

		direction dir;
		std::set<std::pair<int,int> > visited;
		std::vector<std::pair<int,int> > newNodes;
	};

	// IMS - END
	
	//: Called when pacman reaches its destination.
	virtual bool event_reached_dest();

	//void on_key_down(const CL_InputEvent &key, const CL_InputState &state);
	
	int anim_pos, anim_dir;

	int move_dir;
	/*int wanted_dir;*/

	bool i_am_dead;

	bool got_powerup;

	int powerup_starttime;

	//: Pacman images.
	CL_Sprite spr_pacman;

	CL_SlotContainer slots;
};

#endif
