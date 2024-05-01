#include<unistd.h>
#include<stdio.h>

struct moteurBras
{
	int id;
	int angle;
	int angle_max;
	int angle_min;
}

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
