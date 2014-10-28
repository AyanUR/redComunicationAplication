#include <stdlib.h>
#ifndef __STACK_H__
#define __STACK_H__
typedef struct stack{
	void *object;
	int nb;
	struct stack *down;
}stack;
char push(stack **top,void *object,int nb){
	stack *nvo;
	if((nvo=(stack*)malloc(sizeof(stack)))==NULL)return -1;
	nvo->object=object;
	nvo->nb=nb;
	nvo->down=*top;
	*top=nvo;
	return 0;
}
void pop(stack **top){
	if(*top!=NULL){
		stack *temp=*top;
		*top=(*top)->down;
		free(temp);
	}
}
void* peek(stack *top){
	if(top!=NULL)
		return top->object;
	return NULL;
}
void printStack(stack *top){
	printf("\n");
	for(;top!=NULL;top=top->down){
		if(top->nb==sizeof(double))
			printf("\t%f",*((float*)top->object));
		else{
			if(top->nb==sizeof(char))
				printf("\t%s",((char*)top->object));
			else
				printf("\nerror type dont found");
		}
	}
}
#endif
