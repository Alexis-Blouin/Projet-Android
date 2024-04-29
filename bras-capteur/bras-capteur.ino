#include <SoftwareSerial.h>
#include <Servo.h>
#include "HX711.h"

// Déclaration des moteurs.
Servo servoBase1;
Servo servoBase2;
Servo servoBras1;
Servo servoBras2;
Servo servoBras3;
Servo servoPince;

// Déclaration des broches
const int LOADCELL_DOUT_PIN = 3; // Fil jaune
const int LOADCELL_SCK_PIN = 2; // Fil vert

HX711 balance; // Déclaration de la balance.

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

  // Démarrage du capteur de tension.
  balance.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);
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
      // Réinitialisation de la valeur.
      valeur = "";
    }else if(nouveaucaractere == 'c'){
      // Déclaration des variables pour le poid et le nombre de données lues.
      int totalDonnees = 0;
      float donnee = 0;
      // Tente récupérer la données 10 fois.
      for(int i = 0; i < 25; i++){
        // Si le capteur de tension est disponible.
        if (balance.is_ready()) {
          // Lecture du capteur.
          long reading = balance.read();
          // Conversion de la valeur lue en donnée interprétable.
          float conversion = ((reading + 122700.)/ 320200 * 10.)*0.724;
          // Mesure initiale avec la pince attachée.
          float poidPince = 2.50;
          // On enlève la nouvelle mesure à la mesure de la pince et on divise par 10 pour avoir le poid en grammes.
          donnee = (poidPince - conversion) / 10;
          Serial.println(String(donnee));

          totalDonnees++;
        }
      }

      // Envoi la moyenne des donnée récupérée au Raspberry Pi.
      monSerial.println(donnee / totalDonnees);

      valeur = "";
    }else{
      // Sinon, on ajoute le nouveau caractère à valeur
      valeur += String(nouveauCaractere);
    }
  }
}
