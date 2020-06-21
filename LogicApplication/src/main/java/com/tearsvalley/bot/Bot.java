package com.tearsvalley.bot;

import com.tearsvalley.data.Color;
import com.tearsvalley.data.Point;
import com.tearsvalley.logic.BoardService;

public class Bot {

    BoardService boardService;

    public Bot(BoardService boardService) {
        this.boardService = boardService;
    }

    public Point botMove() {
        Point toPlace = findBestAttack();
        boardService.setStoneOnBoard(toPlace.getX(), toPlace.getY(), Color.WHITE);
        return toPlace;
    }

    private Point findBestAttack() {
        int[][] bestFields = new int[19][19];
        int[] toReturn = new int[2];
        int maxcounter = 0;

        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++) {
                int counter = 0;
                if(boardService.setStoneOnBoard(i, j, Color.WHITE)) {
                    if(boardService.getNortField(i, j) != null) {
                        if (boardService.getNortField(i, j).getStoneColor() == Color.BLACK) {
                            counter++;
                        }
                    }
                    if(boardService.getSouthField(i, j) != null) {
                        if (boardService.getSouthField(i, j).getStoneColor() == Color.BLACK) {
                            counter++;
                        }
                    }
                    if(boardService.getEastField(i, j) != null) {
                        if (boardService.getEastField(i, j).getStoneColor() == Color.BLACK) {
                            counter++;
                        }
                    }
                    if(boardService.getWestField(i, j) != null) {
                        if (boardService.getWestField(i, j).getStoneColor() == Color.BLACK) {
                            counter++;
                        }
                    }

                    boardService.removeStone(i, j);
                }

                bestFields[i][j] = counter;
                if (counter > maxcounter){
                    maxcounter = counter;
                    toReturn[0] = i;
                    toReturn[1] = j;
                }
            }
        }
        return new Point(toReturn[0], toReturn[1]);
    }
}