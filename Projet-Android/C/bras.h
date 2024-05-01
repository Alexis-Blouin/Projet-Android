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
	base.angle = 1;
	base.angle_min = 1;
	base.angle_max = 180;

	brasEpaule.id = 2;
	brasEpaule.angle = 70;
	brasEpaule.angle_min = 20;
	brasEpaule.angle_max = 180;

	brasCoude.id = 3;
	brasCoude.angle = 1;
	brasCoude.angle_min = 1;
	brasCoude.angle_max = 180;

	brasPoignet.id = 4;
	brasPoignet.angle = 1;
	brasPoignet.angle_min = 1;
	brasPoignet.angle_max = 180;

	rotationPince.id = 5;
	rotationPince.angle = 1;
	rotationPince.angle_min = 1;
	rotationPince.angle_max = 180;

	pince.id = 6;
	pince.angle = 15;
	pince.angle_min = 15;
	pince.angle_max = 80;
}


int ajouterAngleBras(struct moteurBras *moteur)
{
	FILE* monfichier;
	monfichier = fopen("/dev/ttyAMA0", "w");

	if (moteur->angle < 94)
	{
		moteur->angle += 5;
		fprintf(monfichier, "%d0%df", moteur->id, moteur->angle);
		fclose(monfichier);
		return 0;
	}

	moteur->angle += 5;
	fprintf(monfichier, "%d%df", moteur->id, moteur->angle);
	
	fclose(monfichier);

	if (moteur->angle >= moteur->angle_max)
		return 1;	
	return 0;
}

int reduireAngleBras(struct moteurBras *moteur)
{
	FILE* monfichier;
	monfichier = fopen("/dev/ttyAMA0", "w");

	if (moteur->angle > 104)
	{
		moteur->angle -= 5;
		fprintf(monfichier, "%d%df", moteur->id, moteur->angle);
		fclose(monfichier);
		return 0;
	}

	moteur->angle  -= 5;
	fprintf(monfichier, "%d0%df", moteur->id, moteur->angle);
	
	fclose(monfichier);

	if (moteur->angle <= moteur->angle_min)
		return 1;	
	return 0;
}

int versZeroBras()
{
	FILE* monfichier;
	monfichier = fopen("/dev/ttyAMA0", "w");
	
		base.angle = 90;
	fprintf(monfichier, "%d%df", base.id, base.angle);

		brasEpaule.angle = 20;
	fprintf(monfichier, "%d%df", brasEpaule.id, brasEpaule.angle);

		brasCoude.angle = 20;
	fprintf(monfichier, "%d%df", brasCoude.id, brasCoude.angle);

		brasPoignet.angle = 20;
	fprintf(monfichier, "%d%df", brasPoignet.id, brasPoignet.angle);

		rotationPince.angle = 1;
	fprintf(monfichier, "%d%df", rotationPince.id, rotationPince.angle);

		pince.angle = 1;
	fprintf(monfichier, "%d%df", pince.id, pince.angle);

	fclose(monfichier);
}


int versPeseBras()
{
	FILE* monfichier;
	monfichier = fopen("/dev/ttyAMA0", "w");
	
		base.angle = 90;
	fprintf(monfichier, "%d%df", base.id, base.angle);

		brasEpaule.angle = 20;
	fprintf(monfichier, "%d%df", brasEpaule.id, brasEpaule.angle);

		brasCoude.angle = 20;
	fprintf(monfichier, "%d%df", brasCoude.id, brasCoude.angle);

		brasPoignet.angle = 20;
	fprintf(monfichier, "%d%df", brasPoignet.id, brasPoignet.angle);

		rotationPince.angle = 1;
	fprintf(monfichier, "%d%df", rotationPince.id, rotationPince.angle);

		pince.angle = 1;
	fprintf(monfichier, "%d%df", pince.id, pince.angle);

	fclose(monfichier);
}