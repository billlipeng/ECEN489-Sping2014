const int ledPin = 13;
String readString;
  int stepsRot = -1;
  int angle = -1;
  
const int EN = 8;
const int DIR = 7;
const int PULSE = 6;
// the setup() method runs once, when the sketch starts

void setup() {
  // initialize the digital pin as an output.
  pinMode(EN, OUTPUT);
  pinMode(DIR, OUTPUT);
  pinMode(PULSE, OUTPUT);
  digitalWrite(DIR, HIGH);
  digitalWrite(EN, LOW);
  
    Serial.begin(9600);
 // Serial.begin(9600);
  //Serial.write(100);
}

// the loop() methor runs over and over again,
// as long as the board has power

void loop() {
     
  while (Serial.available()) {
    char c = Serial.read();  //gets one byte from serial buffer
    readString += c; //makes the string readString
    delay(2);  //slow looping to allow buffer to fill with next character
  }
  if (readString.length() >0) {
    Serial.println(readString);  //so you can see the captured string 
    angle = readString.toInt();  //convert readString into a number
    readString = "";
  }
  
  digitalWrite(EN, LOW);
  //Serial.write(100);
  if(angle != -1)
  {
    int i;
    
    if (angle > 0)
      digitalWrite(DIR, HIGH);
    else
      digitalWrite(DIR, LOW);
      
    stepsRot = (int) (((float)angle)/(360.0/550.0));
    Serial.println(stepsRot);
    rotate(angle);
    /*for (i = 0; i < abs(stepsRot); i++)
    {
      digitalWrite(EN, HIGH);
      digitalWrite(PULSE, LOW);   // set the LED on
      delay(10);                  // wait for a second
      digitalWrite(PULSE, HIGH);    // set the LED off
      delayMicroseconds(100);   
      digitalWrite(PULSE, LOW);      // wait for a second
      digitalWrite(EN, LOW);
    }*/
    stepsRot = -1;
    angle = -1;
  }
}


// rotate by step numbers
void rotate(int number)
{
    //if(number >= 0)
    //  digitalWrite(DIR, LOW);
    //else
    //{
    //  digitalWrite(DIR, HIGH);
    //  number = -number;
    //}
    
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
