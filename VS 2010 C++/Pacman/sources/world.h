#ifndef header_world
#define header_world

#if _MSC_VER > 1000
#pragma once
#endif

#include "gameobject.h"
#include "gameobject_pacman.h"
#include "map.h"
#include <list>

class FontBlowUp;

class WorldConfiguration;

class World
{
public:
	World(CL_ResourceManager *resources, CL_DisplayWindow &window);
	~World();
public:
	CL_ResourceManager *resources;
	Map *map;
	CL_GraphicContext gc;
	std::list<GameObject*> objects;
	GameObject_Pacman *player;
	int score;
	bool quit;
public:
	void run(CL_DisplayWindow &window, const WorldConfiguration& configuration);

public:
	void on_quit() { quit = true; }

private:
	void on_key_down(const CL_InputEvent &key, const CL_InputState &state);
	int view_x, view_y;
	CL_Font fnt_clansoft;
	CL_DisplayWindow game_display_window;
};

#endif
