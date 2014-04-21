/*****************************************************
Team: 1
Title: Final Project Stepper Motor Control Software
ECEN 489
******************************************************/

//************************************************************//

//define pins that are connected to Steppper Motor Driver
const int DIR = 3;
const int EN = 4;
const int PULSE = 5;

String command; // command from serial

double angle; // rotate angle
double pos;

int number; // rotate number
int start_flag;

//************************************************************//

void setup()
{
	//digital output pins
    pinMode(DIR, OUTPUT);
    pinMode(EN, OUTPUT);
    
    //configure serial
	//Serial -> internal communication handled here (want to keep data we are passing around clear)
	//Serial1 -> reserved for debugging and/or verification output
	Serial.begin(9600);
    Serial1.begin(9600);

	//initialize digital pins
    digitalWrite(DIR, LOW);
    digitalWrite(EN, LOW);
    
	//configure PULSE pin for PWM control
    analogWriteFrequency(PULSE, 400);	//pulse frequency
    analogWrite(PULSE, 255);   		//duty cycle represented from 0-255
    
    pos = 0;
    start_flag = 0;
}

//************************************************************//

void loop()
{ 
    //initialize
	angle = 0;
    number = 0;
    
	//build command string from user input
    while (Serial.available()) {
      char c = Serial.read();
      command += c;
      delay(2);
    }
	
	//convert serial string to int
    if(command.length() > 0) {
      Serial1.println(command);
      angle = command.toInt();
      command = "";
      start_flag = 1;
      Serial.flush();
    }    

	//start checking user input to determine course of action	
    if(start_flag) {
	  
	  // range of motion is between -180 and 180 degrees
	  // server will only send values within this range
      if(abs(angle) > 180) {    
        
		Serial1.println("Out of Range!");	//just in case...
      }
       
      else {
        
		//new angle calculation using memory of last (current) position
        angle = angle - pos;
		       
		//calculate number of steps (pulses)
        number = angle/0.09;
		
		//rotate by number of steps
        rotateStepper(number);
                
		//calculate new position
        pos = pos + number * 0.09;

//************************************************************//		
//debugging outputs, uncomment and monitor as needed		
//        Serial.print("Angle: ");
//        Serial.println(angle);		
//        Serial.print("Number: ");
//        Serial.println(number);        
//        Serial.print("Position: ");
//        Serial.println(pos);
//        Serial.println("-----------");		
 //************************************************************//      
      }
;
//************************************************************//        
//      Serial1.println("");
//      Serial1.print("Antenna position: ");
//      Serial1.print(pos);
//      Serial1.print(" degrees");
//************************************************************//

      Serial.flush();	//flush to prepare for new data
      
      start_flag = 0;	//finished with process, reset start flag
    }
}

//************************************************************//
//function to determine which direction in which to rotate 
//and rotate by number of steps calculated in main
void rotateStepper(int number)
{
    if(number >= 0)	{
		digitalWrite(DIR, LOW);	//set direction to CW
    } 
	else {
		digitalWrite(DIR, HIGH); //set direction to CCW
		number = -number;		//might be redundant, but since direction 
								//is controlled by state of DIR pin, need a positive step number
    }

	//small delay before 
    delay(1);
    
    for(int i=0; i < number; i++)
    {
      digitalWrite(EN, HIGH);
      
      //set PWM Duty Cycle to 1
      analogWrite(PULSE, 255);
      
      // 1 millisecond pulse length
      delay(1);
      
      //disable motor -- necessary to avoid erratic behavior
      digitalWrite(EN, LOW);

    }
}
//****************************END********************************//