#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/types.h>
#include <netdb.h>
#include <netinet/in.h>
#include <sys/stat.h>
#include <unistd.h>
#define sizenamefile 50
#define sizeroute 111
#define sizeip 15
#define sizebufer 1024
void beginEnd(int state,void *args){
	close(*((int *)args));
}
void error(char *error,char *aux){
	printf("\n%s %s",error,aux);
	exit(-1);
}
long sizeofFile(FILE *file){
	fseek(file,0,SEEK_END);
	return (ftell(file));
}
void llenaAddr(struct sockaddr_in *cliente,struct hostent *host,int port){
	cliente->sin_family=host->h_addrtype;
	memcpy((char *)&cliente->sin_addr.s_addr,host->h_addr_list[0],host->h_length);
	cliente->sin_port=htons(port);
}
int sendFile(char *route,int dsc){
	FILE *file;
	char name[sizenamefile]={0};
	unsigned char bufer[sizebufer]={0};
	long tamanofile,completado=0;
	int leidos;
	if((file=fopen(route,"rb"))==NULL)
		error("error al abrir el archivo ",route);
	tamanofile=sizeofFile(file);
	fseek(file,0,SEEK_SET);
	strcpy(name,(strrchr(route,'/')+1));
	if(write(dsc,(void *)name,sizenamefile*sizeof(char))<0)
		error("no puede enviar el nombre del archivo",name);
	if(write(dsc,(void *)&tamanofile,sizeof(long))<0)
		error("no pude enviar el tamaño del archivo ","");
	while(completado<tamanofile){
		leidos=fread(bufer,1,sizebufer*sizeof(char),file);
		completado+=leidos;
		printf("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\bescrito %ld",completado);
		if(write(dsc,(void *)bufer,leidos)<0)
			error("error al escrivir el archivo ",name);
	}
	fclose(file);
	printf("\ntermine de enviar %s|%ld\n",name,tamanofile);
	return 0;
}
void selectFiles(int *numfile,char routes[][sizeroute]){
	int i;
	printf("\ningrese en numero de archivos a enviar\t");
	scanf("%d",numfile);
	for(i=0;i<*numfile;i++){
		getchar();
		printf("\ningrese el paht o ruta absoluta del %d archivo a enviar\t",i+1);
		fgets(routes[i],sizeroute,stdin);
		routes[i][strlen(routes[i])-1]=0;
	}
}
int main(int ari,char **arc){//./a.out 127.0.0.1 4000
	if(ari<3)
		error("error ejecuta como ./nombreEjecutable ipServidor puerto ","");
	void(*end)(int,void *)=&beginEnd;
	int dsc,numfile;
	char routes[7][sizeroute];
	struct sockaddr_in cliente;
	struct hostent *host;
	selectFiles(&numfile,routes);
	if((host=gethostbyname(arc[1]))==NULL)
		error("error al resolver la ip",arc[1]);
	if((dsc=socket(AF_INET,SOCK_STREAM,0))<0)
		error("error al abrir el socket :(","");
	on_exit(end,&dsc);
	llenaAddr(&cliente,host,atoi(arc[2]));
	if(connect(dsc,(struct sockaddr *)&cliente,sizeof(struct sockaddr_in))<0)
		error("error al estableser la coneccion","");
	printf("\nconexion establesida :D");
	printf("\n%d archivos a enviar\n",numfile);
	write(dsc,&numfile,sizeof(int));
	while((numfile--)>0)
		sendFile(routes[numfile],dsc);
	puts("");return 0;
}
