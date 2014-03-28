const int ledPin = 13;
String readString;
  int stepsRot = -1;
  int angle = -1;
// the setup() method runs once, when the sketch starts

void setup() {
  // initialize the digital pin as an output.
  pinMode(0, OUTPUT);
  pinMode(1, OUTPUT);
  pinMode(2, OUTPUT);
  digitalWrite(0, HIGH);
  digitalWrite(2, LOW);
  
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
  
  digitalWrite(2, LOW);
  //Serial.write(100);
  if(angle != -1)
  {
    int i;
    digitalWrite(2, HIGH);
    if (angle > 0)
      digitalWrite(0, HIGH);
    else
      digitalWrite(0, LOW);
      
    stepsRot = (int) (((float)angle)/(360.0/880.0));
    Serial.println(stepsRot);
    for (i = 0; i < abs(stepsRot); i++)
    {
      digitalWrite(1, HIGH);   // set the LED on
      delayMicroseconds(10);                  // wait for a second
      digitalWrite(1, LOW);    // set the LED off
      delay(1);                  // wait for a second
    }
    stepsRot = -1;
    angle = -1;
  }
}

