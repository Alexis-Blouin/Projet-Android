#include<unistd.h>
#include<stdio.h>

struct moteurBras
{
	int id;
	int angle;
	int angle_max;
	int angle_min;
};

struct moteurBras* base = NULL;

struct moteurBras* brasEpaule = NULL;
struct moteurBras* brasCoude = NULL;
struct moteurBras* brasPoignet = NULL;

struct moteurBras* rotationPince = NULL;
struct moteurBras* pince = NULL;

void changerAngleBras()
{

}

int main()
{
	int angle = 15;

	for (;;)
{
	usleep(10000);
	if (angle > 79)
	angle = 15;

	angle++;

    FILE* monfichier;
	monfichier = fopen("/dev/ttyAMA0", "w");
		fprintf(monfichier, "60%df", angle);
	fclose(monfichier);
}

    return 0;
}
