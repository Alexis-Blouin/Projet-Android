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

void initBras()
{
	base = malloc(sizeof(struct moteurBras));
	(*base).id = 1;
	(*base).angle = 0;
	(*base).angle_min = 0;
	(*base).angle_max = 180;

	brasEpaule = malloc(sizeof(struct moteurBras));
	(*brasEpaule).id = 2;
	(*brasEpaule).angle = 0;
	(*brasEpaule).angle_min = 0;
	(*brasEpaule).angle_max = 180;

	brasCoude = malloc(sizeof(struct moteurBras));
	(*brasCoude).id = 3;
	(*brasCoude).angle = 0;
	(*brasCoude).angle_min = 0;
	(*brasCoude).angle_max = 180;

	brasPoignet = malloc(sizeof(struct moteurBras));
	(*brasPoignet).id = 4;
	(*brasPoignet).angle = 0;
	(*brasPoignet).angle_min = 0;
	(*brasPoignet).angle_max = 180;

	rotationPince = malloc(sizeof(struct moteurBras));
	(*rotationPince).id = 5;
	(*rotationPince).angle = 0;
	(*rotationPince).angle_min = 0;
	(*rotationPince).angle_max = 180;

	pince = malloc(sizeof(struct moteurBras));
	(*pince).id = 6;
	(*pince).angle = 0;
	(*pince).angle_min = 0;
	(*pince).angle_max = 180;

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
