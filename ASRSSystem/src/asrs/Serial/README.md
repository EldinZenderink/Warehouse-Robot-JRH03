Custom Serial Controller
-----------
This is the readme for the custom Serial controller, for now only compatible with Sanya & Erica Arduino. 

### Required
- Python 3.4 installed : [Download Here](https://www.python.org/downloads/)
- 2 Arduino's, responding every time after a message is send to the Arduino's with SANYA: or ERICA: .

### How to use this?

- You need to include Client.java and Arduino.Java in your project.
- You need to send the class(for example: `YourClass`) where you initiate Client.java to the Client class using `this`
- In the Client class, on line 34, you need to change `final YourClassName yourClass`
- In `YourClass` you need to initiate the Arduino class, passing these parameters (Client, String, String, String): `client`, `Com Port`, `Baud Rate`, `Arduino Name (Sanya or Erica in this case)`
- In `YourClass` you need to create these methods: `public void messageFromSanya(String m)` && ` public void messageFromErica(String m)`, these methods are triggers, every time either of the arduino sends something, these methods will run.
- To send/say something to one of the arduino's you need to call either `sendMessageToSanya` or `sendMessageToErica` from the Arduino class.

See **YourClass.java** for a example!

### Possible Issues / Known Issues

- When sending multiple messages to either of the arduino's, a loop starts running checking if either of the arduino responded back to a previous message, this while loop pauses everything that does not run in a thread.
- No automatic reconnect to tcp server(read **About**)
- No automatic reconnect to serial ports (everything should be ready before running this)
- This cannot be run from the main class, since you cannot pass the main class to the Client class.


#### About
Why all this you may ask? The java libraries for Serial Communication are badly optimized and do not run properly on windows 8(.1). For this we use python tcp server for the Serial communication, Java here is the client, which can send text to the python server, which passes them to the defined serial ports. Schematicly it looks like this:  
`Java TCP Client <-> Python TCP Server <-> Arduino(s) Serial Port(s)`

Written by [@EldinZenderink](https://github.com/EldinZenderink)
