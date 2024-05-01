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

struct Voiture voiture;

void* handle_client(void *arg)
{
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
			default:
				stop(voiture);
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
