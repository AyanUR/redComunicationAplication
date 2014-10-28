#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <unistd.h>
#include "random.h"
#ifndef __BUCKET_H__
#define __BUCKET_H__
typedef struct bucket{
	int limitUp,limitDown,top,*numbers;
	struct bucket *next;
}bucket;
int build(bucket **cab,int numberBuckets,int rangUp,int rangDown){//numberbucket numero de cubetas
   int i,numbersForBucket=(rangUp-rangDown+1)/numberBuckets;
	if(((rangUp-rangDown+1)%numberBuckets)!=0)
		numbersForBucket++;
   bucket *nvo,*p;
   for(i=0;i<numberBuckets;i++){
		if((nvo=(bucket*)malloc(sizeof(bucket)))==NULL)
			return i;
		nvo->limitDown=rangDown;
		if((rangDown+=numbersForBucket)>rangUp)
			nvo->limitUp=rangUp;
		else
			nvo->limitUp=rangDown-1;
		//printf("\nbucke %d \t limitDown=%d limitUp=%d",i+1,nvo->limitDown,nvo->limitUp);
		nvo->top=0;
		nvo->next=NULL;
		if((nvo->numbers=(int*)malloc(sizeof(int)*numbersForBucket))==NULL)
			return i;
		if(*cab==NULL)
			*cab=nvo;
		else{
			for(p=*cab;p->next!=NULL;p=p->next){;}
			p->next=nvo;
		}
   }
	return i-1;
}
int wasNumber(int *list,int top,int number){
	int i;
	for(i=0;i<top;i++){
		if(list[i]==number)
			return list[i];
	}
	return -1;
}
void insertNumbers(bucket **cab,int numbersTotal){//numbersTotal total de numero aleatorios a crear
	int i,random;
	bucket *p;
	putSemilla(getpid());
	for(i=0;i<numbersTotal;i++){
		random=getRandom();
		for(p=*cab;p!=NULL;p=p->next){
			if(random<=p->limitUp&&random>=p->limitDown){
				if(wasNumber(p->numbers,p->top,random)==-1){
					p->numbers[p->top++]=random;
				}
			}
		}
	}
}
void printNumbers(bucket *bucket){
	int i;
	for(i=0;i<bucket->top;i++)
		printf(" %d ",bucket->numbers[i]);
	puts("\n->->->->->->->->->->->->->->->->->->->->->->->->->->");	
}
#endif
