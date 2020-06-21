package com.tearsvalley.data;

public class Board {
    private final int boardSize = 19;
    public Field[][] field;

    public Board() {
        field = new Field[boardSize][boardSize];
        for(int i = 0; i<boardSize; i++) {
            for(int j = 0; j<boardSize; j++) {
                field[i][j] = new Field(i, j);
            }
        }
    }
}