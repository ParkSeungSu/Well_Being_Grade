package com.example.ex07_grapic;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class Rank extends AppCompatActivity {
    String dbname = "myDB";
    String tablename = "customer";
    String sql;
    SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
    Cursor resultset;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
    ListView listView;   // ListView 객체 생성
    String[] result;   // ArrayAdapter에 넣을 배열 생성
    Button refresh;
    Button gomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        db = openOrCreateDatabase(dbname, MODE_PRIVATE, null);
        listView = findViewById(R.id.ranklist);
        refresh = findViewById(R.id.refresh);
        gomain = findViewById(R.id.gomain);
        final MyAdapter mMyAdapter = new MyAdapter();
        try {
            sql = "select name, point, grade from " + tablename + " ORDER BY point desc limit 10";  // 레코드 조회
            resultset = db.rawQuery(sql, null); // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)
            int count = resultset.getCount(); // db에 저장된 행 개수를 읽어온다
            result = new String[count];   // 저장된 행 개수만큼의 배열을 생성
            for (int i = 0; i < count; i++) {
                resultset.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
                String str_name = resultset.getString(0);  // 1번째~3번째 컬럼 값들 읽어 저장
                String str_point = Integer.toString(resultset.getInt(1));
                String str_grade = resultset.getString(2);
                result[i] = str_name + " " + str_point + " " + str_grade;
                mMyAdapter.addresult(result[i]);

            }
            System.out.println("select ok");

            // ArrayAdapter(this, 출력모양, 배열)

            listView.setAdapter(mMyAdapter);   // listView 객체에 Adapter를 붙인다


        } catch (Exception e) {
        }
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Rank.class);
                startActivity(intent);
                finish();

            }
        });
        gomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
