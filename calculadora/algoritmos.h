#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include "stack.h"
#include "list.h"
#ifndef __ALGORITMOS_H__
#define __ALGORITMOS_H__
stack *oprdrs=NULL;
list *psfj=NULL;
double *dt;
char *st,fplus=0;
int precedencia(char *operador){
   if(strlen(operador)>=3)//esta mal funciona pero lo correcto es hacer un if(strcmp(operador,"cos")||strcmp(operador,"sen")||...)
      return 3;
   if(!strcmp(operador,"*")||!strcmp(operador,"/"))
      return 2;
   if(!strcmp(operador,"+")||!strcmp(operador,"-"))
      return 1;
   if(!strcmp(operador,"("))
      return 0;
	return -1;
}
void passOperador(){
	char *temp=(char *)malloc(sizeof(char)*strlen((char*)peek(oprdrs)));
	strcpy(temp,(char*)peek(oprdrs));
	add(&psfj,(void*)temp,sizeof(char));
	pop(&oprdrs);
}
void addOperador(char *operador){
	if(oprdrs!=NULL){
		while(oprdrs!=NULL){
			if(precedencia(operador)>precedencia((char*)peek(oprdrs)))
				break;
			else
				passOperador();
		}
	}
	push(&oprdrs,(void *)operador,sizeof(char));
}
void addNumber(){
	if(fplus){
		st=(char *)malloc(sizeof(char));
		strcpy(st,"+");
		addOperador(st);
	}else
		fplus=1;
}
void emtyalsepar(){
	while(strcmp((char*)peek(oprdrs),"("))
		passOperador();
	pop(&oprdrs);
}
void emptyStack(list **psfjO){
	while(oprdrs!=NULL)
		passOperador();
	*psfjO=psfj;
}
void toPosfija(char *token,int nb){
	if(nb==sizeof(double)){
		dt=(double*)malloc(sizeof(double));
		*dt=strtod(token,NULL);
		addNumber();
		add(&psfj,(void *)dt,sizeof(double));
	}else{
		if(nb==sizeof(char)){
			fplus=0;
			if(!strcmp(token,"(")){
				st=(char *)malloc(sizeof(char));
				strcpy(st,"(");
				push(&oprdrs,(void *)st,sizeof(char));	
			}else{
				if(!strcmp(token,")"))
					emtyalsepar();
				else{
					st=(char *)malloc(sizeof(char)*strlen(token));
					strcpy(st,token);
					addOperador(st);
				}
			}
		}else printf("\nerror in function toPosfija");
	}
}
void rmSgns(char *cad){
	char st='s',*temp=(char*)malloc(sizeof(char)*strlen(cad));
	int i,tam;
	bzero(temp,strlen(temp)*sizeof(char));
	for(i=0,tam=0;i<strlen(cad);i++){
		if(cad[i]=='+'||cad[i]=='-'){
			if(st=='s')
				st=cad[i];
			else{
				if(st=='+'){
					if(cad[i]=='+')
						st='+';
					else{
						if(cad[i]=='-')
							st='-';
					}
				}
				else{
					if(st=='-'){
						if(cad[i]=='+')
							st='-';
						else{
							if(cad[i]=='-')
								st='+';
						}
					}
				}
			}
		}
		else{
			if(st!='s'){
				temp[tam++]=st;
				st='s';
			}
			temp[tam++]=cad[i];
		}
	}
	strcpy(cad,temp);
}
#endif
