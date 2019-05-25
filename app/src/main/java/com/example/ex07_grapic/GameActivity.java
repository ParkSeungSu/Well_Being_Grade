package com.example.ex07_grapic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    Drawable backImg;    //배경
    Drawable gunship;    //사용자 비행기 이미지
    Drawable missile;    //총알 이미지
    Drawable enemy;      //적 이미지
    Drawable explousure; //폭발이미지
    // SoundPool 사운드(1m), MediaPlayer 사운드(1m이상),동영상

    MediaPlayer fire;    //발사음
    MediaPlayer hit;     //타격음
    MediaPlayer bgmusic; //배경음악

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
    boolean gameover;
    String tag;


    List<Missile> mlist;                 //총알 리스트
    List<Enemy>elist;                    //적 리스트
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameover = false;
        Intent reciver = getIntent();
        if (reciver.getExtras() != null) {
            gameover = reciver.getExtras().getBoolean("state");
        }
        if (gameover) {
            GameOverView gameOverView = new GameOverView(this);
            requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바를 숨김
            setContentView(gameOverView);
            gameOverView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sender = new Intent(GameActivity.this, GameActivity.class);
                    sender.putExtra("state", false);
                    startActivity(sender);
                }
            });
        } else {
            MyView view = new MyView(this);
            view.setFocusable(true); //키 이벤트를 받을 수 있도록 설정
            requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바를 숨김
            setContentView(view);

        }
        //xml이 아닌 내부뷰 (커스텀 뷰)로 화면 이용

    }

    //gameover뷰
    class GameOverView extends View {
        Drawable overBg;

        public GameOverView(Context context) {
            super(context);
            overBg = getResources().getDrawable(R.drawable.gameover);

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            //화면의 가로,세로 폰을 기준으로 맞춘다.
            width = getWidth();
            height = getHeight();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            overBg.setBounds(0, 0, width, height);
            overBg.draw(canvas);
            //점수 출력
            String strGrad;
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(150);//폰트 사이즈
            if (point < 100) {
                strGrad = "F";
                canvas.drawText(strGrad, width - (width / 4), height / 4, paint);
            } else if (point >= 100 && point < 150) {
                strGrad = "D";
                canvas.drawText(strGrad, width - (width / 4), height / 4, paint);
            } else if (point >= 150 && point < 200) {
                strGrad = "C";
                canvas.drawText(strGrad, width - (width / 4), height / 4, paint);
            } else if (point >= 200 && point < 250) {
                strGrad = "B";
                canvas.drawText(strGrad, width - (width / 4), height / 4, paint);
            } else if (point >= 250) {
                strGrad = "A";
                canvas.drawText(strGrad, width - (width / 4), height / 4, paint);
            }
            super.onDraw(canvas);
        }

    }


    //내부 클래스
    class MyView extends View implements Runnable{
        boolean stopped=false;
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
            bgmusic = MediaPlayer.create(GameActivity.this, R.raw.gamemusic);
            //리스트 생성
            mlist=new ArrayList<>();
            elist=new ArrayList<>();
            //백그라운드 스레드 생성 -> 그러면 run이 돌아간다.
            Thread th = new Thread(this);
            bgmusic.setLooping(true);
            bgmusic.start();
            th.start();
        }

        @Override
        public void run() {
            while (!stopped){
                Log.d(tag,"스레드시작");

                //적 좌표
                //사용자의 사각영역
                Rect rectG=new Rect(x,y,x+gunshipWidth,y+gunshipHeight);
                try {Log.d(tag,"적기 이동시작");
                        for (int enemy = 0; enemy < elist.size(); enemy++) {

                            Enemy e = elist.get(enemy);
                            //i번째 적    적을 생성 하게 되면 어레이리스트에 계속 쌓인다.
                            e.setEx(e.getEx()+e.getEnemyGo());
                            e.setEy(e.getEy() + e.getEnemyDown());
                            if (e.getEx() > width - enemyWidth) {
                                    e.changeGo();//enemygo값이 -1을 곱한값이 됨 = 방향전환
                                    //x좌표가 우측벽에 닿으면
                            }
                            if (e.getEx()<=0){
                                    e.changeGo();
                                    //x좌표가 좌측벽에 닿으면 방향전환
                            }
                            if (e.getEy() > height - enemyHeight) {  //y좌표가 맨 아래로 내려오면 다시 위로
                                e.setEy(ey);
                                e.changeDown();
                            }
                            Rect rectE = new Rect(e.getEx(), e.getEy(), e.getEx() + enemyWidth, e.getEy() + enemyHeight);
                            if (rectG.intersect(rectE)) { //적기와 내가 박았다?
                                hit.start();
                                isHit = true;
                                hx = x;
                                hy = y;//폭발한 x,y좌표 저장
                                stop();//일단 사용자가 박으면 스레드,배경 음악을 멈추게 함
                            }
                        }
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                Log.d(tag,"미사일 이동");
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
                    try {
                        for (int e=0;e<elist.size();e++){
                            Enemy eCheck = elist.get(e);
                            Rect rectE = new Rect(eCheck.getEx(), eCheck.getEy(), eCheck.getEx() + enemyWidth, eCheck.getEy() + enemyHeight);
                        //겹치는 걸로 충돌 판정
                            if (rectE.intersect(rectM)) {  //겹쳐졌다?=>충돌
                                hit.start();             //폭발음 플레이
                                isHit = true;            //폭발 상태로 변경
                                point += 1;              //점수 증가
                                hx = eCheck.getEx();
                                hy = eCheck.getEy();
                            //폭발한 x,y좌표 저장
                                if(mlist.get(i)!=null) {
                                    mlist.remove(i);
                                }
                                //총알 리스트에서 총알을 제거
                                if(elist.get(e)!=null){
                                elist.remove(e);}

                        }
                    }
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }

                }
                try {
                    Thread.sleep(30);               //잠깐 화면을 멈추고
                }catch (Exception e){
                    e.printStackTrace();
                }
                postInvalidate();
                Log.d(tag,"갱신");//화면 갱신 요청 => onDraw()가 호출됨 그림을 다시 새로 스아악 그린다.
            }


        }
        public void stop(){
            bgmusic.release();
            Intent sender = new Intent(GameActivity.this, GameActivity.class);
            sender.putExtra("state", true);
            startActivity(sender);

        }
        //화면 사이즈가 변경될 때( 최초 가로, 최초 세로, 전환 가로, 전환 세로)
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            Log.d(tag,"사이즈 체인지 시작");
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
            y = height - gunshipHeight;

            //미사일 초기좌표
            mx = x+20;
            my = y;
            //적 초기 좌표
            ey = 0;
            //초기에 적 한마리 생성
            if (elist.size() <= 0) {
                Random random = new Random();
                Enemy e = new Enemy(random.nextInt(width - enemyWidth) + 1, ey);
                elist.add(e);
            }
            Log.d(tag,"사이즈 체인지 끝");

        }

        @Override
        protected  void onDraw(Canvas canvas) {
            Log.d(tag,"드로우 시작");

            //배경 이미지 출력
            //setBounds(x1,y1,x2,y2) 영역 지정
            backImg.setBounds(0,0,width,height);
            backImg.draw(canvas); // 배경을 캔버스에 출력
            //사용자 비행기 출력
            gunship.setBounds(x, y, x + gunshipWidth, y + gunshipHeight);
            gunship.draw(canvas);

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
            }
            //적 생성
            if (elist.size() <= 0) {
                for (int j = 0; j <= point / 5; j++) {
                    Random random = new Random();
                    Enemy enemy = new Enemy(random.nextInt(width - enemyWidth) + 1, ey);
                    if (j % 2 != 1) {
                        enemy.changeGo();
                    }
                    if (point != 0 && point % 2 == 0) {
                        enemy.changeSetDown((point / 2));
                    }
                    elist.add(enemy);
                }//적 기체가 완전히 없어졌을때 다시 점수에 비례해서 그림
            }
            //적 출력
            for(int i=0;i<elist.size();i++){
                Enemy e=elist.get(i);
                enemy.setBounds(e.getEx(), e.getEy(), e.getEx() + enemyWidth, e.getEy() + enemyHeight);
                enemy.draw(canvas);
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
            Log.d(tag,"드로우 끝");

            super.onDraw(canvas);
        }
        //키 이벤트 처리

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.d(tag,"터치");

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
//그냥 커밋 잘되나 테스트