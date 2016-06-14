/**
* @author Wessel Loth & Eldin Zenderink
* @date   11-05-2015
* @lang   C++
**/

#include <stdio.h>
#include <ctype.h>
#include <Controller.h>
#include <PosParser.h>
#include <Finder.h>

// Robot controller
Controller robot = Controller(4, 5, 7, 6);
Finder finder = Finder(robot);

// Instruction variable
String instruction = "";

// Manual mode
bool manual = false;
char dir;

// Automated retreival
int *coordsX, *coordsY;
bool retreivalMode = false;
int arrSize;
bool debug = true;

// Counters for how many times the robot gets in the zone
int xValueCounter = 0;
int yValueCounter = 0;

//sensor readout:
int yVal;
int xVal;


void setup()
{
  Serial.begin(115200);
  Serial.println("SANYA: READY. WAITING FOR INPUT...");

}

void loop() {
  // Check if there's new instructions
  checkInput();

  xVal = analogRead(A0);
  yVal = analogRead(A1);


  if (retreivalMode)
  {

    //loop for robot control check in finder, also sending sensor values
    finder.loopBegin(xVal, yVal);

    // Main retreival finder
    if(finder.isYDone()){
        robot.stopAll();
        retreivalMode = false;
        Serial.println("SANYA: TRUE");
    } else {
      if(finder.isXDone()){
          finder.phaseTwo();
      } else  {
          finder.phaseOne();
      }

    }
    

    if (debug) {
      Serial.print("xVal: ");Serial.println(xVal);
      Serial.print("yVal: ");Serial.println(yVal);
    }
    
  }
  else if (manual)
  {
    robot.manual(dir);
  }
  else if (instruction == "TEST")
  {
    resetAll();
    robot.test();
  }
  else if (instruction == "STOP")
  {
    robot.stopAll();
    delay(1000);
  } else if (instruction == "VALUES")
  {
    Serial.print("X: ");Serial.println(xVal);
    Serial.print("Y: ");Serial.println(yVal);
    delay(10);
    if (xVal >= 10) {
      Serial.print("X is under 10!  -  ");
      ++xValueCounter;
      Serial.print(xValueCounter);
      Serial.println(" many times.");
    } else if (yVal >= 650) {
      Serial.print("Y is above 650!  -  ");
      ++yValueCounter;
      Serial.println(yValueCounter);
      Serial.println(" many times.");
    }
  }
}

//resets everything, bringing robot back to the beginning
void resetAll() {
  robot.moveLeft(255);
  robot.moveDown(160);
  manual = false;
  retreivalMode = false;
  instruction = "";
  dir = 'S';
  finder = new Finder();
  delay(3500);

  robot.stopAll();
  Serial.println("SANYA: RESTARTED!");
  Serial.println("SANYA: TRUE");
}

// Check the serial monitor for new commands
void checkInput()
{
  if (Serial.available()) {
    String serial = Serial.readString();
    
    if (serial == "HELLO") {
      Serial.println("SANYA: HELLO");
    } else if (serial == "STOP") {
      Serial.println("SANYA: STOPPING");
      instruction = "STOP";
      dir = 'S';
      retreivalMode = false;
    } else if (serial == "PICK UP") {
      robot.pickUp();
      robot.stopAll();
      delay(3000);
    }  else if (serial == "DROP") {
      robot.drop();
      robot.stopAll();
      finder.setY(0);
      delay(3000);
    }  
    else if (serial == "MANUAL") {
      dir = 'S';
      if (!manual) {
        Serial.println("SANYA: TURNING ON MANUAL CONTROLS");
        manual = true;
      } else {
        Serial.println("SANYA: MANUAL MODE IS ALREADY ON. SEND `MANUAL OFF` TO TURN IT OFF");
      }
    }
    else if (serial == "MANUAL OFF")
    {
      if (manual) {
        Serial.println("SANYA: TURNING OFF MANUAL MODE");
        manual = false;
      } else {
        Serial.println("SANYA: MANUAL MODE IS ALREADY TURNED OFF. SEND `MANUAL` TO TURN IT ON");
      }
    } 
    else if (serial.substring(0, 4) == "POS_")
    {
      
      if ((isdigit(serial[4])) && (isdigit(serial[5])))
      {
        Serial.println("SANYA: RECEIVED POSITION. START MOVING");
        int x = (int) serial[4] - 48;
        int y = (int) serial[5] - 48;
        retreivalMode = true;
        finder.setValues(x, y);
      } else {
        Serial.println("SANYA: SOMETHING WENT WRONG WHILE PARSING: " + serial);
      }
    }
    else if (serial == "TEST") {
       Serial.print("Y: ");Serial.println(yVal);
       if(yVal < 350){
         Serial.println("level detected");
       }
       delay(30);
    }
    else if (serial == "RIGHT" || serial == "D")
    {
      Serial.println("SANYA: GOING RIGHT");
      dir = 'R';
    }
    else if (serial == "LEFT" || serial == "A")
    {
      Serial.println("SANYA: GOING LEFT");
      dir = 'L';
    }
    else if (serial == "UP" || serial == "W")
    {
      Serial.println("SANYA: GOING UP");
      dir = 'U';
    }
    else if (serial == "DOWN" || serial == "S")
    {
      Serial.println("SANYA: GOING DOWN");
      dir = 'D';
    }
    else if (serial == "PAUSE" || serial == "R")
    {
      Serial.println("SANYA: PAUSING");
      dir = 'P';
    }
    else if (serial == "RESTART")
    {
      Serial.println("SANYA: RESTARTING...");
      resetAll();
    } else if (serial == "VALUES")
    {
      Serial.println("SANYA: PRINTING VALUES");
      instruction = "VALUES";
    } else {
      Serial.println("SANYA: COMMAND NOT RECOGNIZED - STOPPING");
      Serial.print("SANYA: UNRECOGNIZED COMMAND: ");
      Serial.println(serial);
      instruction = "STOP";
      dir = 'S';
    }
  }
}
