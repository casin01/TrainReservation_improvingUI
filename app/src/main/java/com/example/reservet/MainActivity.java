package com.example.reservet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.reservet.ui.Btn1AlertDialog;
import com.example.reservet.ui.CustomDialogClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import static android.net.wifi.p2p.WifiP2pManager.ERROR;

public class MainActivity extends AppCompatActivity {

    private Button btnlogin, btnlogout;
    Button btn1, btn2, btn3, btn5;
    private TextView _username;
    private LinearLayout current;   //유저이름+로그아웃버튼
    String userID, userPasswd, username, userPhone;
    SharedPreferences sf;
    private boolean flag;       //로그인 되어 있는지 여부

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;    //뒤로가기 두 번 누르면 뜸
    private boolean ontts =false;
    private boolean onhelp=false;
    private TextToSpeech tts;       // TTS 변수 선언
    LinearLayout mainlayout;
    ConstraintLayout tutlayout;
    View divider14;
    Button btnenter14;
    String ttsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("처음 화면");
        userID = "";
        userPasswd = "";
        flag = false;


        //패스워드가 있으면 자동로그인, 아이디만 있으면 회원정보 저장
        //저장된 값을 불러오기 위해 같은 네임파일을 찾음. (자동 로그인)
        sf = getSharedPreferences("userFile", MODE_PRIVATE);
        // text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
        userID = sf.getString("userID", "");
        userPasswd = sf.getString("userPasswd", "");
        username = sf.getString("username", "");
        userPhone = sf.getString("userPhone", "");

        Log.e("mainact", "유저아이디 " +  userID);

        //유저 정보가 저장되어있는 경우 자동로그인
        if (userPasswd != "") {
            funLogin(userID, userPasswd);
        }

        oninit();

        //로그인 여부 체크
        checkID();

    }

    void oninit() {
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn5 = findViewById(R.id.button5);
        btnlogin = findViewById(R.id.btnlogin);
        btnlogout = findViewById(R.id.btnlogout);
        _username = findViewById(R.id.username);
        current = findViewById(R.id.currentlayout);
        current.setVisibility(View.GONE);
        mainlayout = findViewById(R.id.mainlayout);
        tutlayout = findViewById(R.id.tutlayout);
        divider14 = findViewById(R.id.divider14);
        btnenter14 = findViewById(R.id.btnenter14);
        mainlayout.setAlpha(1.0f);
        tutlayout.setVisibility(View.GONE);
        divider14.setVisibility(View.GONE);
        btnenter14.setVisibility(View.GONE);
        ttsText= "어플 도움말을 보려면 물음표 버튼을 터치하세요.";


        // 로그인
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                intent.putExtra("userID", userID);  //유저 아이디가 있으면 넘겨주기
                startActivity(intent);
            }
        });

        //로그아웃
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sf.edit();
                if(!sf.getBoolean("IDsave", false)) {
                    editor.remove("userID");
                    userID = "";
                }

                editor.remove("userPasswd");
                editor.remove("username");
                editor.remove("userPhone");
                editor.apply();

                userPasswd = "";
                username = "";
                userPhone = "";
                checkID();
            }
        });

        // 예매
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == false) {
                    needlogindialog(v);
                } else {
                    Intent intent1 = new Intent(MainActivity.this, trainbook.class);
                    UserInfo userinfo = new UserInfo(userID, userPasswd, username, userPhone);
                    intent1.putExtra("userinfo", userinfo);
                    startActivity(intent1);
                }
            }
        });

        //조회 및 취소
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == false) {
                    needlogindialog(v);
                } else {
                    Intent intent2 = new Intent(MainActivity.this, Seemytrain.class);
                    intent2.putExtra("userID", userID);
                    intent2.putExtra("userPassword", userPasswd);
                    startActivity(intent2);
                }
            }
        });

        //안내
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, Documentation.class);
                startActivity(intent3);
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

        //앱 종료
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 해당 앱의 루트 액티비티를 종료
                finishAffinity();

                // 현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어
                System.runFinalization();

                // 현재 액티비티를 종료시킨다.
                System.exit(0);
            }
        });

        //튜토리얼 종료
        btnenter14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tutdisabled();
            }
        });
    }

    //dialog
    public void needlogindialog(View v) {
        String title = "로그인";
        String body = " 로그인이 필요합니다.";
        Btn1AlertDialog btn1dialog = new Btn1AlertDialog(MainActivity.this, new CustomDialogClickListener() {
            @Override
            public void onPositiveClick() {
                //              Log.e("로그인 필요한 팝업창", "OK click");
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


    //로그인 여부
    void checkID() {
        //userPasswd가 저장되어있는지 확인 후 로그인 또는 로그아웃 버튼 띄운다

        //null과  "" 구분
        if (userPasswd == "") {   //저장된 비밀번호 없을 경우
            Log.e("로그인 여부", "유저 비밀번호 null, 로그인 필요");
            btnlogin.setVisibility(View.VISIBLE);
            current.setVisibility(View.GONE);
            btnlogin.setEnabled(true);
            flag = false;
        } else {
            Log.e("로그인 여부", "유저아이디는" + userID + "로그인 되어있음");
            btnlogin.setVisibility(View.GONE);
            btnlogin.setEnabled(false);
            current.setVisibility(View.VISIBLE);
            _username.setText(username + " 님");
            flag = true;
        }
    }

    //자동 로그인
    void funLogin(String userid, String userpwd) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    //php 구문에서 success
                    //서버 통신 성공했는지

                    if (success) {   //로그인 성공
                        Log.e("자동 로그인 성공", "로그인 성공");
                        flag = true;
                    } else {
                        Log.e("자동 로그인 실패", "로그인 실패");
                        // 로그인 실패
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        LoginRequest loginRequest = new LoginRequest(userid, userpwd, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(loginRequest);
    }


    //로그인 액티비티로 로그인했을 때
    //재사용 Activity는 onPause(), onNewIntent(), onResume() 순서로 동작
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
  //      Log.e("onnewintent", "유저아이디 "+userID+"유저 비밀번호 "+userPasswd);

        //로그인 후 User ID 값 받아오기
        if (userPasswd.equals("")) {
            userID = intent.getStringExtra("userID");
            userPasswd = intent.getStringExtra("userPasswd");
            username = intent.getStringExtra("username");
            userPhone = intent.getStringExtra("userPhone");
        }

        checkID();
    }

    //뒤로가기 두 번 눌러 종료
    @Override
    public void onBackPressed() {
        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            //        finish();
            toast.cancel();
            // 해당 앱의 루트 액티비티를 종료
            finishAffinity();

            // 현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어
            System.runFinalization();

            // 현재 액티비티를 종료시킨다.
            System.exit(0);
        }
    }

    void tutdisabled (){
        mainlayout.setAlpha(1.0f);
        tutlayout.setVisibility(View.GONE);
        divider14.setVisibility(View.GONE);
        btnenter14.setVisibility(View.GONE);
        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3.setEnabled(true);
        btn5.setEnabled(true);
        btnlogin.setEnabled(true);
        btnlogout.setEnabled(true);

        setTitle("처음 화면");
        ttsText="어플 도움말을 보려면 물음표 버튼을 터치하세요.";
        onhelp=false;
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
        switch (item.getItemId()) {
            //도움말 버튼
            case R.id.menu_help:
                if(!onhelp) {  //도움말 활성화
                    mainlayout.setAlpha(0.3f);
                    tutlayout.setVisibility(View.VISIBLE);
                    divider14.setVisibility(View.VISIBLE);
                    btnenter14.setVisibility(View.VISIBLE);
                    btn1.setEnabled(false);
                    btn2.setEnabled(false);
                    btn3.setEnabled(false);
                    btn5.setEnabled(false);
                    btnlogin.setEnabled(false);
                    btnlogout.setEnabled(false);
                    setTitle("앱 사용 도움말");
                    ttsText="도움말을 닫으려면 도움말 버튼을 한 번 더 터치하세요.";
                    onhelp=true;
                }
                else {  //도움말 비활성화
                    tutdisabled();
                }
                return true;

            case R.id.menu_tts:
                if(!ontts) {  //tts 활성화
                    tts.speak(ttsText, TextToSpeech.QUEUE_FLUSH, null, null);
                    ontts=true;
                }
                else {  //도움말 비활성화
                    tts.stop();
                    ontts=false;
                }
                return true;

        }


        return super.onOptionsItemSelected(item);
    }


}
