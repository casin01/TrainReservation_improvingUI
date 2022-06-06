package com.example.reservet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DiscountActivity extends Activity {

    RadioGroup radiogroup1;
    TextView seltext;
    int totaldis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        Button gobtn5 =findViewById(R.id.gobtn5);
        seltext = findViewById(R.id.seltext);
        radiogroup1=findViewById(R.id.radiogroup1);
        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rabtn1:
                        seltext.setText("선택 안 함");
                        totaldis=0;
                        break;
                    case R.id.rabtn2:
                        seltext.setText("국가유공자");
                        totaldis=200;
                        break;
                    case R.id.rabtn3:
                        seltext.setText("국가유공자 보호자");
                        totaldis=200;
                        break;
                    case R.id.rabtn4:
                        seltext.setText("지연할인증");
                        totaldis=200;
                        break;
                    case R.id.rabtn5:
                        seltext.setText("할인쿠폰/회원쿠폰");
                        totaldis=200;
                        break;
                    default:
                        break;
                }
            }
        });

        gobtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터 전달하기
                Intent intent = getIntent();
                intent.putExtra("totaldis", totaldis);
                setResult(RESULT_OK, intent);

                //액티비티(팝업) 닫기
                finish();
            }
        });

    }
}