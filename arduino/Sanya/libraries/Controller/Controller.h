#ifndef Controller_h
#define Controller_h

#include "Arduino.h"

class Controller
{
private:
	int _xDefaultSpeedLeft;
	int _xDefaultSpeedRight;
	int _yDefaultSpeedUp;
	int _yDefaultSpeedDown;

	int _xDirectionPin;
	int _yDirectionPin;
	int _xSpeedPin;
	int _ySpeedPin;

	int _moveDownSpeed;

	int _yFreezeSpeed;
	
public:
	// Constructor
	Controller(int xDirectionPin, int xSpeedPin, int yDirectionPin, int ySpeedPin);

	// Moving
	void moveLeft();
	void moveLeft(int speed);

	void moveRight();
	void moveRight(int speed);
	
	void moveUp();
	void moveUp(int speed);
	
	void moveDown();
	void moveDown(int speed);

	// Freezing
	void xAxisStop();
	void xAxisStop(int pauseTime);

	void yAxisStop();
	void yAxisStop(int pauseTime);

	// Freeze both
	void freezeMotors(int pauseTime);

	// Stop all (shut down)
	void stopAll();

	// For picking up packages
	void pickUp();

	// For dropping packages
	void drop();

	// Manual control
	void manual(char direction);

	// Test path
	void test();
};

#endif