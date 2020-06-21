package com.tearsvalley.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.tearsvalley.data.Board;
import com.tearsvalley.data.Color;
import com.tearsvalley.data.Field;
import com.tearsvalley.data.Point;

public class GameRules {

    BoardService boardService;
    ArrayList<Point> tmpFieldsToDelete = new ArrayList<>();
    ArrayList<Field> fieldToCount = new ArrayList<>();
    Color color = null;
    boolean toCount = true;


    public GameRules(BoardService boardService) {
        this.boardService = boardService;
    }

    //recursive function
    public boolean checkBreaths (Field field) {

        //get the neighbors 
        ArrayList <Field> neighbors = boardService.getNeighbors(field.getX(), field.getY());

        Color thisColor = field.getStoneColor();


        for(Field neighbor: neighbors) {
            Color currentNeighborColor = neighbor.getStoneColor();
            if(currentNeighborColor == Color.NAC) {
                field.checked = true; //we want o avoid being invoked in recursive function by our neighbor
                return true; //if one of the neighbors has a breath than currently checked field also
            }

            else if(thisColor == currentNeighborColor && neighbor.checked == false) {
                field.checked = true;
                if(checkBreaths(neighbor))
                    return true;
            }
        }
        return false; //if we didn't return true in any case it means that every same color neighbor didnt return true either

    }   

    public boolean checkDelete (Field field) {

        field.checked = true;
        //get the neighbors 
        ArrayList <Field> neighbors = boardService.getNeighbors(field.getX(), field.getY());

        Color thisColor = field.getStoneColor();

        for(Field neighbor: neighbors) {
            Color currentNeighborColor = neighbor.getStoneColor();
            if(currentNeighborColor == Color.NAC) {
                //field.checked = true; //we want o avoid being invoked in recursive function by our neighbor
                tmpFieldsToDelete.clear();
                return true; //if one of the neighbors has a breath than currently checked field also
            }

            else if(thisColor == currentNeighborColor && neighbor.checked == false) {
                //field.checked = true;
                if(checkDelete(neighbor)) {
                    tmpFieldsToDelete.clear();
                    return true;
                }
            }
        }
        tmpFieldsToDelete.add(new Point(field.getX(), field.getY()));
        return false; //if we didn't return true in any case it means that every same color neighbor didnt return true either
    }  

    public ArrayList<Point> fieldToDelete(Field field) {
        
        ArrayList<Point> toDelete = new ArrayList<>();
        tmpFieldsToDelete.clear();
        
        Color color = field.getStoneColor();
         
        ArrayList<Field> n = boardService.getNeighbors(field.getX(), field.getY());

        for(Field f: n) {
            tmpFieldsToDelete.clear();
            boardService.clearCheckFlags();
            if(color != f.getStoneColor() && f.getStoneColor() != Color.NAC) {
                if(!checkDelete(f)) {
                    toDelete.addAll(tmpFieldsToDelete);
                    tmpFieldsToDelete.clear();
                }
            }
        }
        
        return toDelete;
    }

    public boolean isKilling(Field field) {
        
        ArrayList<Field> n = boardService.getNeighbors(field.getX(), field.getY());

        Color color = field.getStoneColor();

        for(Field f: n) {
            if(color != f.getStoneColor() && f.getStoneColor() != Color.NAC) {
                if(!checkBreaths(f)) {

                    tmpFieldsToDelete.clear();
                    return true;
                }
            }
        }
        tmpFieldsToDelete.clear();
        return false;
    }


    public void countInnerPoints(Field field) {

        field.checked = true;
        //get the neighbors 
        ArrayList <Field> neighbors = boardService.getNeighbors(field.getX(), field.getY());
        //fieldToCount.add(field);

        fieldToCount.add(field);

        for(Field neighbor: neighbors) {
            Color currentNeighborColor = neighbor.getStoneColor();
            //if neighbor has the same color as this player, than it might be his territory so return true
            
            if(currentNeighborColor == Color.NAC && neighbor.checked == false) {
                countInnerPoints(neighbor);
            } else if(currentNeighborColor != color && currentNeighborColor != Color.NAC) {
                toCount = false;
            } 

        }

    }

    public int countTerritory(Color playerColor) {

        boardService.clearCheckFlags();
        color = playerColor;
        int terrain = 0;

        for(int i = 0 ; i < 19; i++) {
            for(int j = 0; j < 19; j++) {
                fieldToCount.clear();
                Field currentField = boardService.getBoard().field[i][j];
                if(currentField.checked == false && currentField.getStoneColor() == Color.NAC) {
                    //invoke the recursive function on the first empty field
                    countInnerPoints(currentField);
                    if (toCount == true) {
                        terrain += fieldToCount.size();
    
                    }
                    toCount = true;
                }
            }
        }

        System.out.println(playerColor+" territory "+terrain);
        return terrain;
    }

}