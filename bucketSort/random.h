#include <stdlib.h>
#ifndef __RANDOM_H__
#define __RANDOM_H__
void putSemilla(unsigned int semilla){
	srand(semilla);
}
int getRandom(){
	return ((999.0*random())/(RAND_MAX));
}
#endif
