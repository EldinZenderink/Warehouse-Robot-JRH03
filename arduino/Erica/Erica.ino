// Motorshield Pins
int binPin = 4;
int binSpeed = 5;
int pickerPin = 6;
int pickerSpeed = 7;

// Global instruction variable
String instruction = "";

void setup() {
  Serial.begin(115200);
  Serial.println("ERICA: INITIALIZING...");
  
  pinMode(binPin, OUTPUT);
  pinMode(pickerPin, OUTPUT);

  pinMode(binSpeed, OUTPUT);
  pinMode(pickerSpeed, OUTPUT);
}

// Check the serial monitor for new commands
void checkInput() {
  if (Serial.available()) {
    String i = Serial.readString();
    
    if (i == "HELLO") {
        Serial.println("ERICA: HELLO");
    } else if (i == "PICK UP") {
        extendPicker();
        delay(750);
           binStop();
        pickerStop();
      Serial.println("ERICA: EXTENDED-1");
    } else if (i == "DROP") {
        extendPicker();
        delay(750);
           binStop();
        pickerStop();
      Serial.println("ERICA: EXTENDED-2");
    } else if (i == "RETRACT") {
        retractPicker();
        delay(750);
        binStop();
        pickerStop();
      Serial.println("ERICA: RETRACTED");
      
    } else if (i == "STOP") {
        Serial.println("ERICA: STOPPING");
        binStop();
        pickerStop();
    } else {
        Serial.println("ERICA: COMMAND NOT RECOGNIZED - STOPPING");
    }
  }
}


// Functions for handling the Picker
void retractPicker() {
  digitalWrite(pickerPin, LOW);
  analogWrite(pickerSpeed, 150);
  digitalWrite(binPin, LOW);
  analogWrite(binSpeed, 150);
}
void extendPicker() {
  digitalWrite(binPin, HIGH);
  analogWrite(binSpeed, 180);
  digitalWrite(pickerPin, HIGH);
  analogWrite(pickerSpeed, 180);
}

// Main Loop
void loop() {
  checkInput();
}

// Stop the motors
void binStop() {
  analogWrite(binSpeed, 0);
}

void pickerStop() {
  analogWrite(pickerSpeed, 0);
}

void stopAll() {
  analogWrite(binSpeed, 0);
  analogWrite(pickerSpeed, 0);
}