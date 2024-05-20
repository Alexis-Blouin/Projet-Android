#include<unistd.h>
#include<stdio.h>

struct Voiture
{
	const char* droitDir;
        const char* droit;
        const char* gaucheDir;
        const char* gauche;

};


void writeFile(const char* file, const char* in);
void forward(struct Voiture voiture);
void backward(struct Voiture voiture);
void droit(struct Voiture voiture);
void gauche(struct Voiture voiture);
void stop(struct Voiture voiture);

int main() {
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
	writeFile(export, "22");
	writeFile(export, "23");
	writeFile(export, "24");
	writeFile(export, "25");

	writeFile(gpio22dir, "out");
	writeFile(gpio23dir, "out");
	writeFile(gpio24dir, "out");
	writeFile(gpio25dir, "out");

	for (int i = 0; i < 3; i++)
	{
		droit(voiture);
		sleep(1);
		gauche(voiture);
		sleep(1);
	}

	stop(voiture);
	return 0;
}

void writeFile(const char* file, const char* in)
{
	FILE* myfile;
	myfile = fopen(file, "w");
	fprintf(myfile, in);
	fclose(myfile);
	return;
}

void forward(struct Voiture voiture)
{
	writeFile(voiture.droit, "1");
	writeFile(voiture.gaucheDir, "0");
        writeFile(voiture.droitDir, "0");
        writeFile(voiture.gauche, "1");
}

void backward(struct Voiture voiture)
{
        writeFile(voiture.droit, "1");
	writeFile(voiture.droitDir, "1");
        writeFile(voiture.gaucheDir, "1");
        writeFile(voiture.gauche, "1");
}



void droit(struct Voiture voiture)
{
	writeFile(voiture.droit, "1");
	writeFile(voiture.gaucheDir, "0");
        writeFile(voiture.droitDir, "1");
        writeFile(voiture.gauche, "1");
}

void gauche(struct Voiture voiture)
{
        writeFile(voiture.droit, "1");
	writeFile(voiture.droitDir, "0");
        writeFile(voiture.gaucheDir, "1");
        writeFile(voiture.gauche, "1");
}




void stop(struct Voiture voiture)
{
	writeFile(voiture.droit, "0");
        writeFile(voiture.gauche, "0");
	writeFile(voiture.droitDir, "0");
        writeFile(voiture.gaucheDir, "0");

}
