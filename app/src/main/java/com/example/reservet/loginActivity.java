package com.example.reservet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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

public class loginActivity extends AppCompatActivity {

    private EditText etID, etPasswd;
    CheckBox checkBox1, checkBox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("로그인");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        etID =findViewById(R.id.userID);
        etPasswd =findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.login);
        Button btnRegister = findViewById(R.id.register);
        Button findID=findViewById(R.id.findID);
        Button findpwd=findViewById(R.id.findpwd);
        checkBox1=findViewById(R.id.checkBox1);
        checkBox2=findViewById(R.id.checkBox2);

        //아이디 저장한 것 받아올 때
        Intent intent= getIntent();
        String savedID = intent.getStringExtra("userID");
        if(savedID != "") etID.setText(savedID);

        //아이디 찾기
        findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(loginActivity.this, findIDActivity.class);
                startActivity(intent1);
            }
        });

        //비번 찾기
        findpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(loginActivity.this, findpwdActivity.class);
                startActivity(intent2);
            }
        });

        //회원가입 버튼
       btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

       //로그인 버튼
       btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = etID.getText().toString();
                String userPasswd = etPasswd.getText().toString();

                // 유저가 항목을 다 채우지 않았을 경우
                if(userID.isEmpty() || userPasswd.isEmpty()){
                    String title="오류";
                    String body=" 입력란을 모두 작성해주세요.";
                    Btn1AlertDialog btn1dialog = new Btn1AlertDialog(loginActivity.this, new CustomDialogClickListener() {
                        @Override
                        public void onPositiveClick() {
       //                     Log.e("로그인 입력란 팝업창", "OK click");
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

                                if (success) {   //로그인 성공
                                    String userID = jsonObject.getString("userID");
                                    String userPasswd = jsonObject.getString("userPassword");
                                    String username = jsonObject.getString("userName");
                                    String userPhone = jsonObject.getString("userPhone");

                                    Log.e("MyMsg", "로그인 성공");

                                    //자동 로그인에 체크되어있을 때
                                    if(checkBox2.isChecked()) {
                                        Log.e("MyMsg", "자동로그인_체크박스_체크");
                                        // 유저가 입력한 id, pw를 쉐어드에 저장한다.
                                        SharedPreferences sf = getSharedPreferences("userFile", MODE_PRIVATE);
                                        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                                        SharedPreferences.Editor editor = sf.edit();
                                        editor.putString("userID",userID); // key, value를 이용하여 저장하는 형태
                                        editor.putString("userPasswd", userPasswd);
                                        editor.putString("username", username);
                                        editor.putString("userPhone", userPhone);
                                        //최종 커밋
                                        editor.apply();
                                    }

                                    //아이디 저장에 체크되어있을 때
                                    if (checkBox1.isChecked()) {
                                        Log.e("MyMsg", "회원번호저장_체크박스에_체크");
                                        // 유저가 입력한 id를 쉐어드에 저장한다.
                                        SharedPreferences sf = getSharedPreferences("userFile", MODE_PRIVATE);
                                        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                                        SharedPreferences.Editor editor = sf.edit();
                                        editor.putString("userID",userID); // key, value를 이용하여 저장하는 형태
                                        editor.putBoolean("IDsave", true);
                                        //최종 커밋
                                        editor.apply();
                                    }
                                    else {
                                        SharedPreferences sf = getSharedPreferences("userFile", MODE_PRIVATE);
                                        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                                        SharedPreferences.Editor editor = sf.edit();
                                        editor.putBoolean("IDsave", false);
                                        editor.remove("userID");
                                        editor.apply();
                                    }

                                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.putExtra("userID", userID);
                                    intent.putExtra("userPasswd", userPasswd);
                                    intent.putExtra("username", username);
                                    intent.putExtra("userPhone", userPhone);
                                    startActivity(intent);
                                } else {
                                    Log.e("MyMsg", "로그인 실패");
                                    // 로그인 실패
                                    String title="로그인 실패";
                                    String body=" 잘못 입력했거나 없는 회원정보입니다.";
                                    Btn1AlertDialog btn1dialog = new Btn1AlertDialog(loginActivity.this, new CustomDialogClickListener() {
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
                    LoginRequest loginRequest = new LoginRequest(userID, userPasswd, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(loginActivity.this);
                    queue.add(loginRequest);

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