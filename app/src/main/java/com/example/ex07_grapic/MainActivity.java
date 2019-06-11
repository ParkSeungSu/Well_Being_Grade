package com.example.ex07_grapic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mainMusic;
    private Button Gstart;
    private EditText playerName;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerName = findViewById(R.id.player_id);
        mainMusic = MediaPlayer.create(MainActivity.this, R.raw.mainmusic);
        mainMusic.start();
        Gstart = findViewById(R.id.start);

        Gstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                id = playerName.getText().toString();
                intent.putExtra("id", id);
                startActivity(intent);
                mainMusic.release();

            }
        });

    }
}
