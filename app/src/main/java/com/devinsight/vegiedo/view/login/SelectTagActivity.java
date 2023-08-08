package com.devinsight.vegiedo.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.devinsight.vegiedo.R;
import com.devinsight.vegiedo.view.MainActivity;

public class SelectTagActivity extends AppCompatActivity {

    private TextView tt_complete;
    private TextView tt_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);

        tt_complete = findViewById(R.id.tt_complete);
        tt_back = findViewById(R.id.tt_back);

        tt_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NickNameActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}