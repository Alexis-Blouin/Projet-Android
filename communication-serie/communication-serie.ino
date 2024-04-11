void setup() {
  // put your setup code here, to run once:
Serial.begin(9600);
Serial1.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  String valeur = "";
  while(Serial1.available()!=0){
    char nouveauCaractere = Serial1.read(); // Lit la donnée au port Serial1
    if(nouveauCaractere == '\n'){ // Si le nouveau caractère est '\n', on affiche la donnée au port Serial
      valeur.remove(0, 1);
      Serial.println(valeur);
      valeur = "";
    }else{ // Sinon, on ajoute le nouveau caractère à valeur
      valeur += String(nouveauCaractere);
    }
  };
}
