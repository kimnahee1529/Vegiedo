package com.devinsight.vegiedo.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devinsight.vegiedo.R;

public class LoginMainActivity extends AppCompatActivity {

    private ImageView btn_kakaoLogin;
    private ImageView btn_googleLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        btn_kakaoLogin = findViewById(R.id.btn_kakaoLogin);
        btn_googleLogin = findViewById(R.id.btn_googleLogin);

        btn_kakaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NickNameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NickNameActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}