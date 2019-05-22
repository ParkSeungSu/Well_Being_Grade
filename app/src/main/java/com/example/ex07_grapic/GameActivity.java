package com.example.ex07_grapic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyView view = new MyView(this);
        view.setFocusable(true); //키 이벤트를 받을 수 있도록 설정
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바를 숨김
        setContentView(view); //xml이 아닌 내부뷰 (커스텀 뷰)로 화면 이용
    }
    //내부 클래스
    class MyView extends View implements Runnable{

        Drawable backImg;    //배경
        Drawable gunship;    //사용자 비행기 이미지
        Drawable missile;    //총알 이미지
        Drawable enemy;      //적 이미지
        Drawable explousure; //폭발이미지
        // SoundPool 사운드(1m), MediaPlayer 사운드(1m이상),동영상

        MediaPlayer fire;    //발사음
        MediaPlayer hit;     //타격음
        int eCheck;            //적 기체 번호
        int width, height;   //화면 가로,세로
        int gunshipWidth, gunshipHeight;  //사용자 비행기 가로,세로
        int missileWidth, missileHeight;     //미사일 가로,세로
        int enemyWidth, enemyHeight;        //적 가로, 세로
        int hitWidth, hitHeight;             //폭발 이미지 가로, 세로
        int x,y;                             //비행기좌표
        int mx,my;                           //미사일좌표
        int ex,ey;                           //적좌표
        int hx,hy;                           //폭발좌표
        int point=0;                         //점수
        boolean isFire;                      //총알발사 여부
        boolean isHit;                       //폭발 여부
        List<Missile> mlist;                 //총알 리스트
        List<Enemy>elist;                    //적 리스트
        //생성자
        public MyView(Context context) {
            super(context);
            //img생성
            backImg=getResources().getDrawable(R.drawable.back0);
            gunship=getResources().getDrawable(R.drawable.gunship);
            missile=getResources().getDrawable(R.drawable.missile);
            enemy=getResources().getDrawable(R.drawable.enemy);
            explousure=getResources().getDrawable(R.drawable.hit);
            //sound생성
            fire=MediaPlayer.create(GameActivity.this,R.raw.fire);
            hit=MediaPlayer.create(GameActivity.this,R.raw.hit);
            //리스트 생성
            mlist=new ArrayList<>();
            elist=new ArrayList<>();
            //백그라운드 스레드 생성 -> 그러면 run이 돌아간다.
            Thread th = new Thread(this);

            th.start();
        }

        @Override
        public void run() {
            while (true){
                //적 좌표
                //사용자의 사각영역
                Rect rectG=new Rect(x,y,x+gunshipWidth,y+gunshipHeight);

                for (int enemy = 0;enemy < mlist.size();enemy++){

                    Enemy e= elist.get(enemy);  //i번째 적    적을 생성 하게 되면 어레이리스트에 계속 쌓인다.
                    e.setEx(e.getEy()-3);  //x좌표 움직임
                    e.setEy(e.getEy()+3);
                    eCheck=enemy;
                    if (e.getEx()<0){
                       e.setEx(width-5);
                            //x좌표가
                    }
                    if(e.getEy()>height){  //y좌표가 맨 아래로 내려오면 리스트에서 제거()
                        e.setEy(0);
                    }
                    Rect rectE=new Rect(e.getEx(),e.getEy(),e.getEx()+enemyWidth,e.getEy()+enemyHeight);
                    if(rectG.intersect(rectE)){ //적기와 내가 박았다?
                        hit.start();
                        isHit = true;
                        hx = e.getEx();
                        hy = e.getEy();//폭발한 x,y좌표 저장
                        break;//일단 사용자가 박으면 프로그램 자체가 멈추게 함
                    }


                }

                //미사일 좌표
                for (int i = 0;i < mlist.size();i++){
                    Missile m= mlist.get(i);  //i번째 총알    총알을 발사하게 되면 어레이리스트에 계속 쌓인다.
                    m.setMy(m.getMy()-10);  //y좌표 감소 처리
                    if (m.getMy()<0){
                        mlist.remove(i);
                        //y좌표가 0보다 작으면 리스트에서 제거(총알이 맨 위에까지 올라가면)
                    }

                    //충돌여부 판정

                    //총알의 사각영역
                    Rect rectM=new Rect(m.getMx(),m.getMy(),m.getMx()+missileWidth,m.getMy()+missileHeight);
                    Enemy e=elist.get(eCheck);
                    Rect rectE=new Rect(e.getEx(),e.getEy(),e.getEx()+enemyWidth,e.getEy()+enemyHeight);
                    //겹치는 걸로 충돌 판정
                    if(rectE.intersect(rectM)){  //겹쳐졌다?=>충돌
                        hit.start();             //폭발음 플레이
                        isHit = true;            //폭발 상태로 변경
                        point += 1;              //점수 증가
                        hx = ex;
                        hy = ey;                 //폭발한 x,y좌표 저장
                        mlist.remove(i); //총알 리스트에서 총알을 제거
                        elist.remove(eCheck);
                       /* ex = random.nextInt(width - enemyWidth)+1;
                        ey = 0; //적 좌표 초기화*/
                    }

                }
                try {
                    Thread.sleep(30);               //잠깐 화면을 멈추고
                }catch (Exception e){
                    e.printStackTrace();
                }
                postInvalidate(); //화면 갱신 요청 => onDraw()가 호출됨 그림을 다시 새로 스아악 그린다.
            }


        }
        //화면 사이즈가 변경될 때( 최초 가로, 최초 세로, 전환 가로, 전환 세로)
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            Random random=new Random();
            //화면의 가로,세로 폰을 기준으로 맞춘다.
            width=getWidth();
            height=getHeight();

            //이미지의 가로,세로 길이
            gunshipWidth=gunship.getIntrinsicWidth();
            gunshipHeight=gunship.getIntrinsicHeight();
            missileWidth=missile.getIntrinsicWidth();
            missileHeight=missile.getIntrinsicHeight();
            enemyWidth=enemy.getIntrinsicWidth();
            enemyHeight=enemy.getIntrinsicHeight();
            hitWidth=explousure.getIntrinsicWidth();
            hitHeight=explousure.getIntrinsicHeight();

            //비행기 좌표
            x = (width/2) - (gunshipWidth/2);   //정중앙
            y = height - 50;

            //미사일 초기좌표
            mx = x+20;
            my = y;

            //적 초기 좌표
            ex = random.nextInt(width - enemyWidth)+1;
            ey = 0;
            //초기에 적 한마리 생성
            Enemy e = new Enemy(ex,ey);
            elist.add(e);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //배경 이미지 출력
            //setBounds(x1,y1,x2,y2) 영역 지정
            backImg.setBounds(0,0,width,height);
            backImg.draw(canvas); // 배경을 캔버스에 출력
            //사용자 비행기 출력
            gunship.setBounds(x,y,x+gunshipWidth,y+gunshipHeight);
            gunship.draw(canvas);
            //적 출력
            if(isHit) {      //폭발 상태
                //폭발 이미지 출력
                explousure.setBounds(hx - 20, hy - 20, hx + hitWidth - 20, hy + hitHeight - 20);
                explousure.draw(canvas);
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isHit = false; //폭발하지 않은 상태로 전환
            }else {
                //폭발하지 않은 상태
                for(int i=0;i<elist.size();i++) {
                    Enemy e=elist.get(i);
                    enemy.setBounds(e.getEx(), e.getEy(), e.getEx() + enemyWidth, e.getEy() + enemyHeight);
                    enemy.draw(canvas);

                }
            }
            //총알 출력
            for (int i = 0; i< mlist.size();i++){
                Missile m=mlist.get(i);      //i번쨰 총알
                missile.setBounds(m.getMx(),m.getMy(),m.getMx()+missileWidth,m.getMy()+missileHeight); //총알 이미지 출력 범위
                missile.draw(canvas);      // i번째 총알 츨력
            }


            //점수 출력
            String str = "POINT : "+ point;
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);      //폰트 사이즈
            canvas.drawText(str,width/2,40,paint);



               // Enemy em = new Enemy(random.nextInt(width),ey);
                //elist.add(em);

            super.onDraw(canvas);

        }

        //키 이벤트 처리

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            isFire = true;  //발사로 전환
            fire.start();   //발사 소리 출력
            Missile ms = new Missile(x+gunshipWidth/2,y);
            mlist.add(ms);
            if(event.getX()<(width/2)){
                x-=20;
                x=Math.max(20,x);   //큰값
            }
            else if(event.getX()>(width/2)) {
                x+=20;
                x=Math.min(width-20,x);
            }
            postInvalidate();
            return super.onTouchEvent(event);
        }
    }
}
