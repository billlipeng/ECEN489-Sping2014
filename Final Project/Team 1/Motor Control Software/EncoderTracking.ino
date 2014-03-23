/*=========================================================
- This example code is written for controlling 
  SD02B Stepper Motor Driver by Cytron Technologies
  through the UART interface.

- SD02B will drive the stepper motor N step, where N is
  defined in the code "steps = 1000". User may replace 1000
  with any number between 0 and 65536.
  
- For more information about SD02B Stepper Motor Driver, 
  please visit www.cytron.com.my
=========================================================*/

void setup()
{
  Serial.begin(9600);
}

void loop()
{
  int steps = 12800;  //set desired step in 16 bits (0-65535)
  char step_H = (steps >> 8) & 0x00FF;	//get the higher 8 bits
  char step_L = steps & 0x00FF;		//get the lower 8 bits
						
  Serial.print('R');    // reset encoder

  Serial.print('T');    // track encoder
  Serial.write(step_H); // send higher 8 bits
  Serial.write(step_L); // send lower 8 bits
  
  Serial.print('O');    // turn on stepper motor

  Serial.print('S');    // set speed
  Serial.write(1);     // to 10

  Serial.print('>');    // rotate counterclockwise

  Serial.print('M');    // set micro-stepping to
  Serial.write(10);      // 1 = no microstepping, so SD02B's 4
                        // leds on/off can be observed easily.

  Serial.print('G');    // run the stepper motor

  while(1);             // loop forever here
}

