package com.example.myhealthapplication.Newsfeed;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myhealthapplication.R;

public class WebView extends AppCompatActivity {

    Toolbar toolbar;
    android.webkit.WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

       // toolbar = findViewById(R.id.toolBarSpecificNews);
        webView = findViewById(R.id.web_view1);

   //     setSupportActionBar(toolbar);

        Intent  intent = getIntent();
        String url = intent.getStringExtra("url");

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }
}