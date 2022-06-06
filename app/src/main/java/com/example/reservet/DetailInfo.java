package com.example.reservet;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.reservet.ui.Btn1AlertDialog;
import com.example.reservet.ui.Btn2AlertDialog;
import com.example.reservet.ui.CustomDialogClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Objects;

import static android.net.wifi.p2p.WifiP2pManager.ERROR;

public class DetailInfo extends AppCompatActivity {

    private String userID, userPassword, imp_uid, merchant_uid;
    TextView alrtxt1, alrtxt2;
    View ani1, ani2;
    private boolean onhelp=false;
    private boolean ontts =false;
    private TextToSpeech tts;       // TTS 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        setTitle("승차권 상세 정보");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        //데이터 받아옴
        Intent intent = getIntent();
        AllInfo myinfo = (AllInfo) intent.getSerializableExtra("myinfo");
        userID=myinfo.userID;
        userPassword= myinfo.userPassword;
        imp_uid=myinfo.imp_uid;
        merchant_uid=myinfo.merchant_uid;

        //화면구성
        Button enterbtn =findViewById(R.id.enterbtn);
        Button refundbtn =findViewById(R.id.refundbtn);
        setView(myinfo);

        //예매 확인 (뒤로 버튼)
        enterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //예매 취소
        refundbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //경고창
                String title="예매 취소";
                String body="환불일로부터 약 7일 이후 결제가 취소됩니다. \n정확한 환불일자는 고객님의 결제일에 따라 다르므로" +
                        " 자세한 사항은 해당 카드사에 문의하여 주시기 바랍니다. \n\n승차권을 취소하시겠습니까?" +
                        "\n\n*자세한 사항은 처음 화면 -> 종합 이용 안내 -> 환불주의사항을 참고하세요. ";
                Btn2AlertDialog octDialog = new Btn2AlertDialog(DetailInfo.this, new CustomDialogClickListener() {
                    @Override
                    public void onPositiveClick() {

                        //환불 요청 (DB에서 삭제)
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    //Log.i("DB 행 삭제 여부 ", "["+response+"]");

                                    //php 구문에서 success
                                    //서버 통신 성공했는지
                                    if (success) {
                                        //열차 예매 취소 성공
                                        Log.e("MyMsg", "승차권 취소 성공했습니다.");
                                        Log.e("canceltrain", "유저아이디: "+userID +"imp_uid : "+imp_uid + "merchant_uid :" + merchant_uid);

                                        //다이얼로그
                                        String title="승차권 취소";
                                        String body=" 승차권 취소가 완료되었습니다.";
                                        Btn1AlertDialog btn1dialog = new Btn1AlertDialog(DetailInfo.this, new CustomDialogClickListener() {
                                            @Override
                                            public void onPositiveClick() {
                                                //메인화면으로
                                                Intent intent = new Intent(DetailInfo.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                startActivity(intent);
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
                                        //열차 취소 성공 다이얼로그 끝


                                    } else {    //열차 예매 취소 실패
                                        Log.d("MyMsg", "승차권 취소 실패");

                                        //다이얼로그
                                        String title="오류";
                                        String body=" 승차권 취소 실패";
                                        Btn1AlertDialog btn1dialog = new Btn1AlertDialog(DetailInfo.this, new CustomDialogClickListener() {
                                            @Override
                                            public void onPositiveClick() {
                                                //메인화면으로
                                                Intent intent = new Intent(DetailInfo.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                startActivity(intent);
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
                                        //열차 취소 실패 다이얼로그 끝

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        //서버로 Volley를 이용해서 요청함
                        CancelRequest cancelRequest = new CancelRequest(userID, userPassword, imp_uid, merchant_uid, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(DetailInfo.this);
                        queue.add(cancelRequest);
                    }
                    @Override
                    public void onNegativeClick() {
                        //
                    }
                }, title, body, -1);
                //다이얼로그 밖에 터치했을 때 다이얼로그가 꺼짐
                octDialog.setCanceledOnTouchOutside(false);
                //back 버튼 누르면 다이얼로그 닫힘
                octDialog.setCancelable(false);
                octDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                octDialog.show();
                //예매 취소 여부 묻는 다이얼로그 끝

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

    }

    //화면 구성
    void setView(AllInfo myinfo){
        TextView todate = findViewById(R.id.todate);
        TextView depstn = findViewById(R.id.depstn);
        TextView deptime = findViewById(R.id.deptime);
        TextView arrstn = findViewById(R.id.arrstn);
        TextView arrtime = findViewById(R.id.arrtime);
        TextView ticdetail = findViewById(R.id.ticdetail);
        TextView imp_uidtxt = findViewById(R.id.imp_uid);
        TextView merchant_uidtxt = findViewById(R.id.merchant_uid);
        TextView txt_total = findViewById(R.id.txt_total);
        Button infobtn =findViewById(R.id.infobtn);
        ani1= findViewById(R.id.ani1);
        ani2= findViewById(R.id.ani2);
        alrtxt1=findViewById(R.id.alrtxt1);
        alrtxt2=findViewById(R.id.alrtxt2);

        //날짜
        String date = myinfo.bookdate;
        String year = date.substring(0,4);
        String month = date.substring(4,6);
        String day = date.substring(6,8);
        String weekday =date.substring(8);
        String dateText = year + "년 " + month + "월 " + day + "일 ("+weekday +")";
        todate.setText(dateText);

        //출발, 도착
        depstn.setText(myinfo.depStn);
        String dephour = myinfo.depTime.substring(8, 10);
        String depmin = myinfo.depTime.substring(10, 12);
        deptime.setText(dephour+":"+depmin);

        arrstn.setText(myinfo.arrStn);
        String arrhour = myinfo.arrTime.substring(8, 10);
        String arrmin = myinfo.arrTime.substring(10, 12);
        arrtime.setText(arrhour+":"+arrmin);

        //정보 + 요금
        ticdetail.setText(myinfo.trainName + " " + myinfo.trainNo);
        imp_uidtxt.setText(imp_uid);
        merchant_uidtxt.setText(merchant_uid);
        txt_total.setText("금액  " +myinfo.totalcharge + "원");

        //유의사항 버튼
        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailInfo.this, Documentation.class);
                startActivity(intent);
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
                    alrtxt2.setVisibility(View.VISIBLE);
                    ani1.setAlpha(1f);
                    ani2.setAlpha(1f);
                    onhelp=true;
                }
                else {  //도움말 비활성화
                    alrtxt1.setVisibility(View.GONE);
                    alrtxt2.setVisibility(View.GONE);
                    ani1.setAlpha(0f);
                    ani2.setAlpha(0f);
                    onhelp=false;
                }

                return true;

            case R.id.menu_tts:
                if(!ontts) {  //tts 활성화
                    String text = "승차권을 확인하세요. 승차권 취소하기 버튼을 눌러 승차권을 취소할 수 있습니다.";
                    tts.speak(text,TextToSpeech.QUEUE_FLUSH, null, null);
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