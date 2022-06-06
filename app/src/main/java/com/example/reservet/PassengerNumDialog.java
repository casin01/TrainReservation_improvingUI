package com.example.reservet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class PassengerNumDialog extends Activity {

    private int adult, child, notse, severe, total;
    private TextView txtad, txtch, txtno, txtse, txttotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("승차 인원 수");
        setContentView(R.layout.passnum_dialog);

        oninit();
    }

    public void oninit() {
        txtad=findViewById(R.id.adultnum);
        txtch=findViewById(R.id.child);
        txtno=findViewById(R.id.notsevdis);
        txtse=findViewById(R.id.severedis);
        txttotal=findViewById(R.id.numText);
        adult=1;
        child=0;
        notse=0;
        severe=0;
        total=adult+child+notse+severe;
    }

    public void numClick(View v) {
        switch (v.getId()) {
            case R.id.minusad:
                if (adult>0) adult--;
                break;
            case R.id.addad:
                if (adult<30) adult++;
                break;
            case R.id.minusch:
                if (child>0) child--;
                break;
            case R.id.addch:
                if (child<30) child++;
                break;
            case R.id.minusno:
                if (notse>0) notse--;
                break;
            case R.id.addno:
                if (notse<30) notse++;
                break;
            case R.id.minusse:
                if (severe>0) severe--;
                break;
            case R.id.addse:
                if (severe<30) severe++;
                break;
            case R.id.resetbtn:
                adult=1;    child=0;    notse=0;    severe=0;
                break;
            default:
                break;
        }
        txtad.setText(""+adult);
        txtch.setText(""+child);
        txtno.setText(""+notse);
        txtse.setText(""+severe);
        total=adult+child+notse+severe;
        txttotal.setText(total+ " 명");
    }


    //확인 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        Intent intent = getIntent();
        intent.putExtra("numseat", total);
        intent.putExtra("receivecode", HandlerMessage.SELECT_SEAT);
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    //팝업 바깥 클릭시 닫힘 방지
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
}