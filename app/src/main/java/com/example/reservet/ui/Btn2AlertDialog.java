package com.example.reservet.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.reservet.R;

public class Btn2AlertDialog extends Dialog {

    private Context context;
    private CustomDialogClickListener customDialogClickListener;
    private TextView tvTitle, tvcontent;
    private Button tvNegative, tvPositive;
    private String titletxt, body;
    private int ver;    //1이면 short, -1이면 long
    public static final int SHORT_DIALOG = 1;
    public static final int LONG_DIALOG = -1;

    public Btn2AlertDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener, String titletxt,
                           String body, int ver) {
        super(context);
        this.context = context;
        this.customDialogClickListener = customDialogClickListener;
        this.titletxt = titletxt;
        this.body = body;
        this.ver =ver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ver==SHORT_DIALOG)   setContentView(R.layout.dialog2btn);
        else    setContentView(R.layout.dialoglong);

        tvTitle = findViewById(R.id.titledialog);
        tvcontent = findViewById(R.id.mesgase);
        tvPositive = findViewById(R.id.okButton);
        tvNegative = findViewById(R.id.cancelButton);
        tvTitle.setText(titletxt);
        tvcontent.setText(body);

        tvPositive.setOnClickListener(v -> {
            // 확인 버튼 클릭
            this.customDialogClickListener.onPositiveClick();
            dismiss();
        });
        tvNegative.setOnClickListener(v -> {
            // 취소 버튼 클릭
            this.customDialogClickListener.onNegativeClick();
            dismiss();
        });
    }
}
