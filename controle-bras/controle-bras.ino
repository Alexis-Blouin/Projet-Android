#include <Servo.h>

Servo servoBase1;
Servo servoBase2;
Servo servoBras1;
Servo servoBras2;
Servo servoBras3;
Servo servoPince;


int adc_key_val[5] ={50, 200, 400, 600, 800 };
int NUM_KEYS = 5;
int adc_key_in;
int key=-1;
int oldkey=-1;

void setup() {
  Serial.begin(9600);

  // Attachement des broches de l'arduino aux variables Servo.
  servoBase1.attach(2);
  servoBase2.attach(3);
  servoBras1.attach(4);
  servoBras2.attach(5);
  servoBras3.attach(6);
  servoPince.attach(7);
  
  pinMode(13, OUTPUT);  //LED13 is used for mesure the button state.

}

void loop() {
  servoBase1.write(0);
  //Serial.println("80");
  delay(1000);
  //Serial.println("100");
  //servoBase1.write(100);
  delay(1000);
  //servoBase1.write(180);
  delay(1000);
}
