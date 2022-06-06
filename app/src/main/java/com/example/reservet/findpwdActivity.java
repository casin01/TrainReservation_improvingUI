package com.example.reservet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.reservet.ui.Btn1AlertDialog;
import com.example.reservet.ui.CustomDialogClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class findpwdActivity extends AppCompatActivity {

    private EditText etID, etName, etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd);

        setTitle("비밀번호 찾기");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        etID =findViewById(R.id.userid);
        etName =findViewById(R.id.username);
        etPhone =findViewById(R.id.userPhone);
        Button gobtn4=findViewById(R.id.gobtn4);

        
        //버튼 누르면
        gobtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = etID.getText().toString();
                String userName = etName.getText().toString();
                String userPhone = etPhone.getText().toString();

                // 유저가 항목을 다 채우지 않았을 경우
                if(userID.isEmpty() || userName.isEmpty() || userPhone.isEmpty() ) {
                    String title = "오류";
                    String body = " 입력란을 모두 작성해주세요.";
                    Btn1AlertDialog btn1dialog = new Btn1AlertDialog(findpwdActivity.this, new CustomDialogClickListener() {
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
                    btn1dialog.setCanceledOnTouchOutside(true);
                    //back 버튼 누르면 다이얼로그 닫힘
                    btn1dialog.setCancelable(true);
                    btn1dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    btn1dialog.show();
                }

                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                //php 구문에서 success
                                //서버 통신 성공했는지

                                if (success) {   //비밀번호 찾기 성공
                                    String userPassword = jsonObject.getString("userPassword");
                                    Log.e("MyMsg", "비번 찾기 성공");

                                    //팝업 띄우기
                                    String title="비밀번호 찾기";
                                    String body=" 비밀번호는 " + userPassword + " 입니다.";
                                    Btn1AlertDialog btn1dialog = new Btn1AlertDialog(findpwdActivity.this, new CustomDialogClickListener() {
                                        @Override
                                        public void onPositiveClick() {
                                            //확인
                                            Intent intent =new Intent(findpwdActivity.this, loginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onNegativeClick() {
                                            //
                                        }
                                    }, title, body);
                                    //다이얼로그 밖에 터치했을 때 다이얼로그가 꺼짐
                                    btn1dialog.setCanceledOnTouchOutside(true);
                                    //back 버튼 누르면 다이얼로그 닫힘
                                    btn1dialog.setCancelable(true);
                                    btn1dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                    btn1dialog.show();


                                } else {
                                    Log.e("MyMsg", "비번 찾기 실패");
                                    // 비밀번호 찾기 실패
                                    String title = "비밀번호 찾기 실패";
                                    String body = " 잘못 입력했거나 없는 회원정보입니다.";
                                    Btn1AlertDialog btn1dialog = new Btn1AlertDialog(findpwdActivity.this, new CustomDialogClickListener() {
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
                                    btn1dialog.setCanceledOnTouchOutside(true);
                                    //back 버튼 누르면 다이얼로그 닫힘
                                    btn1dialog.setCancelable(true);
                                    btn1dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                    btn1dialog.show();

                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    FindPwdRequest findpwdrequest = new FindPwdRequest(userID, userName, userPhone, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(findpwdActivity.this);
                    queue.add(findpwdrequest);
                }
            }
        });
        
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