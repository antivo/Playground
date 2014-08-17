#include "precomp.h"
#include "application.h"
#include "world_configuration.h"
#include "world.h"

#include <ctime>

int Application::main(const std::vector<CL_String> &args)
{
	

	//int h = 15, w = 15;

	//int v = 2;

	//int g = 3;
	

	int h = 7, w = 7;

	int v = 1;

	int g = 1;

	

	for(int i = 0; i < 10; ++i) {
		int kx = 0;
		int ky = 0;

		int x[] = {1, 1, (w-2), (w-2)};
		int y[] = {1, (h-2), 1, (h-2)};
	
		struct WorldConfiguration::map m(
			[&]()-> int {return h;},
			[&]()-> int {return w;},
			[]()-> int {return 0;});
		struct WorldConfiguration::pacman pacman(
			[&]()-> int {return h/2;},
			[&]()-> int {return w/2;},
			[&]()-> int {return v;});
		struct WorldConfiguration::ghost ghost(
			[&kx, &x](int i)-> int {return x[++kx % 4];},
			[&ky, &y](int i)-> int {return y[++ky % 4];},
			[&]()-> int {return g;});


		CL_DisplayWindow window("IMS Pacman", 1024, 768, true, true);

		CL_ResourceManager resources;
		resources.load("pacman.xml");

		World world(&resources, window);

		// Connect the Window close event
		CL_Slot slot_quit = window.sig_window_close().connect(&world, &World::on_quit);

		srand(time(NULL));



		WorldConfiguration configuration(m, pacman, ghost);




		// Enter the amazing pacman world
		world.run(window, configuration);


		h = h + 4;
		w = w + 4;
			++v;

			++g;


	}

		return 0;
}
