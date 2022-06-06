package com.example.reservet;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class tutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //타이틀바 없애기
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_tutorial);
        setTitle("앱 사용 도움말");

        Button btnenter = findViewById(R.id.btnenter);
        btnenter.setOnClickListener(new View.OnClickListener() { // 건너띄기 버튼 클릭시 메인화면으로 이동
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //메뉴 툴바
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //도움말 버튼
            case R.id.menu_help:
                finish();
                return true;

            case R.id.menu_tts:
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

}