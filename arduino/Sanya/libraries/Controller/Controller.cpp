#include "Arduino.h"
#include "Controller.h"

// Constructor
Controller::Controller(int xDirectionPin, int xSpeedPin, int yDirectionPin, int ySpeedPin)
{
	// Assign private variables
	_xDirectionPin		= xDirectionPin;
	_xSpeedPin			= xSpeedPin;
	_yDirectionPin		= yDirectionPin;
	_ySpeedPin			= ySpeedPin;

	// Default speed values
	_xDefaultSpeedLeft	= 180;
	_xDefaultSpeedRight	= 170;
	_yDefaultSpeedUp 	= 190;
	_yDefaultSpeedDown	= 75;

	_yFreezeSpeed		= 85;

	// Set pin modes
	pinMode(_xDirectionPin, OUTPUT);
	pinMode(_xSpeedPin, 	OUTPUT);
	pinMode(_yDirectionPin, OUTPUT);
	pinMode(_xSpeedPin, 	OUTPUT);
}


// Moving
void Controller::moveRight() { this->moveRight(_xDefaultSpeedLeft); }
void Controller::moveRight(int speed)
{
	digitalWrite(_xDirectionPin, HIGH);
	analogWrite(_xSpeedPin, speed);
}

void Controller::moveLeft() { this->moveLeft(_xDefaultSpeedRight); }
void Controller::moveLeft(int speed)
{
	digitalWrite(_xDirectionPin, LOW);
	analogWrite(_xSpeedPin, speed);
}

void Controller::moveUp() { this->moveUp(_yDefaultSpeedUp); }
void Controller::moveUp(int speed)
{
	Serial.print("SPEED: ");
	Serial.println(speed);
	digitalWrite(_yDirectionPin, LOW);
	analogWrite(_ySpeedPin, speed);
}

void Controller::moveDown() { this->moveDown(_yDefaultSpeedDown); }
void Controller::moveDown(int speed)
{
	Serial.print("SPEED: ");
	Serial.println(speed);
	digitalWrite(_yDirectionPin, HIGH);
	analogWrite(_ySpeedPin, speed);
}


// Freezing
void Controller::xAxisStop() { this->xAxisStop(0); }
void Controller::xAxisStop(int pauseTime)
{
	analogWrite(_xSpeedPin, 0);
	
	if (pauseTime != 0) {
		delay(pauseTime);
	}
}

void Controller::yAxisStop() { this->yAxisStop(0); }
void Controller::yAxisStop(int pauseTime)
{
	// Give a low pulse up so it doesn't fall down
	analogWrite(_ySpeedPin, _yFreezeSpeed);

	if (pauseTime != 0) {
		delay(pauseTime);
	}
}

// Freeze both
void Controller::freezeMotors(int pauseTime)
{
	this->yAxisStop();
	this->xAxisStop();
	
	if (pauseTime != 0) {
		delay(pauseTime);
	}
}

// Stop all (shut down)
void Controller::stopAll()
{
	analogWrite(_xSpeedPin, 0);
	analogWrite(_ySpeedPin, 0);
}

// For picking up packages
void Controller::pickUp()
{
	Serial.println("SANYA: PICKING UP...");
	this->moveUp();
	delay(350);
	yAxisStop();
	Serial.println("SANYA: IN PICKUP POSITION");
}

//For dropping packages in bins
void Controller::drop()
{
	Serial.println("SANYA: DROPPING...");
	this->moveDown();
	delay(350);
	yAxisStop();
	Serial.println("SANYA: IN DROP POSITION");
}
// Manual control
void Controller::manual(char direction)
{
	switch (direction) {
	    case 'R': // Right
	        this->moveRight();
	        break;
	    case 'L': // Left
	        this->moveLeft();
	        break;
	    case 'U': // Up
	        this->moveUp();
	        break;
	    case 'D': // Down
	        this->moveDown();
	        break;
	    case 'P': // Pause
	        this->stopAll();
	        this->yAxisStop();
	        break;
	    case 'S': // Stop
	        this->stopAll();
	        break;
	    default:
	        this->stopAll();
	        break;
    }
}

// Test path
void Controller::test()
{
	this->moveRight();
    delay(3500);

    this->stopAll();
    this->moveUp();
    delay(2000);

    this->stopAll();    
    this->moveLeft();
    this->yAxisStop();
    delay(2500);

    this->stopAll();
    this->moveRight();
    this->moveDown();
    delay(3000);

    this->stopAll();
    this->moveLeft();
    delay(2000);

    this->stopAll();
    this->moveUp();
    delay(2300);

    this->moveDown();
    delay(1000);

    this->yAxisStop();
    delay(1000);
    
    this->moveDown(5);
    delay(2000);
}