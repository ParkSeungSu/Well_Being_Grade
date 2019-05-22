package com.example.ex07_grapic;

public class Missile {
    private int mx;//미사일의 xy좌표
    private int my;

    public Missile(int mx,int my){
        this.mx=mx;
        this.my=my;
    }//생성자

    public int getMx() {
        return mx;
    }

    public void setMx(int mx) {
        this.mx = mx;
    }

    public int getMy() {
        return my;
    }

    public void setMy(int my) {
        this.my = my;
    }       //getter, setter 정의

}
