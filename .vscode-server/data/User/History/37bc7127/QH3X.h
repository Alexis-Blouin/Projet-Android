#include<unistd.h>
#include<stdio.h>

struct moteurBras
{
	int id;
	int angle;
	int angle_max;
	int angle_min;
};

struct moteurBras base;

struct moteurBras brasEpaule;
struct moteurBras brasCoude;
struct moteurBras brasPoignet;

struct moteurBras rotationPince;
struct moteurBras pince;


void initBras()
{
	base.id = 1;
	base.angle = 0;
	base.angle_min = 0;
	base.angle_max = 180;

	brasEpaule.id = 2;
	brasEpaule.angle = 0;
	brasEpaule.angle_min = 0;
	brasEpaule.angle_max = 180;

	brasCoude.id = 3;
	brasCoude.angle = 0;
	brasCoude.angle_min = 0;
	brasCoude.angle_max = 180;

	brasPoignet.id = 4;
	brasPoignet.angle = 0;
	brasPoignet.angle_min = 0;
	brasPoignet.angle_max = 180;

	rotationPince.id = 5;
	rotationPince.angle = 0;
	rotationPince.angle_min = 0;
	rotationPince.angle_max = 180;

	pince.id = 6;
	pince.angle = 0;
	pince.angle_min = 0;
	pince.angle_max = 180;
}


int ajouterAngleBras(struct moteurBras moteur)
{
	FILE* monfichier;
	monfichier = fopen("/dev/ttyAMA0", "w");

	if (moteur.angle < 100)
	{
		moteur.angle += 1;
		fprintf(monfichier, "%d0%df", moteur.id, moteur.angle);
		return 0;
	}

	moteur.angle += 1;
	fprintf(monfichier, "%d%df", moteur.id, moteur.angle);
	
	fclose(monfichier);

	if (moteur.angle >= moteur.angle_max)
		return 1;	
	return 0;
}

int reduireAngleBras(struct moteurBras moteur)
{
	FILE* monfichier;
	monfichier = fopen("/dev/ttyAMA0", "w");

	if (moteur.angle > 100)
	{
		moteur.angle -= 1;
		fprintf(monfichier, "%d%df", moteur.id, moteur.angle);
		return 0;
	}

	moteur.angle -= 1;
	fprintf(monfichier, "%d0%df", moteur.id, moteur.angle);
	
	fclose(monfichier);

	if (moteur.angle <= moteur.angle_min)
		return 1;	
	return 0;
}