#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>

#include <fcntl.h>
#include <netinet/in.h>
#include <resolv.h>
#include <sys/socket.h>
#include <arpa/inet.h>

#include "bucket.h"
#include "connection.h"
typedef struct peticion{
	char ipServer[15];
	int portServer;
	struct bucket *bucket;
	struct peticion *next;
}peticion;
void *sendBucket(void *args){
	peticion *temp=(peticion*)args;
	int sc,escritos;
	//printNumbers(temp->bucket);
	if(connectClient(&sc,temp->portServer,temp->ipServer)==-1)pthread_exit(NULL);
	printf("\nclient connect sending bucket...");
	if((escritos=send(sc,&temp->bucket->top,sizeof(int),0))==-1)
		printf("\nerror cant send size of bucket");
	if((escritos=send(sc,temp->bucket->numbers,sizeof(int)*temp->bucket->top,0))==-1)
		printf("\nerror cant send numbers");
	if((escritos=recv(sc,temp->bucket->numbers,sizeof(int)*temp->bucket->top,0))==-1)
		printf("\nerror cant receive numbers");
	printNumbers(temp->bucket);
	pthread_exit(NULL);
}
int main(int ari,char **arc){//./a.out numeroCubetas rangUp-rangoDown numeroAleatorios
	if(ari<4){
		printf("\nerror ejecuta como %s numberoCubetas rangoUp-rangoDown numeroAleatorios",arc[0]);
		puts("");return -1;
	}
	int i,j,buckets=atoi(arc[1]),rangUp,rangDown,numbersTotalRandom=atoi(arc[3]);
	sscanf(arc[2],"%d-%d",&rangDown,&rangUp);
	bucket *cab=NULL;
//	peticion *request=(peticion*)malloc(sizeof(peticion));
	peticion *request=NULL,*nvo=NULL,*p=NULL;
	pthread_t thread[buckets];
	build(&cab,buckets,rangUp,rangDown);
	insertNumbers(&cab,numbersTotalRandom);
	for(i=0;cab!=NULL&&i<buckets;cab=cab->next,i++){
		if((nvo=(peticion *)malloc(sizeof(peticion)))==NULL)return -1;
		printf("enter ip for server for bucket %d\n ",i+1);
		scanf("%s",nvo->ipServer);
		printf("\nenter port\n ");
		scanf("%d",&nvo->portServer);
		nvo->bucket=cab;
		nvo->next=NULL;
		if(request==NULL)request=nvo;
		else{
			for(p=request;p->next!=NULL;p=p->next);
			p->next=nvo;
		}
	}
	for(i=0;request!=NULL&&i<buckets;request=request->next,i++){
		//strcpy(request->ipServer,"127.0.0.1");
		//request->portServer=4000;
		//request->bucket=cab;
		pthread_create(&thread[i],NULL,sendBucket,(void *)request);
	}
	for(j=0;j<buckets;j++)
		pthread_join(thread[j],NULL);
	puts("");return 0;
}
