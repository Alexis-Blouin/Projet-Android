#include "HX711.h"

// HX711 circuit wiring
const int LOADCELL_DOUT_PIN = 3; // Name the DT pin of the XH711
const int LOADCELL_SCK_PIN = 2; // Name the SCK pin of the HX711

HX711 scale;

void setup() {
  Serial.begin(9600);
  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN); // The two pins start transmit datas
}

void loop() {

  if (scale.is_ready()) {
    long reading = scale.read();
    float conversion = ((reading + 122700.)/ 320200 * 10.)*0.724; // modifie the values regarding your "zero" value and your weight standard
    Serial.print("HX711 reading: "); // your monitor prints the values reads and begins by "HX711 reading: "
    Serial.println(conversion);
    //Serial.println(reading);
  } else {
    Serial.println("HX711 not found.");
  }
  delay(100);
}

//-103 tête en bas
//-103.15 plat
//-102.75 corp