/*=========================================================
Project 3 Step Motor Controller

=========================================================*/

const int PULSE = 2;
const int DIR = 3;
const int EN = 4;
String command; // command from serial
int angle; // rorate angle
int number; // rotate number
double dirct = 0;

void setup()
{
    pinMode(PULSE, OUTPUT);
    pinMode(DIR, OUTPUT);
    pinMode(EN, OUTPUT);
    Serial.begin(9600);
    Serial.print("Step Motor Online...");
    digitalWrite(PULSE, LOW);
    digitalWrite(DIR, LOW);
    digitalWrite(EN, LOW);
}

void loop()
{ 
    number = 0;
    while (Serial.available()) {
      char c = Serial.read();  //gets one byte from serial buffer
      command += c; //makes the string readString
      delay(2);  //slow looping to allow buffer to fill with next character
    }
    if (command.length() >0) {
      Serial.println(command);  //so you can see the captured string 
      angle = command.toInt();  //convert readString into a number
      command = "";
    }
    if(number){
      if(abs(number)<180)
      {
        
        rotate(number);
        dirct = dirct + number * 0.18;
      }
    }
}

// rotate by step numbers
void rotate(int number)
{
    if(number >= 0)
      digitalWrite(DIR, LOW);
    else
    {
      digitalWrite(DIR, HIGH);
      number = -number;
    }
    
    for(int i=0; i<number; i++)
    {
      digitalWrite(EN, HIGH);
      digitalWrite(PULSE, LOW);
      delay(1);
      digitalWrite(PULSE, HIGH);
      delay(1);
      digitalWrite(PULSE, LOW);
      digitalWrite(EN, LOW);
    }

}
