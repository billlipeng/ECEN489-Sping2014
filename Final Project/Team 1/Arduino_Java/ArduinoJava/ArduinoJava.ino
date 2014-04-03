int led = 13; // LED connected to digital pin 13
int pts = 2;  // Powertail Switch 2 connected to digital pin 2
int recv = 0; // byte received on the serial port
 
void setup() {
  // initialize onboard LED (led), Powertail (pts) and serial port
  pinMode(led, OUTPUT);
  pinMode(pts, OUTPUT);
  Serial.begin(9600);
}
 
void loop()
{
  // if serial port is available, read incoming bytes
  if (Serial.available() > 0) {
    recv = Serial.read();
 
    // if 'y' (decimal 121) is received, turn LED/Powertail on
    // anything other than 121 is received, turn LED/Powertail off

/*    if (recv == 121){
      digitalWrite(led, HIGH);
      digitalWrite(pts,LOW);
    } else {
      digitalWrite(led, LOW);
      digitalWrite(pts,HIGH);
    }
*/     
    // confirm values received in serial monitor window
    Serial.print("--Arduino received: ");
    Serial.println(recv);
  }
}
