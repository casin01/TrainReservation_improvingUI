package com.example.reservet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reservet.R;

import java.util.Objects;

public class Docu extends AppCompatActivity {

    public static final int TRAIN_INFO = 0;
    public static final int REFUND_INFO = 1;
    public static final int Guide_link = 2;
    public static final int TEL_HELP = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docu);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos", TRAIN_INFO);
        String title = intent.getStringExtra("title");
        TextView alerttext= findViewById(R.id.textview5);
        setTitle(title);

        String s1 = "- 코레일톡에서 구입한 승차권을 역 창구에서 변경 시 할인이 취소될 수 있습니다.\n\n" +
                "- 할인 승차권의 할인율은 별도 공지없이 변경될 수 있습니다.\n\n"+
                "- 할인은 운임에만 적용하고 요금은 미적용(특실/우등실은 운임과 요금으로 구분)되며, 최저운임 이하로 할인하지 않습니다.\n\n" +
                "- 승차 시 해당 열차 승차권을 소지해야 하며, 사진이나 캡처한 화면은 유효한 승차권이 아닙니다.\n\n" +
                "- 반려동물은 다른 고객에게 불편을 주지 않도록 케이스(이동장)에 반드시 넣어주시기 바라며, 신체 일부가 밖으로 나오지 않도록 해야 합니다.\n\n" +
                "- 반려동물의 동반좌석이 필요한 경우에는 정상운임을 내고 좌석을 지정받아 이용할 수 있습니다.\n\n" +
                "- 11월 7일부터 KTX 마일리지는 회원 본인이 이용한 승차권 중 1일 4매(결제건별 1매)까지 적립됩니다.(다만, 전달하기 승차권은 결제한 고객에 적립)\n\n" +
                "- 11월 7일부터 직접 결제하지 않고 KTX를 함께 이용한 경우 'KTX 동행자 마일리지'에서 마일리지를 적립하실 수 있습니다.";

        String s2 = "1. 일반 승차권 위약금\n" +
                "- 출발당일 ~ 출발 1시간 전 : 400원\n" +
                "- 출발 1시간 전 ~ 출발 시간 전: 10%\n" + "\n" +
                "2. 단체 승차권 위약금\n" +
                "- 2일 이전까지:400원x인원 수\n" +
                "- 출발 1일 전 ~ 출발시간 전 : 10%\n" +
                "* 단체 승차권의 위약금은 환불 인원별로 계산하여 합산한 금액을 기준으로 지급합니다.\n" + "\n" +
                "3. 열차 출발 후에는 역 창구에서만 환불이 가능합니다.\n" +
                "(단, 코레일톡에서 구매한 승차권은 열차 출발 후 10분까지 열차 내가 아님이 확인된 경우 코레일톡에서 환불이 가능합니다.)\n" + "\n" +
                "4. 도착역 도착 시각 이후에는 환불되지 않습니다.";

        switch (pos) {
            case TRAIN_INFO:
                alerttext.setText(s1);
                return;
            case REFUND_INFO:
                alerttext.setText(s2);
                return;
            default:
                return;
        }

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