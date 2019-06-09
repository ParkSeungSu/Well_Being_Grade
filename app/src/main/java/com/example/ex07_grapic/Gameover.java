package com.example.ex07_grapic;

import android.content.Intent;
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
    int point;                         //점수
    Button restart;
    Button gotoRanking;
    TextView pview;
    TextView grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent reciver = getIntent();
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바를 숨김
        if (reciver.getExtras() != null) {
            setContentView(R.layout.gameover);
            point = reciver.getIntExtra("point", 101);
            restart = findViewById(R.id.restart);
            gotoRanking = findViewById(R.id.rank);
            pview = findViewById(R.id.pstring);
            pview.setText(Integer.toString(point));
            grade = findViewById(R.id.grade);
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

            ImageView gover = findViewById(R.id.pfview);

            Glide.with(this).asGif().load(R.drawable.gameover).into(gover);

            endsound = MediaPlayer.create(this, R.raw.endsound);
            endsound.start();
            //r게임 오버시 출력사운드
            restart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sender = new Intent(getApplicationContext(), GameActivity.class);
                    sender.putExtra("point", 0);
                    endsound.release();           //게임 화면으로 돌아가면 중지
                    startActivity(sender);
                    finish();
                }
            });
            gotoRanking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //r게임 오버시 출력사운드
        }

    }
}
