#include<unistd.h>
#include<stdio.h>


void ecrireFichier(const char* fichier, const char* in)
{
	FILE* monfichier;
	monfichier = fopen(fichier, "w");
	fprintf(monfichier, in);
	fclose(monfichier);
	return;
}


int main()
{

    ecrireFichier("/dev/ttyAMA0", "xyz\n");
    

    return 0;
}