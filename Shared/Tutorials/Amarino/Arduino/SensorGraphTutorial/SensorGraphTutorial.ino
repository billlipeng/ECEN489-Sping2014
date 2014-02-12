/*
  Sends sensor data to Arduino
  (needs SensorGraph and Amarino app installed and running on Android)
*/
 
#include <MeetAndroid.h>

MeetAndroid meetAndroid;
int sensor = 5;
int boardLed = 13; 

void setup()  
{
  // use the baud rate your bluetooth module is configured to 
  // not all baud rates are working well, i.e. ATMEGA168 works best with 57600
  Serial1.begin(9600); 
  
    // register callback functions, which will be called when an associated event occurs.
  meetAndroid.registerFunction(ledOn, 'o');
  meetAndroid.registerFunction(ledOff, 'x');

   pinMode(boardLed, OUTPUT);
  // we initialize analog pin 5 as an input pin
  pinMode(sensor, INPUT);
}

void loop()
{
  meetAndroid.receive(); // you need to keep this in your loop() to receive events
  
  // read input pin and send result to Android
  meetAndroid.send(analogRead(sensor));
  
  // add a little delay otherwise the phone is pretty busy
  delay(100);
}

void ledOn(byte flag, byte numOfValues)
{
	digitalWrite(boardLed, HIGH);
        meetAndroid.send ("LED on");
}

void ledOff(byte flag, byte numOfValues)
{
	digitalWrite(boardLed, LOW);
        meetAndroid.send ("LED off");
}



