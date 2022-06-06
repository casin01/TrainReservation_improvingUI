package com.example.reservet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.reservet.ui.CustomDialogClickListener;
import com.example.reservet.ui.Btn1AlertDialog;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static android.net.wifi.p2p.WifiP2pManager.ERROR;


public class trainbook extends AppCompatActivity {

    //xml
    private Button startstn, deststn, calbtn1;
    StationInfo mStartStation; //초기 설정값 서울
    StationInfo mEndStation; //대전
    private EditText penum; //화면출력 총 몇 명
    private String date = "";       //오늘로부터 6일 후까지 조회 가능, 시간은 설정 불가 //20220101
    private String datetext = "";   //화면 출력 날짜
    private String year, month, day, weekday;
    private int numseat;    //인원 수
    //인원 수는 어른, 어린이, 경증장애인, 중증장애인으로 구분해놓았지만
    //공공데이터포털에서는 어른 요금만 제공하고 있어서
    //인원수만 포함시키고 이후 과정에선 따로 구분하지 않음.
    private UserInfo userinfo;
    View ani1_1, ani1_2, ani2_1, ani3_1;
    TextView alrtxt1, alrtxt2, alrtxt3;
    private boolean onhelp=false;
    private boolean ontts =false;
    private TextToSpeech tts;       // TTS 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainbook);
        setTitle("예매 설정");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        //유저 아이디, 이름 받아오기
        Intent intent = getIntent();
        userinfo = (UserInfo) intent.getSerializableExtra("userinfo");

        Initialize();
    }

    //어떤 화면으로 전환할지
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        switch (data.getIntExtra("receivecode", 0)) {
                            case HandlerMessage.START_SET: // requstCode를 설정
                                if (data != null)
                                    mStartStation = (StationInfo) data.getSerializableExtra("stationInfo");
                                startstn.setText(mStartStation.st_station);
                                break;
                            case HandlerMessage.END_SET:    //도착역
                                if (data != null) {
                                    StationInfo tempstation = (StationInfo) data.getSerializableExtra("stationInfo");

                                    if(tempstation.st_station.equals(mStartStation.st_station) ){
                                        //출발역과 도착역이 같을 경우
                                        String title="오류";
                                        String body="출발역과 도착역이 같습니다.";
                                        Btn1AlertDialog btn1dialog = new Btn1AlertDialog(trainbook.this, new CustomDialogClickListener() {
                                            @Override
                                            public void onPositiveClick() {
                                                Log.e("출발역 도착역 같은 팝업창", "OK click");
                                            }
                                            @Override
                                            public void onNegativeClick() {
                                                //
                                            }
                                        }, title, body);
                                        //다이얼로그 밖에 터치했을 때 다이얼로그가 꺼짐
                                        btn1dialog.setCanceledOnTouchOutside(false);
                                        //back 버튼 누르면 다이얼로그 닫힘
                                        btn1dialog.setCancelable(false);
                                        btn1dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                        btn1dialog.show();

                                    }
                                    else {
                                        mEndStation.st_station=tempstation.st_station;
                                        mEndStation.st_stationCode=tempstation.st_stationCode;
                                    }
                                }

                                deststn.setText(mEndStation.st_station);
                                break;
                            case HandlerMessage.SELECT_SEAT:   //인원 수
                                if (data != null)
                                    numseat = data.getIntExtra("numseat", 1);
                                penum.setText("총  " + numseat + " 명");
                                break;
                        }
                    }
                }
            });

    void Initialize() {
        startstn = findViewById(R.id.startstn);
        deststn = findViewById(R.id.deststn);
        calbtn1 = findViewById(R.id.calbtn1);
        penum = findViewById(R.id.penum);
        ani1_1= findViewById(R.id.ani1_1);
        ani1_2= findViewById(R.id.ani1_2);
        alrtxt1=findViewById(R.id.alrtxt1);
        ani2_1= findViewById(R.id.ani2_1);
        alrtxt2=findViewById(R.id.alrtxt2);
        ani3_1= findViewById(R.id.ani3_1);
        alrtxt3=findViewById(R.id.alrtxt3);

        //stateProgressBar
        String[] descriptionData = {"예매 설정", "열차 조회", "상세 정보 확인", "예매 완료"};
        StateProgressBar stateProgressBar = findViewById(R.id.topprogressbar1);
        stateProgressBar.setStateDescriptionData(descriptionData);

        //현재 날짜
        Calendar cal = Calendar.getInstance();  //현재시간을 담고있는 Calendar 객체를 리턴
        year = String.valueOf(cal.get(Calendar.YEAR));  //~~tostring
        month = makeString(cal.get(Calendar.MONTH) + 1);
        day = makeString(cal.get(Calendar.DAY_OF_MONTH));
        date = year + month + day;
        datetext = year + "년 " + month + "월 " + day + "일 00시 이후";
        calbtn1.setText(datetext);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        weekday = dayFormat.format(cal.getTime());

        //디폴트 출발역, 도착역
        mStartStation = new StationInfo("서울", "NAT010000");
        mEndStation = new StationInfo("대전", "NAT011668");

        //인원 수
        numseat=1;
        penum.setText("총  " + numseat + " 명");

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


    //한 자리 숫자일 경우 앞에 0 붙임
    String makeString(int val)
    {
        String temp = String.valueOf(val);
        if (val >= 1 && val < 10)
            temp = "0" + val;

        return temp;
    }

    //날짜선택 Dialog
    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    // Date Picker에서 선택한 날짜를 TextView에 설정
                    year = String.valueOf(yy);  //~~tostring
                    month = makeString(mm + 1);
                    day = makeString(dd);
                    date = year + month + day;
                    datetext = year + "년 " + month + "월 " + day + "일 00시 이후";
                    calbtn1.setText(datetext);

                    //요일 구하기
                    Calendar cal = Calendar.getInstance();
                    cal.set(yy, mm, dd);
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EE", Locale.getDefault());
                    weekday = dayFormat.format(cal.getTime());
                }
            };

    public void mOnClick_DatePick(View view) {
        // DATE Picker가 처음 떴을 때, 오늘 날짜가 보이도록 설정.
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();
    }
    
    //버튼 함수
    public void mClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.deststn:
                intent = new Intent(this, StationList.class);
                intent.putExtra("requestCode", HandlerMessage.END_SET);
                someActivityResultLauncher.launch(intent);
                break;
            case R.id.startstn:
                intent = new Intent(this, StationList.class);
                intent.putExtra("requestCode", HandlerMessage.START_SET);
                someActivityResultLauncher.launch(intent);
                break;
            case R.id.selectseat:
                intent = new Intent(this, PassengerNumDialog.class);
                someActivityResultLauncher.launch(intent);
                break;
            case R.id.gobtn: // 조회 버튼
                intent = new Intent(this, trainNow.class);
                intent.putExtra("userinfo", userinfo);
                intent.putExtra("mStartStation", mStartStation);
                intent.putExtra("mEndStation", mEndStation);
                intent.putExtra("date", date);
                intent.putExtra("weekday", weekday);
                intent.putExtra("numseat", numseat);
                someActivityResultLauncher.launch(intent);
                break;
        }
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
                    ani1_1.setVisibility(View.VISIBLE);
                    ani1_2.setVisibility(View.VISIBLE);
                    alrtxt1.setAlpha(1f);
                    ani2_1.setVisibility(View.VISIBLE);
                    alrtxt2.setAlpha(1f);
                    ani3_1.setAlpha(1f);
                    alrtxt3.setAlpha(1f);
                    onhelp=true;
                }
                else {  //도움말 비활성화
                    ani1_1.setVisibility(View.GONE);
                    ani1_2.setVisibility(View.GONE);
                    alrtxt1.setAlpha(0f);
                    ani2_1.setVisibility(View.GONE);
                    alrtxt2.setAlpha(0f);
                    ani3_1.setAlpha(0f);
                    alrtxt3.setAlpha(0f);
                    onhelp=false;
                }

                return true;

            case R.id.menu_tts:
                if(!ontts) {  //tts 활성화
                    tts.speak("출발역, 도착역, 가는 날짜, 인원 수를 설정하세요.",TextToSpeech.QUEUE_FLUSH, null, null);
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