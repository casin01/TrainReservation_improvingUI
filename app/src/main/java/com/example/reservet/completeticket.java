package com.example.reservet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.reservet.ui.Btn1AlertDialog;
import com.example.reservet.ui.CustomDialogClickListener;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

public class completeticket extends AppCompatActivity {
    private int totalcharge;
    private String date;
    private String weekday;
    private StationInfo mStartStation, mEndStation;
    private TrainTicketInfo ticket;
    private String imp_uid, merchant_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeticket);
        setTitle("승차권 예매 완료");

        ProgressDialog progressDialog = ProgressDialog.show(this, "",
                "로딩 중", true); // ProgressDialog 시작

        //데이터 받아오기
        Intent intent = getIntent();
        UserInfo userinfo = (UserInfo) intent.getSerializableExtra("userinfo");
        mStartStation= (StationInfo) intent.getSerializableExtra("mStartStation");
        mEndStation= (StationInfo) intent.getSerializableExtra("mEndStation");
        date= intent.getStringExtra("date");
        weekday= intent.getStringExtra("weekday");
        int numseat = intent.getIntExtra("numseat", 1);
        ticket= (TrainTicketInfo) intent.getSerializableExtra("ticket");
        totalcharge=intent.getIntExtra("totalcharge", 300);
        String responseText= intent.getStringExtra("responseText");

        //uid 받아오는데 필요함
        String[] result = getUID(responseText);
        imp_uid = substr(result[2]);
        merchant_uid = substr(result[3]);
        Log.e ("imp_uid         ", imp_uid);
        Log.e ("merchant_uid         ", merchant_uid);

        String bookdate = date + weekday;   //20220101금
        TextView vermsg= findViewById(R.id.vermsg); //빨간 문구 msg

        //서버로 Volley를 이용해서 요청함
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
            //        Log.i("completeticket TICKETINFO 등록 확인여부", "["+response+"]");


                    //php 구문에서 success
                    //서버 통신 성공했는지
                    if (success) {   // 예매 성공
                        Log.e("MyMsg", "json 서버 통신 성공");


                    } else {    //예매 실패
                        Log.e("MyMsg", "json 서버 통신 실패, 예매 실패");
                        String title="오류";
                        String body="예매 실패했습니다.";
                        Btn1AlertDialog btn1dialog = new Btn1AlertDialog(completeticket.this, new CustomDialogClickListener() {
                            @Override
                            public void onPositiveClick() {
                                //
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

                        vermsg.setText("예매 실패");
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("json 연결 실패", e.getMessage());
                    vermsg.setText("예매 실패");
                }
            }
        };

        TicketRequest ticketRequest = new TicketRequest(userinfo.userID, userinfo.userPassword, userinfo.userName,
                userinfo.userPhone, mStartStation.st_station, mStartStation.st_stationCode, mEndStation.st_station,
                mEndStation.st_stationCode, ticket.trainGradeName, ticket.trainno, ticket.depPlandTime,
                ticket.arrPlandTime, numseat, totalcharge, imp_uid, merchant_uid, bookdate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(completeticket.this);
        queue.add(ticketRequest);

        //화면 보여줌
        setView();
        progressDialog.dismiss();
    }

    //str 자름
    String substr (String str) {
        String reltxt = str.substring(str.lastIndexOf("=")+1);
        return reltxt;
    }
    //uid 받아오기
    String[] getUID (String text) {
        String [] splitText =text.split(", ");
        return splitText;
    }


    void setView () {
        TextView todate = findViewById(R.id.todate);
        TextView depstn = findViewById(R.id.depstn);
        TextView deptime = findViewById(R.id.deptime);
        TextView arrstn = findViewById(R.id.arrstn);
        TextView arrtime = findViewById(R.id.arrtime);
        TextView ticdetail = findViewById(R.id.ticdetail);
        TextView imp_uidtxt = findViewById(R.id.imp_uid);
        TextView merchant_uidtxt = findViewById(R.id.merchant_uid);
        TextView txt_total = findViewById(R.id.txt_total);
        Button gobtn =findViewById(R.id.gobtn3);
        Button infobtn =findViewById(R.id.infobtn);

        //stateProgressBar
        String[] descriptionData = {"예매 설정", "열차 조회", "상세 정보 확인", "예매 완료"};
        StateProgressBar stateProgressBar = findViewById(R.id.topprogressbar1);
        stateProgressBar.setStateDescriptionData(descriptionData);

        //날짜
        String year = date.substring(0,4);
        String month = date.substring(4,6);
        String day = date.substring(6,8);
        String dateText = year + "년 " + month + "월 " + day + "일 ("+weekday +")";
        todate.setText(dateText);

        //출발, 도착
        depstn.setText(mStartStation.st_station);
        String dephour = ticket.depPlandTime.substring(8, 10);
        String depmin = ticket.depPlandTime.substring(10, 12);
        deptime.setText(dephour+":"+depmin);

        arrstn.setText(mEndStation.st_station);
        String arrhour = ticket.arrPlandTime.substring(8, 10);
        String arrmin = ticket.arrPlandTime.substring(10, 12);
        arrtime.setText(arrhour+":"+arrmin);

        //정보 + 요금
        ticdetail.setText(ticket.trainGradeName + " " + ticket.trainno);
        imp_uidtxt.setText(imp_uid);
        merchant_uidtxt.setText(merchant_uid);
        txt_total.setText("금액  " +totalcharge + "원");

        //유의사항 버튼
        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(completeticket.this, Documentation.class);
                startActivity(intent);
            }
        });

        //예매 완료 버튼
        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(completeticket.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Log.e("my msg", "예매 완료 및 처음으로 되돌아가기");
                startActivity(intent);
            }
        });


    }

    //뒤로가기 버튼 막기
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}