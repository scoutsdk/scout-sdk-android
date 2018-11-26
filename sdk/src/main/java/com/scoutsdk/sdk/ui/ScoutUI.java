package com.scoutsdk.sdk.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.scoutsdk.sdk.R;
import com.scoutsdk.sdk.ScoutBroadcaster;

class ScoutUI extends FrameLayout {
    final float UI_HEIGHT_PCT = 0.80f;
    final float UI_WIDTH_PCT = 0.80f;

    private WebView webView;
    private long callbackId;

    ScoutUI(Context context, AttributeSet attrs) {
        super(context, attrs);

//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PieChart, 0, 0);
//
//        try {
//            mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
//            mTextPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
//        } finally {
//            a.recycle();
//        }
//
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int uiHeight = Math.round(displayMetrics.heightPixels * UI_HEIGHT_PCT);
        int uiWidth = Math.round(displayMetrics.widthPixels * UI_WIDTH_PCT);

        webView = new WebView(context);
        WebSettings webViewSettings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient());
        webView.setLayoutParams(new LayoutParams(uiWidth, uiHeight));
        webViewSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(context), "ScoutNative");

        addView(webView);
    }

    ScoutUI(Context context, AttributeSet attrs, String page) {
        this(context, attrs);
        webView.loadUrl(getResources().getString(R.string.WEBSDK_URL) + "/" + page);
    }

    public void setCallbackId(long callbackId) {
        this.callbackId = callbackId;
    }

    public class WebAppInterface {
        Context appContext;

        WebAppInterface(Context context) {
            appContext = context.getApplicationContext();
        }

        @JavascriptInterface
        public void callback(String json) {
            ScoutBroadcaster.callback(appContext, callbackId, json);
        }
    }
}
