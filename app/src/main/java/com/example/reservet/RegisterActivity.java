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

public class RegisterActivity extends AppCompatActivity {

    private EditText etID, etName, etPasswd, etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("회원가입");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        etID = findViewById(R.id.userid);
        etName = findViewById(R.id.username);
        etPasswd = findViewById(R.id.userpasswd);
        etPhone = findViewById(R.id.userPhone);
        Button gobtn = findViewById(R.id.gobtn);

        //회원가입 버튼 클릭 시
        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = etID.getText().toString();
                String userName = etName.getText().toString();
                String userPasswd = etPasswd.getText().toString();
                String userPhone =etPhone.getText().toString();

                // 유저가 항목을 다 채우지 않았을 경우
                if(userID.isEmpty() || userPasswd.isEmpty() || userName.isEmpty() || userPhone.isEmpty()){
                    String title="오류";
                    String body="입력란을 모두 작성해주세요.";
                    Btn1AlertDialog btn1dialog = new Btn1AlertDialog(RegisterActivity.this, new CustomDialogClickListener() {
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
                                //Log.i("tagconvertstr", "["+response+"]");

                                //php 구문에서 success
                                //서버 통신 성공했는지
                                if (success) {   //회원가입 성공
                                    Log.d("MyMsg", "회원등록에 성공했습니다.");
                                    String title="회원가입";
                                    String body="회원등록에 성공했습니다.";
                                    Btn1AlertDialog btn1dialog = new Btn1AlertDialog(RegisterActivity.this, new CustomDialogClickListener() {
                                        @Override
                                        public void onPositiveClick() {
                                            Intent intent = new Intent(RegisterActivity.this, loginActivity.class);
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
                                    btn1dialog.setCancelable(true);
                                    btn1dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                                    btn1dialog.show();

                                } else {    //회원가입 실패
                                    Log.d("MyMsg", "회원등록에 실패");
                                    String title="회원가입 실패";
                                    String body="회원가입에 실패했습니다.";
                                    Btn1AlertDialog btn1dialog = new Btn1AlertDialog(RegisterActivity.this, new CustomDialogClickListener() {
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

                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    //서버로 Volley를 이용해서 요청함
                    RegisterRequest registerRequest = new RegisterRequest(userID, userPasswd, userName, userPhone, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
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