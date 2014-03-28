String readString;

void setup()
{
  Serial1.begin(9600);

  
  Serial.begin(9600);
// Uncomment the code below if you've forgot what's the current 
// baudrate set to SD02B.
  Serial.print('U');  // set motor controller baudrate to
  Serial.write(1);    // 9600bps
  Serial.begin(19200);
  Serial.print('U');  // set motor controller baudrate to
  Serial.write(1);    // 9600bps
  Serial.begin(38400);
  Serial.print('U');  // set motor controller baudrate to
  Serial.write(1);    // 9600bps
  Serial.begin(57600);
  Serial.print('U');  // set motor controller baudrate to
  Serial.write(1);    // 9600bps
  Serial.begin(9600); // use to communicate 9600 after this  */

}

  int flag =1;
  unsigned int steps =0;
  int stepsRot = -1;
void loop()
{
   while (Serial.available()) {
    char c = Serial.read();  //gets one byte from serial buffer
    readString += c; //makes the string readString
    delay(2);  //slow looping to allow buffer to fill with next character
  }
  if (readString.length() >0) {
    Serial.println(readString);  //so you can see the captured string 
    stepsRot = readString.toInt();  //convert readString into a number
    readString = "";
  }

  if(stepsRot != -1)
  {
    //flag = 1;
    //Serial.println(stepsRot);

    Serial1.print('R');
    Serial1.flush();
    Serial1.print('T');
    Serial1.flush();
    char send1 = ((abs(stepsRot)) >> 8) & 0x00FF;
    Serial1.write(send1);
    Serial1.flush();
    send1 = (abs(stepsRot)) & 0x00FF;
    Serial1.write(send1);
    Serial1.flush();
    Serial1.print('O');  // turn on stepper motor
    Serial1.print('S');  // set speed
    Serial1.write(2);   // to 1
    if (stepsRot > 0)
      Serial1.print('>');  // rotate counterclockwise
    else
      Serial1.print('<');  // rotate clockwise
    Serial1.print('M');  // set micro-stepping to
    Serial1.write(2);   // 10
    Serial1.print('G');  // run the stepper motor
    
    
 /* while(steps < stepsRot)
  {
    Serial1.print('E');
    steps = Serial1.read();
    steps = steps << 8;
    delay(2);
    steps += Serial1.read();
    Serial.println(steps);
  }*/
  stepsRot = -1;
  steps = 0;

    
/*    if (steps >= 10)
    {
        Serial1.print('F');
        flag = 0;
        stepsRot = -1;
    }
  }*/
}
  //delay(3000);
}

