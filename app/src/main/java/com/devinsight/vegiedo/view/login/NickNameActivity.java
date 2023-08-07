package com.devinsight.vegiedo.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.devinsight.vegiedo.R;

public class NickNameActivity extends AppCompatActivity {

    private TextView tt_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nickname);

        tt_next = findViewById(R.id.tt_next);
        tt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectTagActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}