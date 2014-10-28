#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>

#include <fcntl.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <resolv.h>
#include <arpa/inet.h>
#include <unistd.h>

#include "connection.h"
void burbuja(int *numbers,int top){
	int i,j,temp;
	for(i=0;i<top;i++){
		for(j=i+1;j<top;j++){
			if(numbers[j]<numbers[i]){
				temp=numbers[j];
				numbers[j]=numbers[i];
				numbers[i]=temp;
			}
		}
	}
}
void *receiveBucket(void *args){
	int top,leidos,sc=*((int*)args);
	if((leidos=recv(sc,&top,sizeof(int),0))==-1)
		printf("\nerror cant receive size of bucket");
	int numbers[top];
	if((leidos=recv(sc,numbers,sizeof(int)*top,0))==-1)
		printf("\nerror cant receive numbers");
	burbuja(numbers,top);
	if((leidos=send(sc,numbers,sizeof(int)*top,0))==-1)
		printf("\nerror cant send numbers");
	pthread_exit(NULL);
}
int main(int ari,char **arc){//./a.out puerto colaConexiones
	if(ari<3){
		printf("\nejecuta como %s puerto colaConexinoes",arc[0]);
		return -1;
	}
	int ss,sc;
	struct sockaddr_in addrc;
	socklen_t addrSize=sizeof(struct sockaddr_in);
	if(connectServer(&ss,atoi(arc[1]),"0.0.0.0",atoi(arc[2]))==-1)return -1;
	printf("\nserver wake up");
	while(1){
		printf("\nwaiting for client");
		if((sc=accept(ss,(struct sockaddr *)&addrc,&addrSize))==-1){
			printf("\nerror accepting client ");
			continue;
		}
		printf("\nclient conect %d\t%s",sc,inet_ntoa(addrc.sin_addr));
		pthread_t atiende;
		pthread_create(&atiende,NULL,receiveBucket,(void*)&sc);
//		pthread_detach(atiende);
	}
	puts("");return 0;
}
