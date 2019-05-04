package com.fk.mycollection.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fk.mycollection.R;
import com.fk.mycollection.View.ProgressWebView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressWebViewActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    ProgressWebView mProgressWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_web_view);
        ButterKnife.bind(this);
        loadWeb();
    }
    public void loadWeb() {
        // 用法同WebView
        mProgressWebView.getSettings().setJavaScriptEnabled(true);
        mProgressWebView.loadUrl("http://www.bing.com");
        mProgressWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
}
