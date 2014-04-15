/*
Team: 1
Title: Final Project Stepper Motor Control Software
ECEN 489
*/

const int DIR = 3;
const int EN = 4;
const int PULSE = 5;

String command; // command from serial

double angle; // rotate angle
double pos;

int number; // rotate number
int start_flag;


void setup()
{

    pinMode(DIR, OUTPUT);
    pinMode(EN, OUTPUT);
    
    Serial.begin(9600);
    Serial1.begin(9600);
    Serial1.print("Setup complete...");
    

    digitalWrite(DIR, LOW);
    digitalWrite(EN, LOW);
    
    analogWriteFrequency(PULSE, 400);
    analogWrite(PULSE, 255);   
    
    pos = 0;
    start_flag = 0;
}

void loop()
{ 
    angle = 0;
    number = 0;
    
    while (Serial.available()) {
      char c = Serial.read();
      command += c;
      delay(2);
    }
    if(command.length() > 0) {
      Serial1.println(command);
      angle = command.toInt();
      command = "";
      start_flag = 1;
      Serial.flush();
    }    

    if(start_flag){
      if(abs(angle) > 180) {    
        Serial1.println("Out of Range!");
      }
       
      else {
        
        angle = angle - pos;
        Serial.print("Angle: ");
        Serial.println(angle);
        
        number = angle/0.09;
        Serial.print("Number: ");
        Serial.println(number);        
        rotate(-number);
        
        Serial.print("Old Position: ");
        Serial.println(pos);        
        pos = pos + number * 0.09;
        Serial.print("Position: ");
        Serial.println(pos);
        Serial.println("-----------");        
      }
;
        
      Serial1.println("");
      Serial1.print("Antenna position: ");
      Serial1.print(pos);
      Serial1.print(" degrees");
      Serial.flush();
      
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
    
    delay(1);
    
    for(int i=0; i < number; i++)
    {
      digitalWrite(EN, HIGH);
      
      //set PWM Duty Cycle to 1
      analogWrite(PULSE, 255);
      
      // 1 millisecond pulse
      delay(1);
      
      //disable motor
      digitalWrite(EN, LOW);

      
    }
}

int cal_steps(double angle, double pos)
{
    int number = 0;
    if(abs(angle + pos)<180)
      return number = angle/0.18;
    else
      return 0;
}
