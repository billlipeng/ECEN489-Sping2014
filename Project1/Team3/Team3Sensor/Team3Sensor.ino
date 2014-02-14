/*
  PhotoCell
 */

#include <math.h>
#include "MeetAndroid.h"
#include <sstream>

String sensor_id = "team_3_sensor_1";
String sensor_type = "light";

int sensorPin = A0;
double vdc = 3.8;
double r2 = 9800.0;

// String json = "";

MeetAndroid ma;

void setup()  {
  Serial.begin(9600);
  Serial1.begin(9600);
  ma.registerFunction(sendSensorVal, 's');
}

void loop()  {
  ma.receive();
}

long photoRes() {
  double vin = analogRead(sensorPin);
  double Va = vin * (vdc/1023.0);
  double resistance = (vdc*r2)/Va - r2;
  return resistance;
}

double readPhotoRes () {
  // Collect 5 Values
  long res[5];
  for (int i = 0; i < 5; i++) {
    res[i] = photoRes();
    delay(10);
  }
  // Take average of 5 values
  float avgRes = 0;
  for (int i = 0; i < 5; i++) {
    avgRes += res[i];
  }
  return avgRes/5;
}

double lux_conversion() {
	  // double adc_value = analogRead(sensorPin);
  // double voltage = vin * ((double)adc_value/1024.0);
  // double resistance = (r2*vin)/voltage - r2;
  // double illuminance = 255.84 * pow(resistance, -10/9);
  return 0.0;
}

// {
// 	"sensor-id": "team1-sensor1",
// 	"sensor-type": "temperature",
// 	"sensor-value": 123.1
// }
String create_json() {
	double res = readPhotoRes();
  char buffer[6];
  // Serial.println(dtostrf(res, 3, 2, buffer));
	String blah = "10";
	String json = "{ \"sensor-id\": \"team3_sensor1\", \"sensor-type\": \"light\", \"sensor-value\": " + String(dtostrf(res, 3, 1, buffer)) + " }";
	return json;
}

void sendSensorVal(byte flag, byte numOfValues) {
  char message[100];
  create_json().toCharArray(message, 100);
  ma.send(message);
}


