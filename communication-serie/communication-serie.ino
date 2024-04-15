#include <SoftwareSerial.h>

SoftwareSerial mySerial(0, 1);

void setup() {
  // put your setup code here, to run once:
  mySerial.begin(9600);
//Serial1.begin(9600);
//Serial.begin(9600);
pinMode(LED_BUILTIN, OUTPUT);
}

void loop() {
  bool allume = false;
  // put your main code here, to run repeatedly:
  String valeur = "";
  while (mySerial.available()) { // Check if data is available
    char newCharacter = mySerial.read(); // Read a character

    if(newCharacter == 'a'){
      allume = true;
      mySerial.println("received");
    }else if(newCharacter == 'b'){
      allume = false;
    }

    if(allume){
      digitalWrite(LED_BUILTIN, HIGH);
    }else{
      digitalWrite(LED_BUILTIN, LOW);
  }
  }

  // digitalWrite(LED_BUILTIN, HIGH);  // turn the LED on (HIGH is the voltage level)
  // delay(1000);                      // wait for a second
  // digitalWrite(LED_BUILTIN, LOW);   // turn the LED off by making the voltage LOW
  // delay(1000);  
}
