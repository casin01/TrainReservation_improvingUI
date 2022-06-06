package com.example.reservet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.iamport.sdk.data.sdk.IamPortRequest;
import com.iamport.sdk.data.sdk.PG;
import com.iamport.sdk.data.sdk.PayMethod;
import com.iamport.sdk.domain.core.Iamport;

import java.util.Date;
import java.util.Objects;

import kotlin.Unit;

public class AuthActivity extends AppCompatActivity {

    private WebView webview = null;
    private int totalcharge, numseat;
    private String date, weekday;
    private UserInfo userinfo;
    private StationInfo mStartStation, mEndStation;
    private TrainTicketInfo ticket;
    ProgressDialog progressDialog; // Loading 표시 위한 ProgressDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authactivity);
        setTitle("결제");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼

        //데이터 받아옴
        Intent intent = getIntent();
        userinfo = (UserInfo) intent.getSerializableExtra("userinfo");
        mStartStation = (StationInfo) intent.getSerializableExtra("mStartStation");
        mEndStation = (StationInfo) intent.getSerializableExtra("mEndStation");
        date = intent.getStringExtra("date");
        weekday = intent.getStringExtra("weekday");
        numseat = intent.getIntExtra("numseat", 1);
        ticket = (TrainTicketInfo) intent.getSerializableExtra("ticket");
        totalcharge = intent.getIntExtra("totalcharge", 0);
        int diswon =intent.getIntExtra("diswon", 0);
        String itemname = ticket.trainGradeName + " " + ticket.depPlaceName + " -> " + ticket.arrPlaceName;

        Iamport.INSTANCE.init(this);
        initWebView();

        IamPortRequest request = IamPortRequest.builder()
                .pg(PG.kcp.makePgRawName(""))
                .pay_method(PayMethod.card.name())
                .name(itemname)
                .merchant_uid("mid_" + (new Date()).getTime())
                .amount(500 - diswon + "")      //테스트용 기본 500원, 할인 -200원 다날 최소결제금액 300원
                .buyer_name(userinfo.userName).build();

        Iamport.INSTANCE.payment("imp33835104", null, webview, request,
                iamPortApprove -> {
                    // (Optional) CHAI 최종 결제전 콜백 함수.
                    return Unit.INSTANCE;
                }, iamPortResponse -> {
                    // 최종 결제결과 콜백 함수.
                    String responseText = iamPortResponse.toString();
                    Log.e("IAMPORT_SAMPLE_responseText", responseText);
                    afterPay(responseText);
                    return Unit.INSTANCE;
                });
    }

    /* 아임포트에서 제공하는 다날 결제 모듈은 최소 결제 금액이 300원이고
    공공데이터포털에서 adultcharge가 0원으로 등록되어있는 경우가 많아
    기본 요금은 500원으로 설정, 할인 요금은 200원으로 설정함.
     */


    //다음 intent로 값 넘겨줌
    void afterPay(String text) {
        Intent intent = new Intent(AuthActivity.this, completeticket.class);
        intent.putExtra("userinfo", userinfo);
        intent.putExtra("mStartStation", mStartStation);
        intent.putExtra("mEndStation", mEndStation);
        intent.putExtra("date", date);
        intent.putExtra("weekday", weekday);
        intent.putExtra("numseat", numseat);
        intent.putExtra("ticket", ticket);
        intent.putExtra("totalcharge", totalcharge);
        intent.putExtra("responseText", text);      //uid 받아오는데 필요함
        startActivity(intent);
    }


    // SDK 종료
    // 명시적으로 화면을 나가는 시점, 꺼지는 시점 등에 추가
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Iamport.INSTANCE.close();
        super.onDetachedFromWindow();
        if (webview != null) {
            webview.removeAllViews();
            webview.destroy();
        }

    }

    //웹뷰
    void initWebView() {
        webview = findViewById(R.id.activity_auth_webView);
        WebSettings settings = webview.getSettings();

        settings.setJavaScriptEnabled(true); // 자바스크립트 사용여부
        settings.setLoadWithOverviewMode(true);  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        settings.setUseWideViewPort(true);  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함
        settings.setSupportZoom(false);  // 줌 설정 여부
        settings.setBuiltInZoomControls(false);  // 줌 확대/축소 버튼 여부
        settings.setDefaultTextEncodingName("UTF-8");
        //생성한class넣고 javascript안에서 쓸 이름을 명시한다
        //웹에서는 호출할때, android라고 부르며 호출 시 WebAppInterface 클래스의 메소드들을 가져옴
        //webview.addJavascriptInterface(new JsHandler(), "Android");
        //webview.loadUrl("file:///android_asset/auth.html");


        webview.setWebViewClient(new WebViewClient() {  // 새 창 띄우기 않기
            // 링크 클릭에 대한 반응
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            // 웹페이지 호출시 오류 발생에 대한 처리
            @Override
            public void onReceivedHttpError(WebView webview, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(webview, request, errorResponse);
            }

            // 페이지 로딩 시작시 호출
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.setMessage("Loading");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100) progressDialog.dismiss();
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
    }

    //툴바 뒤로가기
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


/*
    class JsHandler {
        //절대필수
        @android.webkit.JavascriptInterface
        public void getData(String impUid) {

        }//가져올 매개변수타입 정의

        @android.webkit.JavascriptInterface
        public void onClickPayment() {
            IamPortRequest request = IamPortRequest.builder()
                    .pg(PG.kcp.makePgRawName("danal_tpay"))
                    .pay_method(PayMethod.card.name())
                    .name("테스트 주문")
                    .merchant_uid("mid_" + (new Date()).getTime())
                    .amount(total+"")
                    .buyer_name(username).build();

            // 결제호출
            Iamport.INSTANCE.payment("imp33835104", null, null, request,
                    iamPortApprove -> {
                        // (Optional) CHAI 최종 결제전 콜백 함수.
                        return Unit.INSTANCE;
                    }, iamPortResponse -> {
                        // 최종 결제결과 콜백 함수.
                        String responseText = iamPortResponse.toString();
                        Log.e("IAMPORT_SAMPLE", responseText);
                        return Unit.INSTANCE;
                    });
        }
    }
    */

}