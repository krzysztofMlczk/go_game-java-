package com.tearsvalley.logic;

import java.util.ArrayList;

import com.tearsvalley.data.Board;
import com.tearsvalley.data.Color;
import com.tearsvalley.data.Field;
import com.tearsvalley.data.Point;

public class BoardService {

    private Board board;
    private GameRules gameRules;
    private Point ko;

    public int whitePoints = 0;
    public int blackPoints = 0;

    public BoardService(Board board) {
        this.board = board;
        this.gameRules = new GameRules(this);
    }

    public void clearCheckFlags() {
        for(int i = 0; i < 19; i++)
            for(int j = 0; j < 19; j++) {
                board.field[i][j].checked = false;         
        }
    }

    public boolean setStoneOnBoard(int x, int y, Color stoneColor) {

        clearCheckFlags();
        //firstly check if fiedl is occupied
        if(!board.field[x][y].occupied) {
            board.field[x][y].setStoneColor(stoneColor);

            if( ko != null && (x == ko.getX() && y == ko.getY())){
                board.field[x][y].removeStone();
                return false; 
            }
            //if it's not occupied than check if it is suicidal
            if(gameRules.checkBreaths(board.field[x][y])  || gameRules.isKilling(board.field[x][y])) {
                ko = new Point(-1,-1);
                return true;
            }
            else {
                board.field[x][y].removeStone();
                return false;
            }
        }
        else
            return false;
    }   

    public void removeStone(int x, int y) {
        board.field[x][y].removeStone();
    }

    public ArrayList<Point> getStonesToDelete(int x, int y, Color stoneColor) {
        ArrayList<Point> toDelete = gameRules.fieldToDelete(board.field[x][y]);

        for(Point p : toDelete) {

            if( board.field[p.getX()][p.getY()].getStoneColor() == Color.WHITE)
                blackPoints++;
            else if( board.field[p.getX()][p.getY()].getStoneColor() == Color.BLACK)
                whitePoints++;

            board.field[p.getX()][p.getY()].removeStone();
        }

        if(toDelete.size() == 1) {
            setKo(toDelete.get(0).getX(), toDelete.get(0).getY());
        }

        return toDelete;
    }
    
    
    public void setKo(int x, int y) {
    	this.ko = new Point(x, y);
    }
    
    public void setGameRules(GameRules gameRules) { 
    	this.gameRules = gameRules;
    }
    
    public GameRules getGameRules() {
    	return this.gameRules;
    }

    public ArrayList<Field> getNeighbors(int x, int y){
        ArrayList<Field> neighbor = new ArrayList<>();

        if(getNortField(x, y) != null)
            neighbor.add(getNortField(x, y));
        if(getEastField(x, y) != null) 
            neighbor.add(getEastField(x, y));
        if(getSouthField(x, y) != null) 
            neighbor.add(getSouthField(x, y));
        if(getWestField(x, y) != null) 
            neighbor.add(getWestField(x, y));

        return neighbor;
    }

    public Field getNortField(int x, int y) {
        if(x>0)
            return board.field[x-1][y];
        else  
            return null;
    }

    public Field getSouthField(int x, int y) {
        if(x<18)
            return board.field[x+1][y];
        else  
            return null;
    }

    public Field getWestField(int x, int y) {
        if(y>0)
            return board.field[x][y-1];
        else  
            return null;
    }

    public Field getEastField(int x, int y) {
        if(y<18)
            return board.field[x][y+1];
        else  
            return null;
    }

    public Board getBoard() {
        return this.board;
    }

    public int getTerritoryCounted(Color playerColor) {
            return gameRules.countTerritory(playerColor);
    }

    public int getScore(Color playerColor) {
        if(playerColor == Color.WHITE){
            whitePoints = whitePoints + getTerritoryCounted(playerColor);
            return whitePoints;
        }
        else if(playerColor == Color.BLACK) {
            blackPoints = blackPoints + getTerritoryCounted(playerColor);
            return blackPoints;
        }

        return -1;
    }
}