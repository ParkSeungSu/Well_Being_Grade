package com.example.ex07_grapic;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class LoadingActivity extends AppCompatActivity {
    String dbname = "myDB";
    String tablename = "customer";
    String sql;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = openOrCreateDatabase(dbname, MODE_PRIVATE, null);   // 해당 DB가 있으면 열고 없으면 생성해라 ( DB이름, 개인모드, 에러핸들러 )
        sql = "create table if not exists " + tablename + " (name varchar(20), point varchar(20), grade varchar(20));";    // 테이블을 정의함
        db.execSQL(sql);   // db를 실행시키기 위해 사용 select 말고는 다 쓴다(delete, update, insert, create)
        setContentView(R.layout.loading);

        ImageView loading = findViewById(R.id.imageView);
        Glide.with(this).asGif().override(300).load(R.drawable.loading).into(loading);
        thread_sleep sleep = new thread_sleep(this);
        sleep.start();


    }

    class thread_sleep extends Thread {
        Activity thisAct;

        thread_sleep(Activity theAct) {
            thisAct = theAct;
        }

        public void run() {
            try {
                sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(thisAct, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
