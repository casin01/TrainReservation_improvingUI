package com.example.reservet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Objects;

public class Seemytrain extends AppCompatActivity {
    private ListView listview2 = null;
    private MyListviewAdapter adapter2 = null;
    private AllInfo myinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seemytrain);
        setTitle("내 승차권 조회 및 취소");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "",
                "로딩 중", true); // ProgressDialog 시작

        //아이디, 비밀번호 받아옴
        Intent intent =getIntent();
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");

        //리스트뷰
        listview2 = findViewById(R.id.listview2);
        adapter2 = new MyListviewAdapter(Seemytrain.this);

        //조회
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Log.e("jsonjsonjson", "응답-> " + response);
                    //GSON
                    Gson gson = new Gson();
                    TicketTable tickettable = gson.fromJson(response, TicketTable.class);
                    adapter2.setItems(tickettable.tickettable);
                    adapter2.notifyDataSetChanged();
                    Log.e("MyMsg", "조회 성공");
                    listview2.setAdapter(adapter2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        SeeRequest seeRequest = new SeeRequest(userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Seemytrain.this);
        queue.add(seeRequest);

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myinfo = (AllInfo) adapter2.getItem(position);

                Intent intent= new Intent(Seemytrain.this, DetailInfo.class);
                intent.putExtra("myinfo", myinfo);
                startActivity(intent);
            }
        });

        dialog.dismiss(); // 프로그레스 다이얼로그 종료
    }

    //툴바 뒤로가기
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}