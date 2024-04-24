#include "HX711.h"

// Déclaration des broches
const int LOADCELL_DOUT_PIN = 3; // Fil jaune
const int LOADCELL_SCK_PIN = 2; // Fil vert

HX711 scale;

void setup() {
  Serial.begin(9600);
  // Démarrage du capteur de tension.
  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);
}

void loop() {

  if (scale.is_ready()) {
    // Lecture du capteur.
    long reading = scale.read();
    // Conversion de la valeur lue en donnée interprétable.
    float conversion = ((reading + 122700.)/ 320200 * 10.)*0.724;
    // Mesure initiale avec la pince attachée.
    float poidPince = 2.50;
    // On enlève la nouvelle mesure à la mesure de la pince et on divise par 10 pour avoir le poid en grammes.
    float donnee = (poidPince - conversion) / 10;
    Serial.println(String(donnee));
  } else {
    Serial.println("HX711 not found.");
  }
  delay(100);
}
