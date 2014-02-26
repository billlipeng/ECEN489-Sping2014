#include "MeetAndroid.h"
#include <aJSON.h>
#define ID "team4"
#define TYPE "light"

// these variables are used for light sensor calibrations

double voltage[2] = {0.0};    
double resistance[2] = {0.0};
double lux[2] = {0.0};
float A = -1.08;
float B = 5.42;
boolean calculationsComplete = false;

// these variables are for continuous temp readings
double constantVolt = 0.0;
double constantRes = 0.0;
double constantLux = 0.0;

MeetAndroid meet;

void setup() {
  Serial.begin(9600);
  Serial1.begin(9600); 
  /* 
  Serial.println("Light Sensor Calibration"); 
  Serial.println("Which light source are you using?");
  Serial.println("'b' - bright | 'd' - dark | 'q' - calibrate"); 
  */
  meet.registerFunction(querySensor, 's');
}

void loop() {
  while (!calculationsComplete) {
    while (Serial.available()) {
      char input = (char) Serial.read();
      if (input == 'b') {
        Serial.println("Bright Selected!");
        voltage[0] = readVoltage(voltage[0]);
        resistance[0] = calcResistance(voltage[0]);
      }
      else if (input == 'd') {
        Serial.println("Dark Selected!");
        voltage[1] = readVoltage(voltage[1]);
        resistance[1] = calcResistance(voltage[1]);
      }
      else if(input == 'q') {
        Serial.println("Calibrate Selected!");
        calcCoefficients(resistance, lux, A, B);
        calculationsComplete = true;
      } else {
        Serial.println("Light Sensor Calibration"); 
        Serial.println("Which light source are you using?");
        Serial.println("'b' - bright | 'd' - dark | 'q' - calibrate"); 
      }
    }
  } 
  /*
  // if calibration is complete, then continue on with the rest of the program
  while (calculationsComplete) {
    // these variables are for continuous temp readings
    constantVolt = readVoltage(constantVolt);
    constantRes = calcResistance(constantVolt);
    constantLux = calcLux(constantRes, A, B);
    Serial.print("Voltage: ");
    Serial.println(constantVolt);
    Serial.print("Resistance: ");
    Serial.println(constantRes);
    Serial.print("Lux: ");
    Serial.println(constantLux); 
    delay(1000);
    
    // debug/testing with aJson library...these lines can be removed
    aJsonObject *root;
    root = aJson.createObject();
    aJson.addItemToObject(root, "sensor-id", aJson.createItem(ID));
    aJson.addItemToObject(root, "sensor-type", aJson.createItem(TYPE));
    aJson.addItemToObject(root, "sensor-value", aJson.createItem(constantLux));
    char* string = aJson.print(root);
    if (string !=NULL) {
      Serial.println(string); 
    }
    
    // wait for android signal
    meet.receive();
  } */
  meet.receive();
}

// function that's called when android & arduino connect
void querySensor(byte flag, byte numOfValues) {
    constantVolt = readVoltage(constantVolt);
    constantRes = calcResistance(constantVolt);
    constantLux = calcLux(constantRes, A, B);
    
    Serial.print("Voltage: ");
    Serial.println(constantVolt);
    Serial.print("Resistance: ");
    Serial.println(constantRes);
    Serial.print("Lux: ");
    Serial.println(constantLux); 
    Serial.println();
    
    // create json object using aJson library
    aJsonObject *root;
    root = aJson.createObject();
    aJson.addItemToObject(root, "sensor_id", aJson.createItem(ID));
    aJson.addItemToObject(root, "sensor_type", aJson.createItem(TYPE));
    aJson.addItemToObject(root, "sensor_value", aJson.createItem(constantLux));
    char* jsonString = aJson.print(root);
    if (jsonString !=NULL) {
      Serial.println(jsonString);
      Serial.println(); 
      meet.send(jsonString);
    }
    free(root);
    free(jsonString);
    
}

// reads analogInput and converts raw input into voltage
double readVoltage(double voltage) {
  voltage = analogRead(A4);
  double temp = voltage * (5.0 / 1023.0); 
  voltage = temp; 
  return voltage;
}

// calculates photoresistor resistance value
double calcResistance(double voltage) {
  return (1650.0 / voltage) - 500.0;
}

// calculates lux coefficients 
void calcCoefficients(double resistance[], double lux[], float &A, float &B) {
  lux[0] = 1000.0; // phone light
  lux[1] = 10.0; // hand covered
  double C = 1 / (log10(resistance[0]) - log10(resistance[1]));
  A = C * (log10(lux[0]) - log10(lux[1]));
  B = C * ((-log10(resistance[1]) * log10(lux[0])) + (log10(resistance[0]) * log10(lux[1])));
  Serial.println(A);
  Serial.println(B);  
}

// calculates lux value
double calcLux(double resistance, float A, float B) {
  double temp = A * log10(resistance) + B;  
  return pow(10, temp);
}

