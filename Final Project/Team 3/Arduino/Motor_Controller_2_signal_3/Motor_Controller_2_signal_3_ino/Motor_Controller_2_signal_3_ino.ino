const int ledPin = 13;
String readString;
  int stepsRot = -1;
  int angle = -1;
  int positionNow = 0;
  int rotAngle;
  
const int EN = 3;
const int DIR = 4;
const int PULSE = 5;
// the setup() method runs once, when the sketch starts

void setup() {
  // initialize the digital pin as an output.
  pinMode(EN, OUTPUT);
  pinMode(DIR, OUTPUT);
//  pinMode(PULSE, OUTPUT);
  digitalWrite(DIR, HIGH);
  digitalWrite(EN, LOW);
  
  
  analogWriteFrequency(PULSE, 200);
  analogWrite(PULSE, 20);
  
    Serial.begin(9600);
}

// the loop() methor runs over and over again,
// as long as the board has power

void loop() {
  int j = 0;   
  while (Serial.available()) {
    char c = Serial.read();  //gets one byte from serial buffer
    readString += c; //makes the string readString
    //angle = (Serial.read()<< 8*j++);
    delay(2);  //slow looping to allow buffer to fill with next character
  }
  if (readString.length() >0) {
    Serial.println(readString);  //so you can see the captured string 
    angle = readString.toInt();  //convert readString into a number
    readString = "";
  }
  if (angle < -180 or angle > 180)
 {
      Serial.println("Command exceeds the range.");
      Serial.println("Range: -180 to 180"); 
 }
 else
 {
   // Calculating angle to rotate
     rotAngle = angle - positionNow;
        digitalWrite(EN, LOW);
        //Serial.write(100);
        if(angle != -1)
        {
          
          int i;
          
          if (rotAngle > 0)
            digitalWrite(DIR, HIGH);
          else
            digitalWrite(DIR, LOW);
            
          stepsRot = (int) (((float)(abs(rotAngle)))/(360.0/1775.0));
          //Serial.println(rotAngle);
          rotate(stepsRot);
          positionNow = angle;
          stepsRot = -1;
          angle = -1;
        }
 }
 angle = -1;
}


// rotate by step numbers
void rotate(int number)
{
    analogWrite(PULSE, 0);
    for(int i=0; i<number; i++)
    {
      digitalWrite(EN, HIGH);
      analogWrite(PULSE, 20);
      delay(1);
      digitalWrite(EN, LOW);
    }
}
