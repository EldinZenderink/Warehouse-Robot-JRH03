#include "Arduino.h"
#include "Finder.h"

// Constructor
Finder::Finder(Controller robot) : robot(robot)
{
	// Position detection
	xPosition = 0;
	yPosition = 0;

	// Variables for algorithm
	xDone = false;
	xDetected = false;
	xFound = 0;
	moveRight = true;
	arrivedAtX = false;

	yDone = false;
	yDetected = false;
	yFound = 0;
	moveUp = true;
	arrivedAtY = false;

	// Variables for logistics
	atPosition = false;
	completedRoutine = false;

	// Variables for calibration
	_xSensor = 50;
	_ySensor = 100;

	// Always starts of moving right
	previousDirection = "right";

}

// Setting the remaining variables
void Finder::setValues(int coordsX, int coordsY)
{
	this->coordsX = coordsX;
	this->coordsY = coordsY;
	
	xDone = false;
	arrivedAtX = false;
	xDetected = false;

	yDone = false;	
	arrivedAtY = false;
	yDetected = false;

	if(coordsY < yPosition){
		moveUp = false;
		oneTimeDelay = false;
		yPosition = yPosition + 1;
	} else {
		moveUp = true;
	}
}

//sets position 
void Finder::setX(int x){
	xPosition = x;
}

void Finder::setY(int y){
	yPosition = y;
}

// Main controller logic
void Finder::loopBegin(int xVal, int yVal)
{

	this->yVal = yVal;
	this->xVal = xVal;

	if (arrivedAtX) {
		robot.xAxisStop();
	} else if (moveRight) {
		robot.moveRight();
	} else {
		robot.moveLeft();
	}

	if (arrivedAtY) {
		robot.yAxisStop();
	} else if (moveUp && arrivedAtX) {
		robot.moveUp();
		Serial.println("MOVING UP");
	} else if(arrivedAtX) {
		robot.moveDown();
		Serial.println("MOVING DOWN");
	}

	delay(5);

}

// Main algorithm execution
void Finder::phaseOne()
{
	
	// Checks if robot should move left or right.
	if (coordsX < xPosition) {
		moveRight = false;
	} else {
		moveRight = true;
	}

	//Skips everything below if already at location
	if(xPosition != coordsX){
		xDone = false;
		arrivedAtX = false;
	} else {
		xDone = true;
		arrivedAtX = true;
	}

	// Check and adjust for X
	if ((xVal > _xSensor) && (!xDone))
	{
		if (!xDetected)
		{
			//Desides what to do with given direction
			if (moveRight) {
	    		// Add +1 to the X coordinate
	    		Serial.println(previousDirection);
				xPosition++;
				previousDirection = "right";
			} else {
				if(xFound != 0){
					//Remove 1 from the Y coordinate
					xPosition = xPosition - 1;
					previousDirection = "left";
				}
			}

			if (xPosition == coordsX) {
				//making sure its not going to go over the same led/wire again.
				if(previousDirection == "left"){
					robot.moveRight();
					delay(150);
				} else {
					robot.moveLeft();
					delay(150);
				}

				xDone = true;  
				arrivedAtX = true;  

				Serial.println("SANYA: ARRIVED AT X POSITION");
				Serial.print(coordsX, DEC); Serial.print(","); Serial.println(coordsY);
			}

			Serial.print("SANYA: X ");
			Serial.println(xPosition);
			Serial.print("SANYA: TARGET X: ");
			Serial.println(coordsX);

		}
		xFound++;

		// Found a slot
		xDetected = true;
	} else if (!xDone) {
		xDetected = false;
	}

}

void Finder::phaseTwo()
{

	//Checks if not already on location
	if(yPosition != coordsY){
		yDone = false;
		arrivedAtY = false;
	} else {
		yDone = true;
		arrivedAtY = true;
	}

	

	// Check and adjust for Y
	if (yVal > _ySensor && !yDone)
	{
		if (!yDetected)
		{

			// Passed one slot
			yFound++;

			// Add +1 to the Y coordinate

			if (moveUp) {
				yPosition++;
				previousDirection = "up";
			} else {
				yPosition = yPosition - 1;
				previousDirection = "down";
			}
		
			//checks if your on location
			if (yPosition == coordsY) {

				if(!moveUp){
					robot.moveUp();
					delay(200);
				} else {
					int delayTime = yPosition * 80;
					delay(delayTime);
				}

				if(xPosition == 6 || xPosition == 7){
					Serial.println("SANYA: ARRIVED AT BIN");
				}

				yDone = true;
				arrivedAtY = true;

				Serial.println("SANYA: ARRIVED AT Y POSITION");
				Serial.print("SANYA: ARRIVED AT: ");
				Serial.print(coordsX, DEC); Serial.print(","); Serial.println(coordsY);

			} else if(coordsY == 0 && yPosition == 1) {

				delay(200);

				yDone = true;
				arrivedAtY = true;

				Serial.println("SANYA: ARRIVED AT Y POSITION");
				Serial.print("SANYA: ARRIVED AT: ");
				Serial.print(coordsX, DEC); Serial.print(","); Serial.println(coordsY);

				Serial.println("SANYA: TRUE");
			} else {
				delay(500);
			}

			Serial.print("SANYA: Y : ");
			Serial.println(yPosition);
			Serial.print("SANYA: TARGET Y: ");
			Serial.println(coordsY);


		}

		// Found a slot
		yDetected = true;
	} else if (!yDone) {
		yDetected = false;
	}

	
}

//To check outside this class if something is done 
bool Finder::isXDone()
{
	return arrivedAtX;
}

bool Finder::isYDone()
{
	return arrivedAtY;
}