#define PULSE 2
#define DIR 3
#define EN 4

String command; // command from serial
double angle; // rorate angle
double pos;

int number; // rotate number
int start_flag; //used to determine

void setup()
{
    pinMode(PULSE, OUTPUT);
    pinMode(DIR, OUTPUT);
    pinMode(EN, OUTPUT);
    
    Serial.begin(9600);
    Serial.print("Stepper Motor Online! Enter a position between -180 and 180 (in degrees): ");
    Serial.println("");
    
    digitalWrite(PULSE, LOW);
    digitalWrite(DIR, LOW);
    digitalWrite(EN, LOW);
    
    pos = 0;
    start_flag = 0;
}

void loop() { 
    
    //reset angle and number to initial position
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
      if(abs(angle)>180) {
        Serial.println("Out of range!");
      } else {
        angle = angle - pos;
        number = angle/0.18;
        rotate(number);
        pos = pos + number * 0.18;
        Serial.println("Done!...");
      }
      
      Serial.print("Antenna position: ");
      Serial.print(pos);
      Serial.print(" degrees");
      Serial.println("");
      Serial.print("Enter a new position: ");
      //reset start flag
      start_flag = 0;
    }
}

// rotate by step numbers
void rotate(int number)
{
    if(number >= 0)
      digitalWrite(DIR, LOW);
    else {
      digitalWrite(DIR, HIGH);
      number = -number;
    }
    
    for(int i=0; i<number; i++) {
      digitalWrite(EN, HIGH);
      digitalWrite(PULSE, LOW);
      delay(1);
      digitalWrite(PULSE, HIGH);
      delay(1);
      digitalWrite(PULSE, LOW);
      digitalWrite(EN, LOW);
    }
}
