package com.vision.grievanceredressal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class LoginWeb extends Home {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Login");

        ViewFlipper view = (ViewFlipper) findViewById(R.id.viewer);
        view.setDisplayedChild(5);

        WebView webvu = findViewById(R.id.webvu);
        WebSettings webSettings = webvu.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);

        webvu.setWebViewClient(new LoginWebViewClient());
        webvu.loadUrl("http://13.59.251.253:8000/login/");
    }

    @Override
    public void onBackPressed() {
        WebView webvu = findViewById(R.id.webvu);
        if(webvu.canGoBack())
            webvu.goBack();
        else
            super.onBackPressed();
    }

    class LoginWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("13.59.251.253")) {
                if (Uri.parse(url).getPath().contains("/login/")) {
                    setTitle("Login");
                } else
                    setTitle("User");

                if (Uri.parse(url).getPath().contains("/problems/status/")) {
                    Intent intent = new Intent(getApplicationContext(), checkStatus.class);
                    startActivity(intent);
                    return true;
                } else if (Uri.parse(url).getPath().equals("/problems/register/")) {
                    Intent intent = new Intent(getApplicationContext(), registerComplaint.class);
                    startActivity(intent);
                    return true;
                }

                return false;
            }

            return true;
        }
    }

}
