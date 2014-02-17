/*
  Sends sensor data to Arduino
*/
 
#include <MeetAndroid.h>

MeetAndroid meetAndroid;

char string_head[] = "{ \"sensor_id\":'Team1\", \"sensor_type\":\"light\", \"sensor_value\":";
char string_tail[] = " }";


void setup()  
{
  // use the baud rate your bluetooth module is configured to 
  // not all baud rates are working well, i.e. ATMEGA168 works best with 57600
  Serial1.begin(9600); 
  // meetAndroid.registerFunction(transmit, 's');  

  // we initialize analog pin 5 as an input pin
  pinMode(A0, INPUT);
  

}

void loop()
{
    if(Serial1.available())
    {
        // read out whatever we got
        Serial1.read();
        
        // read sensor value and add to string
        float reading = analogRead(A0);
      
        char buf [10];
        sprintf (buf, "%.2f", reading);
      
        char myBigArray[128];
        myBigArray[0] = '\0';
        strcat(myBigArray, string_head);
        strcat(myBigArray, buf);
        strcat(myBigArray, string_tail);
        
        // read out whatever we got
        Serial1.read();
        
        // send out data
        meetAndroid.send(myBigArray);
        meetAndroid.flush();
        delay(250);
        
        // read out whatever we got
        Serial1.read();
    }
}



