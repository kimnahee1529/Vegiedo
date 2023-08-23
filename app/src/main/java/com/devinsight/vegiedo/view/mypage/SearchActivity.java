package com.devinsight.vegiedo.view.mypage;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.devinsight.vegiedo.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new BridgeInterface(), "Android");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //웹뷰 페이지(index.html) 로딩이 끝났을 때 호출됨. 최초 웹뷰 로드가 먼저 호출될 것임.
                //안드로이드 -> 자바스크립트 함수 호출
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });
        //최초 웹뷰 로드
        webView.loadUrl("https://searchaddress-2a30a.web.app");
    }

    private class BridgeInterface { //자바스크립트 -> 안드로이드
        @JavascriptInterface //이 어노테이션을 붙이는 것이 중요함. 인터페이스 이름은 바꿔도 됨.
        public void processDATA(String data) {
            //다음(카카오) 주소 검색 API의 결과 값이 브릿지 통로를 통해 전달받는다. (from Javascript)
            Intent intent = new Intent();
            intent.putExtra("data", data);
            setResult(RESULT_OK, intent);
            finish();

        }
    }
}