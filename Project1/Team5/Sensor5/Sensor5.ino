
#include <MeetAndroid.h>
#include <aJSON.h>

#define SENSOR_PIN A0

MeetAndroid meetAndroid;
int onboardLed = 13;
double sourceVoltage = 3.8;

void setup()  
{

  Serial1.begin(57600); 
  Serial.begin(57600);
  
  meetAndroid.registerFunction(sensorEvent, 's');
  meetAndroid.registerFunction(sensorEvent, 'S');
  
  pinMode(onboardLed, OUTPUT);
  digitalWrite(onboardLed, HIGH);

}

void loop()
{
  meetAndroid.receive(); // you need to keep this in your loop() to receive events
}

void sensorEvent(byte flag, byte numOfValues)
{
  Serial.println("test");
  float rawVal = analogRead(SENSOR_PIN);
  float sensorVoltage = rawVal*(sourceVoltage/1023);
  
  
  aJsonObject *sensorData;
  sensorData=aJson.createObject();  
  aJson.addItemToObject(sensorData, "sensor-id", aJson.createItem("Sensor 5"));
  aJson.addItemToObject(sensorData, "sensor-type", aJson.createItem("Photo"));
  aJson.addItemToObject(sensorData, "sensor-value", aJson.createItem(sensorVoltage));
  
  char* jsonString = aJson.print(sensorData);
  if (jsonString != NULL)  {
    Serial.println(jsonString);
  }
  
  meetAndroid.send(jsonString);
  
}

