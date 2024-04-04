#include <Servo.h>

Servo servoPince;
Servo servoBase1;
Servo servoBase2;
Servo servoBras1;
Servo servoBras2;
Servo servoBras3;


int adc_key_val[5] ={50, 200, 400, 600, 800 };
int NUM_KEYS = 5;
int adc_key_in;
int key=-1;
int oldkey=-1;

void setup() {
  Serial.begin(9600);

  // Attachement des broches de l'arduino aux variables Servo.
  servoPince.attach(0);
  servoBase1.attach(1);
  servoBase2.attach(2);
  servoBras1.attach(3);
  servoBras2.attach(4);
  servoBras3.attach(5);
  
  pinMode(13, OUTPUT);  //LED13 is used for mesure the button state.

}

void loop() {

  adc_key_in = analogRead(7);
  digitalWrite(13,LOW);
  key = get_key(adc_key_in);  //Call the button judging function.

  if (key != oldkey){   // Get the button pressed
      delay(50);
      adc_key_in = analogRead(7);
      key = get_key(adc_key_in);
      if (key != oldkey)    {
        oldkey = key;
        if (key >=0){
          digitalWrite(13,HIGH);
          switch(key){          // Send messages accordingly.
             case 0:Serial.println("S1 OK");
                    break;
             case 1:Serial.println("S2 OK");
                    break;
             case 2:Serial.println("S3 OK");
                    break;
             case 3:Serial.println("S4 OK");
                    break;
             case 4:Serial.println("S5 OK");
                    break;
          }
        }
      }
  }
  delay(100);
  //https://wiki.dfrobot.com/RoMeo_BLE__SKU_DFR0305_
  // put your main code here, to run repeatedly:
  // servoPince.write(15); // move MG996R's shaft to angle 0°
  // delay(2000); // wait for one second
  // servoPince.write(80); // move MG996R's shaft to angle 45°
  // delay(2000); // wait for one second 
  servoBase1.write(0); // move MG996R's shaft to angle 90°
  servoBase2.write(0); // move MG996R's shaft to angle 90°
  servoBras1.write(0); // move MG996R's shaft to angle 90°
  servoBras2.write(0); // move MG996R's shaft to angle 90°
  servoBras3.write(0); // move MG996R's shaft to angle 90°
  // delay(1000); // wait for one second
  // servoPince.write(180); // move MG996R's shaft to angle 90°
  // delay(1000); // wait for one second
  // servoPince.write(360); // move MG996R's shaft to angle 90°
  // delay(1000); // wait for one second
  // servoPince.write(270); // move MG996R's shaft to angle 90°
  // delay(1000); // wait for one second
  // servoPince.write(720); // move MG996R's shaft to angle 90°
  // delay(1000); // wait for one second
  // servo.write(135); // move MG996R's shaft to angle 135°
  // delay(1000); // wait for one second
  // servo.write(180); // move MG996R's shaft to angle 180°
  // delay(1000); // wait for one second
  // servo.write(15); // move MG996R's shaft to angle 180°
  // delay(1000); // wait for one second
}

// To know the pressed button.
int get_key(unsigned int input){
    int k;
    for (k = 0; k < NUM_KEYS; k++){
      if (input < adc_key_val[k]){     // Get the button pressed
            return k;
      }
   }
   if (k >= NUM_KEYS)k = -1;  // No button is pressed.
   return k;
}
