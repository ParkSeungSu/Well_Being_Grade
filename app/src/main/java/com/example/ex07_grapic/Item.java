package com.example.ex07_grapic;

public class Item {
    private int ix;
    private int iy;
    private int state;

    public Item(int ix, int iy, int state) {
        this.ix = ix;
        this.iy = iy;
        this.state = state;
    }

    public int getIx() {
        return ix;
    }

    public void setIx(int ix) {
        this.ix = ix;
    }

    public int getIy() {
        return iy;
    }

    public void setIy(int iy) {
        this.iy = iy;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
