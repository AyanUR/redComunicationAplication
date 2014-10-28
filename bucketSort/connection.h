#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <sys/types.h>
#ifndef __CONNECTION_H__
#define __CONNECTION_H__
char initSocket(int *s,int dominio,int tipo,int protocolo){
	if((*s=socket(dominio,tipo,protocolo))==-1){
		printf("\nerror initializing socket");
		return -1;
	}
	return 0;
}
char settingSocket(int *s,int nivel,int nomopc,const void* valopc,socklen_t lonopc){
	if(setsockopt(*s,nivel,nomopc,valopc,lonopc)==-1){
		printf("\nerror setting option socket");
		return -1;
	}
	return 0;
}
void settingAddr(struct sockaddr_in *myaddr,int port,char *host){
	(*myaddr).sin_family=AF_INET;
	(*myaddr).sin_port=htons(port);
	inet_aton(host,&((*myaddr).sin_addr));
}
char makeConnection(int *s,struct sockaddr *addr,socklen_t addrlen){
	if(connect(*s,addr,addrlen)==-1){
		printf("\nerror connection faild");
		return -1;
	}
	return 0;
}
char asingName(int *s,struct sockaddr* addr,socklen_t addrlen){
	if(bind(*s,addr,addrlen)==-1){
		printf("\nerror asing name faild");
		return -1;
	}
	return 0;
}
char colaConnection(int *s,int cola){
	if(listen(*s,cola)==-1){
		printf("\nerror al poner cola de conexiones");
		return -1;
	}
	return 0;
}
char passComun(int *s,struct sockaddr_in **addr,int port,char *host){
	int *opcSock=(int *)malloc(sizeof(int));
	*opcSock=1;
	if(initSocket(s,AF_INET,SOCK_STREAM,0)==-1)return -1;
	if(settingSocket(s,SOL_SOCKET,SO_REUSEADDR,opcSock,sizeof(int))==-1)return -1;
	if(settingSocket(s,SOL_SOCKET,SO_KEEPALIVE,opcSock,sizeof(int))==-1)return -1;
   free(opcSock);
   settingAddr(*addr,port,host);
	return 0;
}
char connectClient(int *s,int port,char *host){
	struct sockaddr_in *addr=(struct sockaddr_in *)malloc(sizeof(struct sockaddr_in));
	if(passComun(s,&addr,port,host)==-1)return -1;
	if(makeConnection(s,(struct sockaddr*)addr,sizeof(struct sockaddr_in))==-1)return -1;	
	return 0;
}
char connectServer(int *s,int port,char *host,int cola){
	struct sockaddr_in *addr=(struct sockaddr_in *)malloc(sizeof(struct sockaddr_in));
	if(passComun(s,&addr,port,host)==-1)return -1;
	if(asingName(s,(struct sockaddr *)addr,sizeof(struct sockaddr_in))==-1)return -1;
	if(colaConnection(s,cola)==-1)return -1;
	return 0;
}
#endif
