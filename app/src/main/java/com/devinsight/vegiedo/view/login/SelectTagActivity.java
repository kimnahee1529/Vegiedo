package com.devinsight.vegiedo.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.view.MainActivity;

public class SelectTagActivity extends AppCompatActivity {

    private TextView btn_complete;
    private TextView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);

        btn_complete = findViewById(R.id.tt_complete);
        btn_back = findViewById(R.id.tt_back);

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NickNameActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}