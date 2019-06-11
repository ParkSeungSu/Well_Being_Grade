package com.example.ex07_grapic;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Gameover extends AppCompatActivity {
    MediaPlayer endsound;
    String dbname = "myDB";
    String tablename = "customer";
    String sql;
    SQLiteDatabase db;
    int point;                         //점수
    Button restart;
    Button rank;
    TextView pview;
    TextView grade;
    String sgrade;
    TextView name;
    String sname;
    String pstring;
    private String player_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent reciver = getIntent();
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바를 숨김
        db = openOrCreateDatabase(dbname, MODE_PRIVATE, null);   // 해당 DB가 있으면 열고 없으면 생성해라 ( DB이름, 개인모드, 에러핸들러 )
        sql = "create table if not exists " + tablename + " (name varchar(20), point varchar(20), grade varchar(20));";    // 테이블을 정의함
        db.execSQL(sql);   // db를 실행시키기 위해 사용 select 말고는 다 쓴다(delete, update, insert, create)
        if (reciver.getExtras() != null) {
            setContentView(R.layout.gameover);
            name = findViewById(R.id.pstring2);
            endsound = MediaPlayer.create(this, R.raw.endsound);//r게임 오버시 출력사운드
            endsound.start();
            point = reciver.getExtras().getInt("point");
            player_id = reciver.getStringExtra("id");
            name.setText(player_id);
            restart = findViewById(R.id.restart);
            rank = findViewById(R.id.Rank);
            pview = findViewById(R.id.pstring);
            pview.setText(Integer.toString(point));
            grade = findViewById(R.id.grade);
            pstring = Integer.toString(point);
            if (point <= 20) {
                grade.setText("F");
            }
            if (point > 20 && point < 30) {
                grade.setText("D");
            }
            if (point >= 30 && point < 40) {
                grade.setText("C");
            }
            if (point >= 40 && point < 50) {
                grade.setText("B");
            }
            if (point >= 50) {
                grade.setText("A");
            }
            sname = name.getText().toString();
            sgrade = grade.getText().toString();
            ImageView gover = findViewById(R.id.pfview);
            Glide.with(this).asGif().load(R.drawable.gameover).into(gover); //gif이미지 로드
            restart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sender = new Intent(getApplicationContext(), GameActivity.class);
                    sender.putExtra("point", 0);
                    sender.putExtra("id", player_id);
                    endsound.release();           //게임 화면으로 돌아가면 중지
                    startActivity(sender);
                    finish();
                }
            });
            rank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {   // DB 관련 코드는 항상 예외처리를 해준다!
                        sql = "insert into " + tablename + "(name, point, grade) values('" + sname + "', '" + pstring + "', '" + sgrade + "');"; // 레코드 삽입
                        db.execSQL(sql);   // 해당 SQL을 db에 실행
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), Rank.class);
                    startActivity(intent);

                }
            });
            //r게임 오버시 출력사운드
        }

    }
}
