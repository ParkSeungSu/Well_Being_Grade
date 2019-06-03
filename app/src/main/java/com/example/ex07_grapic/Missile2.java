package com.example.ex07_grapic;

public class Missile2 {
    private int mx2;//미사일의 xy좌표
    private int my2;

    public Missile2(int mx2, int my2) {
        this.mx2 = mx2;
        this.my2 = my2;
    }//생성자

    public int getMx() {
        return mx2;
    }

    public void setMx(int mx2) {
        this.mx2 = mx2;
    }

    public int getMy() {
        return my2;
    }

    public void setMy(int my2) {
        this.my2 = my2;
    }       //getter, setter 정의
}
