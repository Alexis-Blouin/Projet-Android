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

  // code permettant de contrôler les moteurs avec les boutons.
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
             servoBras3.write(0);
                    break;
             case 1:Serial.println("S2 OK");
             servoBras3.write(90);
                    break;
             case 2:Serial.println("S3 OK");
             servoBras3.write(180);
                    break;
             case 3:Serial.println("S4 OK");
             //servoBras2.write(90);
             delay(500);
             //servoBras1.write(0);
                    break;
             case 4:Serial.println("S5 OK");
             //servoBras1.write(180);
             //servoPince.write(80);
                    break;
          }
        }
      }
  }
  delay(100);

  // max pince 15 = ouvert 80 = fermé
  //https://wiki.dfrobot.com/RoMeo_BLE__SKU_DFR0305_
  // servoPince.write(15);
  // servoBras2.write(270);
  //servoBase1.write(90);
  //servoBase2.write(90);
  //servoBras1.write(90);
  //servoBras2.write(90);
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
