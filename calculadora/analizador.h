#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "algoritmos.h"
#ifndef __ANALIZADOR_H__
#define __ANALIZADOR_H__
int state=0,ib=0,it=0,iba;
char *bufer,*token,c;
void nextState(int s,char c){
	state=s;
	token=(char*)realloc((void*)token,it+2);
	token[it++]=c;
}
void freeToken(){
	it=state=0;
	free((void *)token);
	token=NULL;
}
void reconocido(char c,char flat,int nb){
	if(flat)
		nextState(0,c);
	else
		iba--;
	ib=iba;
	toPosfija(token,nb);
	freeToken();
}
char atmtc(){
	iba=ib;
	state=0;
	while(1){
		c=bufer[iba++];
		switch(state){
			case 0:
				if(c=='c')
					nextState(1,c);
				else
					return 1;
			break;
			case 1:
				if(c=='o')
					nextState(2,c);
				else{
					if(c=='t')
						nextState(4,c);
					else{
						if(c=='s')
							nextState(6,c);
						else
							return 1;
					}
				}
			break;
			case 2:
				if(c=='s'){
					reconocido(c,1,sizeof(char));
					return 0;
				}else
					return 1;
			break;
			case 4:
				if(c=='g'){
					reconocido(c,1,sizeof(char));
					return 0;
				}else
					return 1;
			break;
			case 6:
				if(c=='c'){
					reconocido(c,1,sizeof(char));
					return 0;
				}else
					return 1;
			break;
		}
	}
}
char atmtn(){
	iba=ib;
	state=0;
	while(1){
		c=bufer[iba++];
		switch(state){
			case 0:
					if(c=='+'||c=='-')
						nextState(1,c);
					else{
						if(isdigit(c))
							nextState(2,c);
						else
							return 1;
					}
			break;
			case 1:
					if(isdigit(c))
						nextState(2,c);
					else
						return 1;
			break;
			case 2:
				if(isdigit(c))
					nextState(2,c);
				else{
					if(c=='.')
						nextState(3,c);
					else{
						reconocido(c,0,sizeof(double));
						return 0;
					}
				}
			break;
			case 3:
				if(isdigit(c))
					nextState(3,c);
				else{
					reconocido(c,0,sizeof(double));
					return 0;
				}
			break;
		}
	}
}
char atmts(){
	iba=ib;
	state=0;
	while(1){
		c=bufer[iba++];
		switch(state){
			case 0:
				if(c=='s')
					nextState(1,c);
				else
					return 1;
			break;
			case 1:
				if(c=='e')
					nextState(2,c);
				else
					return 1;
			break;
			case 2:
				if(c=='n'){
					reconocido(c,1,sizeof(char));
					return 0;
				}else{
					if(c=='c'){
						reconocido(c,1,sizeof(char));
						return 0;
					}else
						return 1;
				}
			break;
		}
	}
}
char atmtsign(){
	iba=ib;
	state=0;
	c=bufer[iba++];
	if(c=='+'||c=='-'||c=='*'||c=='/'){
		reconocido(c,1,sizeof(char));
		return 0;
	}
	return 1;
}
char atmtt(){
	iba=ib;
	state=0;
	while(1){
		c=bufer[iba++];
		switch(state){
			case 0:
				if(c=='t')
					nextState(1,c);
				else
					return 1;
			break;
			case 1:
				if(c=='a')
					nextState(2,c);
				else
					return 1;
			break;
			case 2:
				if(c=='n'){
					reconocido(c,1,sizeof(char));
					return 0;
				}
				else
					return 1;
			break;
		}
	}
}
char atmtl(){
	iba=ib;
	state=0;
	while(1){
		c=bufer[iba++];
		switch(state){
			case 0:
				if(c=='l')
					nextState(1,c);
				else
					return 1;
			break;
			case 1:
				if(c=='o')
					nextState(2,c);
				else
					return 1;
			break;
			case 2:
				if(c=='g'){
					reconocido(c,1,sizeof(char));
					return 0;
				}
				else
					return 1;
			break;
		}
	}
}
char atmtsqrt(){
	iba=ib;
	state=0;
	while(1){
		c=bufer[iba++];
		switch(state){
			case 0:
				if(c=='s')
					nextState(1,c);
				else
					return 1;
			break;
			case 1:
				if(c=='q')
					nextState(2,c);
				else
					return 1;
			break;
			case 2:
				if(c=='r')
					nextState(3,c);
				else
					return 1;
			break;
			case 3:
				if(c=='t'){
					reconocido(c,1,sizeof(char));
					return 0;
				}else
					return 1;
			break;
		}
	}
}
void yylex(char *cad,list **psfjO){
	bufer=(char*)malloc(sizeof(char)*strlen(cad));
	token=NULL;
	strcpy(bufer,cad);
	int length=strlen(bufer);
	while(ib<length){
		if(atmtc()){
			if(atmtn()){
				freeToken();
				if(atmts()){
					if(atmtt()){
						if(atmtl()){
							if(atmtsqrt()){
								if(atmtsign()){
									ib++;
								}
							}
						}
					}
				}
			}
		}
	}
	emptyStack(psfjO);
}
#endif
