package com.fk.mycollection.View;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.ProgressBar;


/*
 * 带进度条的WebView
 */

public class ProgressWebView extends WebView {

  private ProgressBar progressbar;


  public ProgressWebView(Context context, AttributeSet attrs) {
    super(context, attrs);
    progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
    progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 6, 0, 0));
    // progressbar.setBackgroundColor(Color.YELLOW);
    ClipDrawable d =
        new ClipDrawable(new ColorDrawable(Color.RED),// 在此处修改颜色,可在color.xml中定义进度条颜色
            Gravity.LEFT, ClipDrawable.HORIZONTAL);
    progressbar.setProgressDrawable(d);
    addView(progressbar);
    // setWebViewClient(new WebViewClient(){});
    setWebChromeClient(new WebChromeClient());
  }

  public class WebChromeClient extends android.webkit.WebChromeClient {
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
      if (newProgress == 100) {
        progressbar.setVisibility(GONE);
      } else {
        if (progressbar.getVisibility() == GONE)
          progressbar.setVisibility(VISIBLE);
        progressbar.setProgress(newProgress);
      }
      super.onProgressChanged(view, newProgress);
    }

  }

  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
    lp.x = l;
    lp.y = t;
    progressbar.setLayoutParams(lp);
    super.onScrollChanged(l, t, oldl, oldt);
  }
}
