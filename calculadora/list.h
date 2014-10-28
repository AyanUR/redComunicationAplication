#include <stdlib.h>
#include <stdio.h>
#ifndef __LIST_H__
#define __LIST_H__
typedef struct list{
	void *object;
	int nb;
	struct list *next,*before;
}list;
char add(list **head,void *object,int nb){
	list *nvo,*p;
	if((nvo=(list*)malloc(sizeof(list)))==NULL)return -1;
	nvo->object=object;
	nvo->nb=nb;
	nvo->next=nvo->before=NULL;
	if(*head==NULL)
		*head=nvo;
	else{
		for(p=*head;p->next!=NULL;p=p->next);
		p->next=nvo;
		nvo->before=p;
	}
	return 0;
}
void rmElement(list **head){
	list *p,*q;
	if(*head!=NULL){
		p=(*head)->before;
		q=(*head)->next;
		free(*head);
		*head=NULL;
		if(p!=NULL){
			p->next=q;
			*head=p;
		}
		if(q!=NULL){
			q->before=p;
			*head=q;
		}
		if(*head!=NULL)
			for(;(*head)->before!=NULL;*head=(*head)->before);
	}
}
void* show(list *head){
	if(head!=NULL)
		return head->object;
	return NULL;
}
void printList(list *head){
	printf("\n");
	for(;head!=NULL;head=head->next){
		if(head->nb==sizeof(double))
			printf("\t%f",*((double*)head->object));
		else{
			if(head->nb==sizeof(char))
				printf("\t%s",((char*)head->object));
			else
				printf("\nerror type dont found");
		}
	}
}
#endif
