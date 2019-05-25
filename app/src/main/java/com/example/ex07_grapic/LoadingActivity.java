package com.example.ex07_grapic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
