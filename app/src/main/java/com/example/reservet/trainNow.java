package com.example.reservet;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static android.net.wifi.p2p.WifiP2pManager.ERROR;

public class trainNow extends AppCompatActivity {

    //받아온 데이터
    private UserInfo userinfo;
    private String weekday;
    private String date=""; //20220101
    private int numseat;

    private ListView listview = null;
    private ListViewAdapter adapter = null;

    //스레드
    URLRequest mRequest;
    StationInfo mStartStation; //출발역 서울
    StationInfo mEndStation;    //도착역 대전
    //NAT010000
    //NAT011668
    ArrayList<TrainTicketInfo> mResult; // URLRequest로 받은 데이터 저장
    ArrayList<TrainType> mTrainCategory; // 역 구분을 위함
    private ProgressDialog dialog; // Loading 표시 위한 ProgressDialog

    private TextView no_result; //검색결과 없다는 알림 텍스트
    private TrainTicketInfo ticket; //선택된 리스트 항목
    Button gobtn;
    String setDate;

    TextView alrtxt1;
    private boolean onhelp=false;
    private boolean ontts =false;
    private TextToSpeech tts;       // TTS 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_now);
        setTitle("열차 조회");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        //데이터 받아오기
        Intent intent = getIntent();
        userinfo = (UserInfo) intent.getSerializableExtra("userinfo");
        mStartStation= (StationInfo) intent.getSerializableExtra("mStartStation");
        mEndStation= (StationInfo) intent.getSerializableExtra("mEndStation");
        date= intent.getStringExtra("date");
        weekday= intent.getStringExtra("weekday");
        numseat=intent.getIntExtra("numseat", 1);
        init();
    }

    void init() {
        mTrainCategory = new ArrayList<>();
        mResult = new ArrayList<>();
        no_result = findViewById(R.id.no_result);
        alrtxt1 = findViewById(R.id.alrtxt1);

        //stateProgressBar
        String[] descriptionData = {"예매 설정", "열차 조회", "상세 정보 확인", "예매 완료"};
        StateProgressBar stateProgressBar = findViewById(R.id.topprogressbar1);
        stateProgressBar.setStateDescriptionData(descriptionData);

        //예매 선택한 날짜
        TextView seldate = findViewById(R.id.seldate);
        String year = date.substring(0,4);
        String month = date.substring(4,6);
        String day = date.substring(6,8);
        String dateText = year + "년 " + month + "월 " + day + "일 ("+weekday +")";
        seldate.setText(dateText);  //화면출력용
        gobtn =findViewById(R.id.gobtn);
        gobtn.setEnabled(false);
        setDate= date;

        //리스트에서 선택한 아이템
        listview = findViewById(R.id.listview1);
        adapter = new ListViewAdapter(trainNow.this);

        TrainCategorySet();

        //열차 조회 URLRequest
        mRequest = new URLRequest(mStartStation, mEndStation, date, trainNow.this);
        mRequest.start();

        dialog = ProgressDialog.show(this, "",
                "로딩 중", true); // ProgressDialog 시작

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

    }

    // 열자 조회 스레드 핸들러
    // Thread 작업이 완료되면 메세지를 받는다.
    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HandlerMessage.THREAD_HANDLER_SUCCESS_INFO:
                    ArrayList<ContentValues> mTrainTicekt = (ArrayList<ContentValues>) msg.obj; //메세지 받아온 데이터
                    ContentValueToArrayList(mTrainTicekt);  //arraylist로 변환

                    if (mResult.size() == 0) {
                        no_result.setVisibility(no_result.VISIBLE);
                        Log.e("myMsg", "불러오기 실패");
                        dialog.dismiss(); // 프로그레스 다이얼로그 종료
                        break;
                    }

                    no_result.setVisibility(no_result.GONE);
                    for (int i = 0; i < mResult.size(); i++) {
                        adapter.addItem(mResult.get(i));
                    }

                    listview.setAdapter(adapter);

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ticket = (TrainTicketInfo) adapter.getItem(position);
//                            Log.e("선택한테스트", "기차이름 "+ticket.trainGradeName+" 기차번호 "+ticket.trainno+" 출발시간 "+ticket.depPlandTime
 //                           +" 도착시간 "+ticket.arrPlandTime);
                            gobtn.setEnabled(true);
                            gobtn.setTextColor(Color.WHITE);

                        }
                    });

                    dialog.dismiss(); // 프로그레스 다이얼로그 종료
                    break;

                default:
                    break;
            }
        }
    };

    //받아온 데이터 arraylist로 바꾸기
    void ContentValueToArrayList(ArrayList<ContentValues> mData) {

        for (int j = 0; j < mData.size(); j++) {
            String trainGradeName = String.valueOf(mData.get(j).get("traingradename")); // 차량 종류
            String depPlandTime = String.valueOf(mData.get(j).get("depplandtime"));   // 출발 시간
            String arrPlandTime = String.valueOf(mData.get(j).get("arrplandtime"));   // 도착 시간
            String depPlaceName = String.valueOf(mData.get(j).get("depplacename"));  // 출발지
            String arrPlaceName = String.valueOf(mData.get(j).get("arrplacename")); // 도착지
            String adultCharge = String.valueOf(mData.get(j).get("adultcharge")); // 비용
            String trainNo = String.valueOf(mData.get(j).get("trainno")); // 열차 번호

            if(trainGradeName != null && depPlaceName != null && depPlandTime != null && arrPlandTime != null
                    && arrPlaceName != null && adultCharge != null) {
                TrainTicketInfo mTemp = new TrainTicketInfo(trainGradeName, depPlandTime, arrPlandTime, depPlaceName, arrPlaceName, adultCharge, trainNo);
                mResult.add(mTemp); //받아온 TrainTicketInfo arraylist : mResult
            }
        }

    }

    public void mClick(View v) {
        Intent intent;
        switch(v.getId())
        {
            case R.id.gobtn: // 예매하기 버튼
                intent=new Intent(this, beforResev.class);
                intent.putExtra("userinfo", userinfo);
                intent.putExtra("mStartStation", mStartStation);
                intent.putExtra("mEndStation", mEndStation);
                intent.putExtra("date", date);
                intent.putExtra("weekday", weekday);
                intent.putExtra("numseat", numseat);
                intent.putExtra("ticket", ticket);
 //               Log.e("mStartStation 역이름", mStartStation.st_station+"mEndStation 역이름" +mEndStation.st_station);
 //               Log.e("depPlaceName 역이름", ticket.depPlaceName + "arrPlaceName 역이름" + ticket.arrPlaceName);
                startActivity(intent);
                break;
        }
    }

    void TrainCategorySet() // 기차 코드번호에 따른 기차 종류
    {
        mTrainCategory.add(new TrainType("00", "KTX"));
        mTrainCategory.add(new TrainType("01", "새마을호"));
        mTrainCategory.add(new TrainType("02", "무궁화호"));
        mTrainCategory.add(new TrainType("03", "통근열차"));
        mTrainCategory.add(new TrainType("04", "누리로"));
        mTrainCategory.add(new TrainType("06", "공항직통"));
        mTrainCategory.add(new TrainType("07", "KTX-산천"));
        mTrainCategory.add(new TrainType("08", "ITX-새마을"));
        mTrainCategory.add(new TrainType("09", "ITX-청춘"));
        mTrainCategory.add(new TrainType("10", "KTX-산천"));
        mTrainCategory.add(new TrainType("16", "KTX-이음"));
        mTrainCategory.add(new TrainType("17", "SRT"));

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
                    onhelp=true;
                }
                else {  //도움말 비활성화
                    alrtxt1.setVisibility(View.GONE);
                    onhelp=false;
                }

                return true;

            case R.id.menu_tts:
                if(!ontts) {  //tts 활성화
                    tts.speak("원하는 표를 터치하세요.",TextToSpeech.QUEUE_FLUSH, null, null);
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