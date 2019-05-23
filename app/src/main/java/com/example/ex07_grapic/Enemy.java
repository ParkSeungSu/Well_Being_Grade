package com.example.ex07_grapic;

public class Enemy {
    private int ex;//적 기체 좌표
    private  int ey;
    private int enemyGo=3;
    public Enemy(int ex,int ey) {
        this.ex=ex;
        this.ey=ey;
    }

    public int getEx() {
        return ex;
    }

    public void setEx(int ex) {
        this.ex = ex;
    }

    public int getEy() {
        return ey;
    }

    public void setEy(int ey) {
        this.ey = ey;
    }

    public  void changeGo(){
        enemyGo*=-1;
    }
    public int getEnemyGo(){
        return enemyGo;
    }
}
