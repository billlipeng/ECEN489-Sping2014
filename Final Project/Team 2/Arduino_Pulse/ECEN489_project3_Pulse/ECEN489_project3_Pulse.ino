/*=========================================================
Project 3 Step Motor Controller

=========================================================*/

const int PULSE = 2;
const int DIR = 3;
const int EN = 4;
String command; // command from serial
double angle; // rorate angle
int number; // rotate number
double dirct;
int start_flag;
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
    dirct = 0;
    start_flag = 0;
}

void loop()
{ 
    angle = 0;
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
      start_flag = 1;
    }
    if(start_flag){
      if(abs(angle)>180)
      {
        Serial.println("Angles between -180 ~ 180");
      }
      else{
        angle = angle - dirct;
        number = angle/0.18;
        rotate(number);
        dirct = dirct + number * 0.18;
        Serial.println("Rotate finished...");
      }
      Serial.print("Direction is ");
      Serial.println(dirct);
      start_flag = 0;
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

int cal_steps(double angle, double dirct)
{
    int number = 0;
    if(abs(angle + dirct)<180)
      return number = angle/0.18;
    else
      return 0;
}
