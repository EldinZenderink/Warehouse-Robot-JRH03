#ifndef Finder_h
#define Finder_h

#include "Arduino.h"
#include "Controller.h"

class Finder
{
private:
	// Robot object
	Controller robot;

	// Position detection
	int xPosition, yPosition;
	int coordsX, coordsY;

	// Variables for algorithm
	bool xDone, xDetected, xFound, moveRight, arrivedAtX;
	bool yDone, yDetected, yFound, moveUp, arrivedAtY;

	// Variables for logistics
	bool atPosition;
	bool completedRoutine;
	int cNumber;

	// Variables for calibration
	int _xSensor, _ySensor;
	int xVal, yVal;

	//previous direction
	String previousDirection;
	bool oneTimeDelay;
	
public:
	// Constructor
	Finder(Controller robot);

	// Setting the remaining variables
	void setValues(int coordsX, int coordsY);

	// Main controller logic
	void loopBegin(int xVal, int yVal);

	// Main movement executions
	void phaseOne();
	void phaseTwo();

	//Bool for chekcing
	bool isXDone();
    bool isYDone();

    // Set current position
    void setX(int x);
    void setY(int y);
};

#endif