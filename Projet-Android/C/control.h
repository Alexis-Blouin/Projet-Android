#include<unistd.h>
#include<stdio.h>

struct Voiture
{
	const char* droitDir;
        const char* droit;
        const char* gaucheDir;
        const char* gauche;

};


void ecrireFichier(const char* fichier, const char* in);
void avant(struct Voiture voiture);
void arriere(struct Voiture voiture);
void droit(struct Voiture voiture);
void gauche(struct Voiture voiture);
void stop(struct Voiture voiture);

struct Voiture init_voiture() {
	const char* export = "/sys/class/gpio/export";

	struct Voiture voiture;
	// M1-M3 cote droit = 23 (direction 22)
	// M2-M4 cote gauche = 25 (direction 24)
	voiture.droitDir  = "/sys/class/gpio/gpio22/value";
	voiture.droit     = "/sys/class/gpio/gpio23/value";
	voiture.gaucheDir = "/sys/class/gpio/gpio24/value";
	voiture.gauche    = "/sys/class/gpio/gpio25/value";

	const char* gpio22dir = "/sys/class/gpio/gpio22/direction";
        const char* gpio23dir = "/sys/class/gpio/gpio23/direction";
        const char* gpio24dir = "/sys/class/gpio/gpio24/direction";
        const char* gpio25dir = "/sys/class/gpio/gpio25/direction";

	//initialise les sorties gpio
	ecrireFichier(export, "22");
	ecrireFichier(export, "23");
	ecrireFichier(export, "24");
	ecrireFichier(export, "25");

	sleep(1);

	ecrireFichier(gpio22dir, "out");
	ecrireFichier(gpio23dir, "out");
	ecrireFichier(gpio24dir, "out");
	ecrireFichier(gpio25dir, "out");

	stop(voiture);
	return voiture;
}


void ecrireFichier(const char* fichier, const char* in)
{
	FILE* monfichier;
	monfichier = fopen(fichier, "w");
	fprintf(monfichier, in);
	fclose(monfichier);
	return;
}


void avant(struct Voiture voiture)
{
	ecrireFichier(voiture.droit, "1");
	ecrireFichier(voiture.gaucheDir, "0");
        ecrireFichier(voiture.droitDir, "0");
        ecrireFichier(voiture.gauche, "1");
}

void arriere(struct Voiture voiture)
{
        ecrireFichier(voiture.droit, "1");
	ecrireFichier(voiture.droitDir, "1");
        ecrireFichier(voiture.gaucheDir, "1");
        ecrireFichier(voiture.gauche, "1");
}



void droit(struct Voiture voiture)
{
	ecrireFichier(voiture.droit, "1");
	ecrireFichier(voiture.gaucheDir, "0");
        ecrireFichier(voiture.droitDir, "1");
        ecrireFichier(voiture.gauche, "1");
}

void gauche(struct Voiture voiture)
{
        ecrireFichier(voiture.droit, "1");
	ecrireFichier(voiture.droitDir, "0");
        ecrireFichier(voiture.gaucheDir, "1");
        ecrireFichier(voiture.gauche, "1");
}




void stop(struct Voiture voiture)
{
	ecrireFichier(voiture.droit, "0");
        ecrireFichier(voiture.gauche, "0");
	//ecrireFichier(voiture.droitDir, "0");
        //ecrireFichier(voiture.gaucheDir, "0");

}
