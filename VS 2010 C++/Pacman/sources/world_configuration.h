#ifndef WORLD_CONFIGURATION
#define WORLD_CONFIGURATION

#if _MSC_VER > 1000
#pragma once
#endif

#include <functional>

class WorldConfiguration {
public:
	struct map {
	public:
		map(const std::tr1::function<int()>& _getWidth, const std::tr1::function<int()>& _getHeight, const std::tr1::function<int()>& _getNumConnections);
		
		int getWidth() const;
		int getHeight() const;
		int getNumConnections() const; 
	private:
		std::tr1::function<int()> _getWidth;
		std::tr1::function<int()> _getHeight;
		std::tr1::function<int()> _getNumConnections;
	};

	struct pacman {
	public:
		pacman(const std::tr1::function<int()>& _getWidth, const std::tr1::function<int()>& _getHeight, const std::tr1::function<int()>& _getVision);
		
		int getWidth() const;
		int getHeight() const;
		int getVision() const;
	private:
		std::tr1::function<int()> _getWidth;
		std::tr1::function<int()> _getHeight;
		std::tr1::function<int()> _getVision;
	};

	struct ghost {
	public:
		ghost(const std::tr1::function<int(int)>& _getWidth, const std::tr1::function<int(int)>& _getHeight, const std::tr1::function<int()>& _getNum);
		
		int getWidth(int index) const;
		int getHeight(int index) const;
		int getNum() const;
	private:
		std::tr1::function<int(int)> _getWidth;
		std::tr1::function<int(int)> _getHeight;
		std::tr1::function<int()> _getNum;
	};

	WorldConfiguration(const struct map& map, const struct pacman& pacman, const struct ghost& ghost);


	struct map map;
	struct pacman pacman;
	struct ghost ghost;
private:
	

};

#endif