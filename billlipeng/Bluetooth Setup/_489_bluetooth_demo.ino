#include <SoftwareSerial.h>

SoftwareSerial Bluetooth_Serial(04, 05); // RX, TX

/*************************Public Part**********************/
unsigned long time;

/*************************DTH Part*************************/
#include "DHT.h"
#define PIN_DHT_0 A0  //temperature and humidity sensor 0
#define PIN_DHT_1 A2  //temperature and humidity sensor 2
#define DHTTYPE DHT22   // DHT 22  (AM2302)

/***********************Motion Part************************/
#define PIN_MOTION 2 //Use pin 2 to receive the signal from the module 

/***********************Light Part*************************/
#include <math.h>
float Rsensor; //Resistance of sensor in K

DHT dht_0(PIN_DHT_0, DHTTYPE);
DHT dht_1(PIN_DHT_1, DHTTYPE);
void setup() 
{
    Bluetooth_Serial.begin(57600);
    //Serial.begin(9600); 
    Bluetooth_Serial.println("Bluetooth is connected...");
    Bluetooth_Serial.println("Arduino is online....");
    DHTest_setup();
    Motion_setup();
    Bluetooth_Serial.println("Sensors are online....");
    time = millis();
    Bluetooth_Serial.print("Arduino starts collecting data @(time)");
    Bluetooth_Serial.println(time/1000);
    print_title();
}

void loop() 
{
    Motion();
    DHTest();
    Light();
    Bluetooth_Serial.println("\t");
}
/**********************Public Part*************************/
void print_title()
{
    Bluetooth_Serial.print("Time\t");
    Bluetooth_Serial.print("Motion\t");
    Bluetooth_Serial.print("Humi_0\t");
    Bluetooth_Serial.print("Tep_0\t");
    Bluetooth_Serial.print("Humi_1\t");
    Bluetooth_Serial.print("Tep_1\t");
    Bluetooth_Serial.print("Ligt_rd\t");
    Bluetooth_Serial.println("Ligt_R\t");
}


/*************************DTH Part*************************/
void DHTest_setup()
{
    dht_0.begin();
    dht_1.begin();
}
void DHTest()
{
    // Reading temperature or humidity takes about 250 milliseconds!
    // Sensor readings may also be up to 2 seconds 'old' (its a very slow sensor)
    float  h0 = dht_0.readHumidity(),
           t0 = dht_0.readTemperature(),
           h1 = dht_1.readHumidity(),
           t1 = dht_1.readTemperature();
    
    // check if returns are valid, if they are NaN (not a number) then something went wrong!
    if (isnan(t0) || isnan(h0) || isnan(t1) || isnan(h1)) 
    {
        Bluetooth_Serial.println(0);
        Bluetooth_Serial.println("\t\t\t\t");//indicate error
    } 
    else 
    {
        Bluetooth_Serial.print(h0);//Humidity
        Bluetooth_Serial.print("\t");
        Bluetooth_Serial.print(t0);//temperature
        Bluetooth_Serial.print("\t");
        Bluetooth_Serial.print(h1);//Humidity
        Bluetooth_Serial.print("\t");
        Bluetooth_Serial.print(t1);//temperature
        Bluetooth_Serial.print("\t");
    }
}
/***********************Motion Part************************/
void Motion_setup()
{
    pinMode(PIN_MOTION, INPUT);
}

void Motion()
{
    int cnt_detected = 0;
    int sensorValue = digitalRead(PIN_MOTION);
    time = millis();
    while(time%1000 != 0)
    {
        if(sensorValue == HIGH)
            cnt_detected = cnt_detected + 1;  // Detected
        delayMicroseconds(1000);
        time = millis();
    }
    Bluetooth_Serial.print(time/1000);
    Bluetooth_Serial.print("\t");
    if( cnt_detected >2 )
            Bluetooth_Serial.print(1);  // Detected
    else
            Bluetooth_Serial.print(0);  // Undetected
    Bluetooth_Serial.print("\t");
    //delay(1);
}
/***********************Light Part*************************/
void Light()
{
  int sensorValue = analogRead(4); 
  Rsensor=(float)(1023-sensorValue)*10/sensorValue;

  // analog read value
  Bluetooth_Serial.print(sensorValue);
  Bluetooth_Serial.print("\t");
  // sensor resistance
  Bluetooth_Serial.print(Rsensor,DEC);//show the ligth intensity on the Bluetooth_Serial monitor;
}
