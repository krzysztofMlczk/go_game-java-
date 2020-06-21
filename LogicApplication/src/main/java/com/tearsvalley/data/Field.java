package com.tearsvalley.data;

public class Field {
    private Color stoneColor;
    private int x;
    private int y;
    public boolean checked = false;
    public boolean occupied;
    
    public Field(int x, int y) {
        this.x = x;
        this.y = y;
        stoneColor = Color.NAC;
    }

    public void setStoneColor(Color stoneColor) {
        this.stoneColor = stoneColor;
        setOccupiedTrue();
    }
    public Color getStoneColor() {
        return stoneColor;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setOccupiedTrue() {
        this.occupied = true;
    }
    public void setOccupiedFalse() {
        this.occupied = false;
    }
    public boolean getOccupied() {
        return occupied;
    }


    public void removeStone() {
        this.stoneColor = Color.NAC;
        setOccupiedFalse();
    }
}