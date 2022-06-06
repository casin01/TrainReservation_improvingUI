package com.example.reservet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reservet.ui.Docu;

import java.util.Objects;

public class Documentation extends AppCompatActivity {

    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentation);
        setTitle("종합 이용 안내");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        //recyclerview
        RecyclerView recyclerView = findViewById(R.id.recycleview);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                switch (pos) {
                    case Docu.TEL_HELP:
                        Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0215447788"));
                        startActivity(tel);
                        break;
                    case Docu.Guide_link:
                        startActivity(new Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse("https://www.youtube.com/watch?v=RBK-28kZ4C8")) // edit this url
                                .setPackage("com.google.android.youtube"));
                        break;
                    default:
                        Intent intent = new Intent(Documentation.this, Docu.class);
                        intent.putExtra("pos", pos);
                        intent.putExtra("title", adapter.getItem(pos));
                        startActivity(intent);
                        break;

                }


            }
        });

        recyclerView.setAdapter(adapter);

        //arraylist
        adapter.addItem("승차 이용 안내");
        adapter.addItem("환불 주의사항");
        //adapter.addItem("코레일톡 유튜브");    //코레일톡 예매 영상 유튜브 보기
        //adapter.addItem("코레일톡 전화");     //코레일톡 전화

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