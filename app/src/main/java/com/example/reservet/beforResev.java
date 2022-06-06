package com.example.reservet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.Locale;
import java.util.Objects;

import static android.net.wifi.p2p.WifiP2pManager.ERROR;

public class beforResev extends AppCompatActivity {

    //받아온 데이터
    private UserInfo userinfo;
    private StationInfo mStartStation, mEndStation;
    private String date, weekday;
    private int numseat;
    private TrainTicketInfo ticket;
    private int totalcharge;
    private int diswon, adult;
    private TextView totchrtxt, discounttxt;
    TextView alrtxt1, alrtxt2;
    View ani1, ani2;
    private boolean onhelp=false;
    private boolean ontts =false;
    private TextToSpeech tts;       // TTS 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_befor_resev);
        setTitle("승차권 상세 정보 확인");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        //데이터 받아오기
        Intent intent = getIntent();
        userinfo = (UserInfo) intent.getSerializableExtra("userinfo");
        mStartStation = (StationInfo) intent.getSerializableExtra("mStartStation");
        mEndStation = (StationInfo) intent.getSerializableExtra("mEndStation");
        date = intent.getStringExtra("date");
        weekday = intent.getStringExtra("weekday");
        numseat = intent.getIntExtra("numseat", 1);
        ticket = (TrainTicketInfo) intent.getSerializableExtra("ticket");


        oninit();
    }

    ActivityResultLauncher<Intent> dialogResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        //문제있으면 앱 강제종료
                        assert data != null;
                        diswon = data.getIntExtra("totaldis", 0);
                        discounttxt.setText("(-) "+ diswon+" 원");
                        totalcharge = numseat * adult - diswon;
                        totchrtxt.setText(totalcharge + " 원");
                        Log.e("beforeReserv", "할인: " +diswon);

                    }
                }
            });

    void oninit() {
        //xml
        TextView depstn = findViewById(R.id.depstn);
        TextView deptime = findViewById(R.id.deptime);
        TextView arrstn = findViewById(R.id.arrstn);
        TextView arrtime = findViewById(R.id.arrtime);
        TextView todate = findViewById(R.id.todate);
        TextView charge1 = findViewById(R.id.charge1);
        TextView charge2 = findViewById(R.id.charge2);
        totchrtxt = findViewById(R.id.totchrtxt);
        TextView traintext = findViewById(R.id.traintext);
        discounttxt =findViewById(R.id.discounttxt);
        Button infobtn =findViewById(R.id.infobtn);
        ani1= findViewById(R.id.ani1);
        ani2= findViewById(R.id.ani2);
        alrtxt1=findViewById(R.id.alrtxt1);
        alrtxt2=findViewById(R.id.alrtxt2);

        //stateProgressBar
        String[] descriptionData = {"예매 설정", "열차 조회", "상세 정보 확인", "예매 완료"};
        StateProgressBar stateProgressBar = findViewById(R.id.topprogressbar1);
        stateProgressBar.setStateDescriptionData(descriptionData);

        //날짜
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        String dateText = year + "년 " + month + "월 " + day + "일 (" + weekday + ")";
        todate.setText(dateText);

        //출발, 도착
        depstn.setText(mStartStation.st_station);
        String dephour = ticket.depPlandTime.substring(8, 10);
        String depmin = ticket.depPlandTime.substring(10, 12);
        deptime.setText(dephour + ":" + depmin);

        arrstn.setText(mEndStation.st_station);
        String arrhour = ticket.arrPlandTime.substring(8, 10);
        String arrmin = ticket.arrPlandTime.substring(10, 12);
        arrtime.setText(arrhour + ":" + arrmin);

        //정보 + 요금
        traintext.setText(ticket.trainGradeName + " " + ticket.trainno );
        charge1.setText(ticket.adultCharge + " 원");
        charge2.setText(numseat + " 명");
        adult = Integer.parseInt(ticket.adultCharge);
        diswon=0;
        discounttxt.setText("(-) "+ diswon+" 원");
        totalcharge = numseat * adult - diswon;
        totchrtxt.setText(totalcharge + " 원");

        //유의사항 버튼
        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(beforResev.this, Documentation.class);
                startActivity(intent);
            }
        });

        // TTS를 생성하고 OnInitListener로 초기화 한다.
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        tts.setSpeechRate(0.9f);    // 읽는 속도를 0.75빠르기로 설정

        //결제 버튼
        Button gobtn = findViewById(R.id.gobtn);
        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(beforResev.this, AuthActivity.class);
                intent.putExtra("userinfo", userinfo);
                intent.putExtra("mStartStation", mStartStation);
                intent.putExtra("mEndStation", mEndStation);
                intent.putExtra("date", date);
                intent.putExtra("weekday", weekday);
                intent.putExtra("numseat", numseat);
                intent.putExtra("ticket", ticket);
                intent.putExtra("totalcharge", totalcharge);
                intent.putExtra("diswon", diswon);
                startActivity(intent);
            }
        });

        //할인 버튼
        Button discountbtn = findViewById(R.id.discountbtn);
        discountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(beforResev.this, DiscountActivity.class);
                dialogResultLauncher.launch(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
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

        switch (item.getItemId()){
            //도움말 버튼
            case R.id.menu_help:
                if(!onhelp) {  //도움말 활성화
                    alrtxt1.setVisibility(View.VISIBLE);
                    alrtxt2.setAlpha(1f);
                    ani1.setAlpha(1f);
                    ani2.setAlpha(1f);
                    onhelp=true;
                }
                else {  //도움말 비활성화
                    alrtxt1.setVisibility(View.GONE);
                    alrtxt2.setAlpha(0f);
                    ani1.setAlpha(0f);
                    ani2.setAlpha(0f);
                    onhelp=false;
                }

                return true;

            case R.id.menu_tts:
                if(!ontts) {  //tts 활성화
                    tts.speak("표를 확인하고 결제하세요. 할인 가능하면 원하는 할인을 선택하세요.",TextToSpeech.QUEUE_FLUSH, null, null);
                    ontts=true;
                }
                else {  //도움말 비활성화
                    tts.stop();
                    ontts=false;
                }

                return true;

            //toolbar의 back키 눌렀을 때 동작
            case android.R.id.home:{
                finish();
                return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }

}