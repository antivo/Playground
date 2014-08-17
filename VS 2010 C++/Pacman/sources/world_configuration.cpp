#include "precomp.h" 
#include "world_configuration.h"

// map

WorldConfiguration::map::map(const std::tr1::function<int()>& _getWidth, const std::tr1::function<int()>& _getHeight, const std::tr1::function<int()>& _getNumConnections):
	_getWidth(_getWidth), _getHeight(_getHeight), _getNumConnections(_getNumConnections)
{}

int WorldConfiguration::map::getWidth () const {
	return this->_getWidth();
}

int WorldConfiguration::map::getHeight () const {
	return this->_getHeight();
}

int WorldConfiguration::map::getNumConnections () const {
	return this->_getNumConnections();
}


// pacman

WorldConfiguration::pacman::pacman(const std::tr1::function<int()>& _getWidth, const std::tr1::function<int()>& _getHeight, const std::tr1::function<int()>& _getVision):
	_getWidth(_getWidth), _getHeight(_getHeight), _getVision(_getVision)
{}

int WorldConfiguration::pacman::getWidth () const {
	return this->_getWidth();
}

int WorldConfiguration::pacman::getHeight () const {
	return this->_getHeight();
}

int WorldConfiguration::pacman::getVision () const {
	return this->_getVision();
}

// ghost

WorldConfiguration::ghost::ghost(const std::tr1::function<int(int)>& _getWidth, const std::tr1::function<int(int)>& _getHeight, const std::tr1::function<int()>& _getNum) :
	_getWidth(_getWidth), _getHeight(_getHeight), _getNum(_getNum)
{}

int WorldConfiguration::ghost::getWidth (int i) const {
	return this->_getWidth(i);
}

int WorldConfiguration::ghost::getHeight (int i) const {
	return this->_getHeight(i);
}

int WorldConfiguration::ghost::getNum() const {
	return this->_getNum();
}

// world configuration

WorldConfiguration::WorldConfiguration(const struct WorldConfiguration::map& map, const struct WorldConfiguration::pacman& pacman, const struct WorldConfiguration::ghost& ghost):
	map(map), pacman(pacman), ghost(ghost)
{}
	