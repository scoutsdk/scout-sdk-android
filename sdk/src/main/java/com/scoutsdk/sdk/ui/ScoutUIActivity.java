package com.scoutsdk.sdk.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import com.scoutsdk.sdk.R;


public class ScoutUIActivity extends Activity {

    private ScoutUI ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.Theme_Modal);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String page = intent.getStringExtra("page");
        long callbackId = intent.getLongExtra("callbackId", 0L);

        if (page != null) {
            switch (page) {
                case "search":
                    ui = new SearchWindow(this, null);
                    break;
            }
        }

        if (ui != null) {
            ui.setCallbackId(callbackId);
        }

        setContentView(ui);
    }

    @Override
    protected void onStop() {
        super.onStop();

        ((ViewGroup)ui.getParent()).removeView(ui);
    }
}

