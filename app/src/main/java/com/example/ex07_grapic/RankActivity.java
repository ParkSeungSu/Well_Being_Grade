package com.example.ex07_grapic;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private String player_id; // intent로 받아온 id
    private int score; // intent로 받아온 point
    private myDatabase helper;
    private ArrayList arrayList; // id들의 목록이 들어갈 list.
    private String sql_insert = "INSERT INTO user VALUES(" + player_id + "," + score + ")"; // 삽입 sql문
    private String sql_update = "UPDATE user SET score=" + score + " WHERE id = " + player_id; // 업데이트 sql문
    private String sql_select = "SELECT * FROM user ORDER BY score DESC LIMIT 10"; // 상위 10개를 뽑아내는 sql문
    private Cursor cursor; // user 테이블을 관리할 커서
    private Cursor cursor2; // select로 상위 10개 테이블을 관리할 커서
    private TextView rank1;
    private TextView rank2;
    private TextView rank3;
    private TextView rank4;
    private TextView rank5;
    private TextView rank6;
    private TextView rank7;
    private TextView rank8;
    private TextView rank9;
    private TextView rank10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        arrayList = new ArrayList<String>();
        rank1 = findViewById(R.id.rank1);
        rank2 = findViewById(R.id.rank2);
        rank3 = findViewById(R.id.rank3);
        rank4 = findViewById(R.id.rank4);
        rank5 = findViewById(R.id.rank5);
        rank6 = findViewById(R.id.rank6);
        rank7 = findViewById(R.id.rank7);
        rank8 = findViewById(R.id.rank8);
        rank9 = findViewById(R.id.rank9);
        rank10 = findViewById(R.id.rank10);


        Intent reciver = getIntent();
        player_id = reciver.getExtras().getString("id"); // id 정보 받아오기
        score = reciver.getExtras().getInt("score"); // point 정보 받아오기

        helper = new myDatabase(this); // 테이블 생성
        database = helper.getReadableDatabase();
        cursor = database.rawQuery("select * from user", null);
        cursor.moveToFirst();

        Button refresh = findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    String id_value = cursor.getString(0);
                    int score_value = cursor.getInt(1);
                    arrayList.add(id_value);
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i) == player_id)
                        database.execSQL(sql_update);
                    else
                        database.execSQL(sql_insert);
                }
                cursor2 = database.rawQuery(sql_select, null);
                for (int i = 1; i <= cursor2.getCount(); i++) {
                    if (i == 1)
                        rank1.setText(cursor2.getString(0) + " : " + cursor2.getInt(1));
                    else if (i == 2)
                        rank2.setText(cursor2.getString(0) + " : " + cursor2.getInt(1));
                    else if (i == 3)
                        rank3.setText(cursor2.getString(0) + " : " + cursor2.getInt(1));
                    else if (i == 4)
                        rank4.setText(cursor2.getString(0) + " : " + cursor2.getInt(1));
                    else if (i == 5)
                        rank5.setText(cursor2.getString(0) + " : " + cursor2.getInt(1));
                    else if (i == 6)
                        rank6.setText(cursor2.getString(0) + " : " + cursor2.getInt(1));
                    else if (i == 7)
                        rank7.setText(cursor2.getString(0) + " : " + cursor2.getInt(1));
                    else if (i == 8)
                        rank8.setText(cursor2.getString(0) + " : " + cursor2.getInt(1));
                    else if (i == 9)
                        rank9.setText(cursor2.getString(0) + " : " + cursor2.getInt(1));
                    else if (i == 10)
                        rank10.setText(cursor2.getString(0) + " : " + cursor2.getInt(1));
                }
            }
        });


        Button goMain = findViewById(R.id.goMain);
        goMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RankActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
