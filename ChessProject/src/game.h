#ifndef __GAME_H
#define __GAME_H

#include "ChessProject.h"

typedef enum {

	WHITE2PLAY,
	BLACK2PLAY,
	PAUSED,
	WHITEWINS,
	BLACKWINS

} GAME_STATE;

MENU_STATE game_management();

MENU_STATE menu_management();

GAME_STATE getGameState();

void turnGameState();

void winnerState();

void decrement(int *counter);

void increment(int *counter);

void reset();

int waitForEnter();


#endif /* __GAME_H */
