#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <netdb.h>
#include <netinet/in.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#define sizenamefile 50
#define sizeroute 111
#define sizeip 15
#define sizebufer 1024
void error(char *error,char *aux){
	printf("\n%s %s",error,aux);
	exit(-1);
}
void beginEnd(int state,void *args){
	close(*((int *)args));
}
void llenaAddr(struct sockaddr_in *servidor,int port){
	servidor->sin_family=AF_INET;
	servidor->sin_addr.s_addr=htonl(INADDR_ANY);
	servidor->sin_port=htons(port);
}
int recive(int dsc,char *route){
	printf("\nsize_max=%d",SIZE_MAX);
	FILE *file;
	int leidos;
	long tamanofile,completado=0;
	char name[sizenamefile],path[sizeroute]={0};
	unsigned char bufer[sizebufer]={0};
	read(dsc,(void *)&name,sizenamefile*sizeof(char));
	read(dsc,(void *)&tamanofile,sizeof(long));
	strcpy(path,route);	strcat(path,name);
	if((file=fopen(path,"wb"))==NULL)
		error("error al abrir el archivo ",route);
	while((tamanofile-completado)>0){
		if((tamanofile-completado)>=(sizebufer*sizeof(char))){
			if((leidos=read(dsc,bufer,sizebufer*sizeof(char)))<0)
				error("error al leer archivo ",name);
		}else{
			if((leidos=read(dsc,bufer,(tamanofile-completado)))<0)
				error("error al leer archivo",name);
		}
		completado+=leidos;
		fwrite(bufer,1,leidos,file);	fflush(file);
		printf("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\bcompletado=%ld",completado);
	}
	fclose(file);
	printf("\nterminer de recivir %s|%ld\n",name,tamanofile);
	return 0;
}
int main(int ari,char **arc){//./a.out 127.0.0.1 4000 "home/ayan/servidor/"
	if(ari<3)
		error("error ejecuta como ./nombreEjecutable ipServidor puerto ruta","");
	void(*end)(int,void *)=&beginEnd;
	int dss,dsc,numfile;
	struct sockaddr_in cliente,servidor;
	struct hostent *host;
	socklen_t sizeofcliente=sizeof(struct sockaddr_in);
	if((host=gethostbyname(arc[1]))==NULL)
		error("error al resolver la ip ",arc[1]);
	if((dss=socket(AF_INET,SOCK_STREAM,0))<0)
		error("error al obtener descriptor de socket :(","");
	on_exit(end,&dss);
	llenaAddr(&servidor,atoi(arc[2]));
	if(bind(dss,(struct sockaddr *)&servidor,sizeof(struct sockaddr_in))<0)
		error("error al asignar direccion local al socket :(","");
	listen(dss,3);
	while(1){
		printf("\nesperando cliente ...");
		if((dsc=accept(dss,(struct sockaddr *)&cliente,&sizeofcliente))<0)
			error("error al aceptar la conexion","");
		printf("\ncliente conectado ");
		if(read(dsc,(void *)&numfile,sizeof(int))<0)
			error("error al leer la cantidad de archivos a recivir ","");
		printf("\n%d archivos a recivir\n",numfile);
		while((numfile--)>0)
			recive(dsc,arc[3]);
	}
	puts("");return 0;
}
