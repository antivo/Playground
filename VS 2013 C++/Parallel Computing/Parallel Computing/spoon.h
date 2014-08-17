#ifndef SPOON
#define SPOON

class Spoon {
public:
	enum Placement {
		ME, NEIGHBOUR
	};

	enum Condition {
		DIRTY, CLEAN
	};

	Spoon(const Placement inPossession);

	Placement getPlacement() const;
	Condition getCondition() const;

	void setPlacement(const Placement clean);
	void setCondition(const Condition condition);

private:
	Placement placement;
	Condition condition;
};


#endif