
#include "world_configuration.h"
#include "precomp.h"
#include "world.h"
#include "gameobject_ghost.h"
#include "gameobject_pacman.h"
#include "framerate_counter.h"
#include "program.h"
#ifdef USE_SOFTWARE_RENDERER
#include <ClanLib/swrender.h>
#endif
#include "world_configuration.h"

World::World(CL_ResourceManager *resources, CL_DisplayWindow &window) :
	resources(resources), map(0), player(0), score(0)
{
	gc = window.get_gc();
	fnt_clansoft = CL_Font(gc, L"Tahoma", 32);
	map = new Map(resources, gc);
}

World::~World()
{
	for (
		std::list<GameObject*>::iterator it = objects.begin();
		it != objects.end();
		it++)
	{
		delete *it;
	}
	objects.clear();

}

void World::run(CL_DisplayWindow &window, const WorldConfiguration& configuration)
{
	game_display_window = window;
	gc = window.get_gc();
	quit = false;
	
	CL_Slot slot_key_down = window.get_ic().get_keyboard().sig_key_down().connect(this, &World::on_key_down);

	// Create map:
	map->generate_level(configuration.map.getWidth(), configuration.map.getHeight(), configuration.map.getNumConnections());

	int v =  configuration.pacman.getHeight();
	v = configuration.pacman.getVision();

	// Populate map with pacman:
	player = new GameObject_Pacman(configuration.pacman.getWidth(), configuration.pacman.getHeight(), this, 4);
	objects.push_back(player);
	
	for (int i = 0; i < configuration.ghost.getNum(); ++i)
	{
		objects.push_back(new GameObject_Ghost(configuration.ghost.getWidth(i), configuration.ghost.getHeight(i), this));
	}

	// Start the game simulation:
	int start_time = CL_System::get_time();
	int begin_time = CL_System::get_time();
	int score_time = CL_System::get_time();
	int center_x = 0;
	int center_y = 0;
	bool welcome_shown = false;
	FramerateCounter frameratecounter;

	while (!quit)
	{
		float time_elapsed = (CL_System::get_time() - begin_time)/(float) 1000;
		begin_time = CL_System::get_time();
		if (CL_System::get_time()-score_time > 1000 && player != NULL)
		{
			score -= 50;
			if (score < 0) score = 0;
			score_time = CL_System::get_time(); 
		}
		int width = map->get_tile_width()-6;
		int height = map->get_tile_height()-6;
		if (player != NULL)
		{
			if (gc.get_width() > map->get_width()*width)
			{
				center_x = -(gc.get_width()/2-map->get_width()*width/2);
			}
			else
			{
				center_x = int((player->get_x()+0.5)*width-gc.get_width()/2);
				if (center_x < 0) center_x = 0;
				if (center_x > map->get_width()*width-gc.get_width()) center_x = map->get_width()*width-gc.get_width();
			}
			if (gc.get_height() > map->get_height()*height)
			{
				center_y = -(gc.get_height()/2-map->get_height()*height/2);
			}
			else
			{
				center_y = int((player->get_y()+0.5)*height-gc.get_height()/2);
				if (center_y < 0) center_y = 0;
				if (center_y > map->get_height()*height-gc.get_height()) center_y = map->get_height()*height-gc.get_height();
			}
		} else {
			player = new GameObject_Pacman(map->get_width()/2, map->get_height()/2, this, configuration.pacman.getVision());
			objects.push_back(player);
		}

		gc.clear();
		map->draw(center_x, center_y, gc);
		std::list<GameObject*>::iterator it;
		for (it = objects.begin(); it != objects.end(); ++it)
		{
			GameObject *cur = *it;
			cur->show(center_x, center_y, gc);
		}
		gc.flush_batcher();
		float time_elapsed2 = time_elapsed;
		while (time_elapsed2 > 0)
		{
			float turn_time = (time_elapsed2 > 0.05f) ? 0.05f : time_elapsed2;
			it = objects.begin();
			while (it != objects.end())
			{
				if ((*it)->turn(turn_time) == false)
				{
					if ((*it) == player) player = NULL;
					delete (*it);
					it = objects.erase(it);
				}
				else
				{
					it++;
				}
			}
			
			time_elapsed2 -= 0.05f;
		}

		CL_String text2 = cl_format("%1 bonus bananas", score);
		CL_Size size2 = fnt_clansoft.get_text_size(gc, text2);
		fnt_clansoft.draw_text(gc, gc.get_width() - 20 - size2.width, 30, text2);

		window.flip(0);
		frameratecounter.frame_shown();

		if (map->get_eggs_left() == 0) break; // level completed

		CL_KeepAlive::process();
	}

}


void World::on_key_down(const CL_InputEvent &key, const CL_InputState &state)
{
	if (key.id == CL_KEY_ESCAPE) quit = true;
}
