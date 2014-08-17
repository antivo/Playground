#include "spoon.h"

Spoon::Spoon(const Placement placement) :
placement(placement), condition(DIRTY) {}

Spoon::Placement Spoon::getPlacement() const {
	return this->placement;
}

Spoon::Condition Spoon::getCondition() const {
	return this->condition;
}

void Spoon::setPlacement(Placement placement) {
	this->placement = placement;
}

void Spoon::setCondition(const Condition condition) {
	this->condition = condition;
}