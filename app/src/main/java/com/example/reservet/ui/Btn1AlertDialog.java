package com.example.reservet.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.reservet.R;

public class Btn1AlertDialog extends Dialog {

    private Context context;
    private CustomDialogClickListener customDialogClickListener;
    private TextView tvTitle, tvcontent;
    private Button enterbtn;
    private String titletxt, body;


    public Btn1AlertDialog(@NonNull Context context, CustomDialogClickListener customDialogClickListener, String titletxt,
                           String body) {
        super(context);
        this.context = context;
        this.customDialogClickListener = customDialogClickListener;
        this.titletxt = titletxt;
        this.body = body;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog1);

        tvTitle = findViewById(R.id.titletxt);
        tvcontent = findViewById(R.id.mesgase);
        enterbtn = findViewById(R.id.enterbtn);

        tvTitle.setText(titletxt);
        tvcontent.setText(body);

        enterbtn.setOnClickListener(v -> {
            // 확인 버튼 클릭
            this.customDialogClickListener.onPositiveClick();
            dismiss();
        });

    }
}
