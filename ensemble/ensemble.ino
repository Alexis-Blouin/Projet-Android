#include <SoftwareSerial.h>
#include <Servo.h>

Servo servoBase1;
Servo servoBase2;
Servo servoBras1;
Servo servoBras2;
Servo servoBras3;
Servo servoPince;

SoftwareSerial mySerial(0, 1);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  mySerial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);

  servoBase1.attach(2);
  servoBase2.attach(3);
  servoBras1.attach(4);
  servoBras2.attach(5);
  servoBras3.attach(6);
  servoPince.attach(8);
}

void loop() {
  // put your main code here, to run repeatedly:
  String valeur = "";
  // Tant que Serial1 est disponible
  while(mySerial.available()!=0){
    char nouveauCaractere = mySerial.read(); // Lit la donnée au port Serial1
    Serial.println(nouveauCaractere);
    if(nouveauCaractere == 'f'){ // Si le nouveau caractère est '\n', on affiche la donnée au port Serial

      int numeroMoteur = valeur.charAt(0) - 48;
      int angle = valeur.substring(1, 4).toInt();

      switch(numeroMoteur){
        case 1:
          servoBase1.write(angle);
          break;
        case 2:
          servoBase2.write(angle);
          break;
        case 3:
          servoBras1.write(angle);
          break;
        case 4:
          servoBras2.write(angle);
          break;
        case 5:
          servoBras3.write(angle);
          break;
        case 6:
          servoPince.write(angle);
          break;
      }
      String message = "moteur : " + String(numeroMoteur) + " a " + String(angle);
      Serial.println(message + "---" + valeur);
      valeur = "";
    }else{ // Sinon, on ajoute le nouveau caractère à valeur
      valeur += String(nouveauCaractere);
    }
  }

  // digitalWrite(LED_BUILTIN, HIGH);  // turn the LED on (HIGH is the voltage level)
  // delay(1000);                      // wait for a second
  // digitalWrite(LED_BUILTIN, LOW);   // turn the LED off by making the voltage LOW
  // delay(1000);  
}
