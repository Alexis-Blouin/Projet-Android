#include <sys/socket.h>
#include <stdio.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <fcntl.h> // for open
#include <unistd.h> // for close
#include <pthread.h>
#include "control.h"


struct Voiture voiture;

int main()
{
	voiture = init_voiture();
	avant(voiture);
	sleep(2);
	stop(voiture);
	return 0;
}
