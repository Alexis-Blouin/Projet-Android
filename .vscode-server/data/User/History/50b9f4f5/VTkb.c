#include <sys/socket.h>
#include <stdio.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <fcntl.h> // for open
#include <unistd.h> // for close
#include <pthread.h>
#include "control.h"
#include "bras.h"

#define PORT 1444
#define BUFFER_SIZE 104857600

struct ThreadParam
{
	struct moteurBras* moteur;
	int* clientThreadIsRunning;
	int augment;
};

void* brasThread(void* argp) 
{ 
	struct ThreadParam* threadParam = (struct ThreadParam *)argp;
	
	for (;;)
	{
		printf("   Bras d'id : %d   angle : %d \n", threadParam->moteur->id, threadParam->moteur->angle);
		fflush(stdout);

		if ((*threadParam).augment)
		{
			printf("AUGMENTE");
			fflush(stdout);
			
			if (ajouterAngleBras(threadParam->moteur))
				return NULL;
		}
		else
		{
			printf("REDUIT");
			fflush(stdout);
		
			if (reduireAngleBras(threadParam->moteur))
				return NULL;
		}

		usleep(500000);
		if (! *((*threadParam).clientThreadIsRunning))
		{
			return NULL;
		}
	}
	return NULL;
}

struct Voiture voiture;

void* handle_client(void *arg)
{
	int* clientThreadIsRunning = malloc(sizeof(int));
	*clientThreadIsRunning = 0;
	struct ThreadParam threadParam;
	threadParam.clientThreadIsRunning = clientThreadIsRunning;
	threadParam.augment = 0;
	pthread_t client_thread;
	int client_fd = *((int*) arg);
	char* buffer = (char*) malloc(BUFFER_SIZE * sizeof(char));
	printf("received connection\n");
	
	ssize_t bytes_received = recv(client_fd, buffer, BUFFER_SIZE, 0);
	

	while(bytes_received != 0 && bytes_received != -1)
	{
		
		int code_command = buffer[0];
		printf("Code commande : %d\n", code_command);
		printf("%s", buffer);
		fflush(stdout);
		
		switch (code_command)
		{
			// de 0 a ... (ASSCI)
			case '5':
				stop(voiture);
			case '1':
				avant(voiture);
				break;
			case '2':
				arriere(voiture);
				break;
			case '3':
				droit(voiture);
				break;
			case '4':
				gauche(voiture);
				break;

			case 'a':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &base;
				threadParam.augment = 0;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'A':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &base;
				threadParam.augment = 1;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'b':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &brasEpaule;
				threadParam.augment = 0;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'B':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &brasEpaule;
				threadParam.augment = 1;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'c':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &brasCoude;
				threadParam.augment = 0;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'C':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &brasCoude;
				threadParam.augment = 1;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'd':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &brasPoignet;
				threadParam.augment = 0;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'D':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &brasPoignet;
				threadParam.augment = 1;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'e':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &rotationPince;
				threadParam.augment = 0;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'E':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &rotationPince;
				threadParam.augment = 1;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'f':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &pince;
				threadParam.augment = 0;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'F':
				if (*clientThreadIsRunning)
				{
						*clientThreadIsRunning = 0;
						pthread_join(client_thread, NULL);
				}
				*clientThreadIsRunning = 1;
				threadParam.moteur = &pince;
				threadParam.augment = 1;
				pthread_create(&client_thread, NULL, brasThread, (void *)&threadParam);
				break;
			case 'z': case 'Z':
				
				FILE* fichier = fopen("mesure", "r");
				char buffer[30];
				fscanf(fichier, "%s", buffer);
				send(client_fd, buffer, 50, 0);
				break;
			default:

				stop(voiture);
				if (*clientThreadIsRunning)
				{
					*clientThreadIsRunning = 0;
					pthread_join(client_thread, NULL);
				}
				break;
		}
		
		bytes_received = recv(client_fd, buffer, BUFFER_SIZE, 0);
	}

	close(client_fd);
	free(arg);
	free(buffer);
	return NULL;
}

int main()
{
	voiture = init_voiture();
	initBras();
	versZeroBras();
	int server_fd;
	struct sockaddr_in server_addr;

	if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) 
	{
		perror("socket failed");
		exit(EXIT_FAILURE);
	}

	server_addr.sin_family = AF_INET;
	server_addr.sin_addr.s_addr = INADDR_ANY;
	server_addr.sin_port = htons(PORT);

	if (bind(server_fd,(struct sockaddr *) &server_addr, sizeof(server_addr) ) < 0 )
	{
		perror("bind failed");
		exit(EXIT_FAILURE);
	}

	if (listen(server_fd, 10) < 0)
	{
		perror("listen failed");
		exit(EXIT_FAILURE);
	}

	printf("server started\n");
	while(1)
	{
		struct sockaddr_in client_addr;
		socklen_t client_addr_len = sizeof(client_addr);
		int* client_fd = malloc(sizeof(int));

		if ((*client_fd = accept(server_fd, (struct sockaddr*)&client_addr, &client_addr_len)) < 0)
		{
			perror("accept failed");
			continue;
		}

		pthread_t thread_id;
		pthread_create(&thread_id, NULL, handle_client, (void *)client_fd);
		pthread_detach(thread_id);
	}

	close(server_fd);
	return 0;
}
