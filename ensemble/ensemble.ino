#include <SoftwareSerial.h>
#include <Servo.h>

// Déclaration des moteurs.
Servo servoBase1;
Servo servoBase2;
Servo servoBras1;
Servo servoBras2;
Servo servoBras3;
Servo servoPince;

// Déclaration du port série.
SoftwareSerial monSerial(0, 1);

void setup() {
  // Initialisation du port série.
  monSerial.begin(9600);

  // Attache les groupes de broches aux moteurs.
  servoBase1.attach(3);
  servoBase2.attach(4);
  servoBras1.attach(5);
  servoBras2.attach(6);
  servoBras3.attach(8);
  servoPince.attach(9);
  // Valeur par défaut des moteurs du bras pour le déplacement de la voiture.
  servoPince.write(15);
}

void loop() {
  String valeur = "";
  // Tant que monSerial est disponible
  while(monSerial.available()!=0){
    char nouveauCaractere = monSerial.read(); // Lit la donnée au port monSerial
    Serial.println(nouveauCaractere);
    if(nouveauCaractere == 'f'){ // Si le nouveau caractère est 'f', on traite la chaine de caractères reçue.

      // Récupère le numéro du moteur à déplacer.
      int numeroMoteur = valeur.charAt(0) - 48;
      // Récupère l'angle à lequel il doit ce mettre.
      int angle = valeur.substring(1, 4).toInt();

      // Action du moteur.
      switch(numeroMoteur){
        case 7:
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
      // Réinitialisation de la valeur.
      valeur = "";
    }else{
      // Sinon, on ajoute le nouveau caractère à valeur
      valeur += String(nouveauCaractere);
    }
  }
}
