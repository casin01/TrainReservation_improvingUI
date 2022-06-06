package com.example.reservet;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class StationList extends Activity {

    private GridView gridview;
    ArrayList<StationInfo> mInfo;   //역info arraylist
    AssetManager mag;
    String name_buffer;
    String code_buffer;
    StationListAdapter gAdapter;
    int requestcode;
    private StationInfo sel_station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("역 선택");
        setContentView(R.layout.activity_station_list);

        //출발, 도착 구분
        Intent intent = getIntent();
        requestcode = intent.getIntExtra("requestCode", 0);

        onInit();
        ReadStation();

        gAdapter = new StationListAdapter(this, android.R.layout.activity_list_item, mInfo);
        gridview.setAdapter(gAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                sel_station = (StationInfo) gAdapter.getItem(position);
                Log.e("gridview selected", "선택역 이름 "+ sel_station.st_station+ " 선택 역 아이디 "+ sel_station.st_stationCode);
                Intent intent =new Intent();
                intent.putExtra("receivecode", requestcode);
                intent.putExtra("stationInfo", sel_station);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    void onInit()
    {
        mag = this.getResources().getAssets();   // Assets 초기화
        mInfo = new ArrayList<>();
        /*
        et_searchStation = findViewById(R.id.et_stationSearch);
        et_searchStation.addTextChangedListener((new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { // 초성검색을 위한 함수, L
                String temp = et_searchStation.getText().toString();
                gAdapter.Filter(temp); // 단어 입력시 Filter 함수에 접근,
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }));
        */
        gridview = findViewById(R.id.gridview);

    }

    /*
    //역 리스트 불러올 때 핸들러
    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HandlerMessage.THREAD_HANDLER_SUCCESS_INFO:
                    mResult = getIntent();
                    StationInfo tResult = (StationInfo)msg.obj;
                    mResult.putExtra("receivecode", requestcode);
                    mResult.putExtra("stationInfo", tResult);
                    setResult(RESULT_OK,mResult);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
*/

    //역 리스트 불러오기
    void ReadStation()
    {
        try {
            InputStream name = mag.open("InfoTrainStation.txt");
            InputStream code = mag.open("InfoTrainStationCode.txt");
            int nameSize = name.available();
            int codeSize = code.available();

            byte[] buffer = new byte[nameSize];
            byte[] buffer2 = new byte[codeSize];

            name.read(buffer);
            code.read(buffer2);

            name.close();
            code.close();

            name_buffer = new String(buffer);
            code_buffer = new String(buffer2);

            StringToToken(name_buffer,code_buffer);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }


    }

    void StringToToken(String name,String code)
    {
        StringTokenizer mToken = new StringTokenizer(name, "\r\n");
        StringTokenizer mToken2 = new StringTokenizer(code, "\r\n");

        while(mToken.hasMoreTokens() )
        {
            String mName = mToken.nextToken();
            String mCode = mToken2.nextToken();

            StationInfo temp = new StationInfo(mName, mCode);
            mInfo.add(temp);
        }
    }

    /*
    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }
*/

    //각 초성 버튼
    public void iniClick(View v) {
        switch (v.getId()) {
            case R.id.btn1: //ㄱ
                gAdapter.__Filter("ㄱ");
                break;
            case R.id.btn2: //ㄴ
                gAdapter.__Filter("ㄴ");
                break;
            case R.id.btn3: //ㄷ
                gAdapter.__Filter("ㄷ");
                break;
            case R.id.btn4: //ㄹ
                gAdapter.__Filter("ㄹ");
                break;
            case R.id.btn5: //ㅁ
                gAdapter.__Filter("ㅁ");
                break;
            case R.id.btn6: //ㅂ
                gAdapter.__Filter("ㅂ");
                break;
            case R.id.btn7: //ㅅ
                gAdapter.__Filter("ㅅ");
                break;
            case R.id.btn8: //ㅇ
                gAdapter.__Filter("ㅇ");
                break;
            case R.id.btn9: //ㅈ
                gAdapter.__Filter("ㅈ");
                break;
            case R.id.btn10: //ㅊ
                gAdapter.__Filter("ㅊ");
                break;
            case R.id.btn11: //ㅋ
                gAdapter.__Filter("ㅋ");
                break;
            case R.id.btn12: //ㅌ
                gAdapter.__Filter("ㅌ");
                break;
            case R.id.btn13: //ㅍ
                gAdapter.__Filter("ㅍ");
                break;
            case R.id.btn14: //ㅎ
                gAdapter.__Filter("ㅎ");
                break;
        }
    }
    
    //바깥레이어 클릭시 안닫히게
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

}