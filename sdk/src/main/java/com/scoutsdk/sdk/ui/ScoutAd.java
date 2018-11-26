package com.scoutsdk.sdk.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.scoutsdk.sdk.R;


public class ScoutAd extends FrameLayout {

    private AdView adView;

    public ScoutAd(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScoutAd, 0, 0);

        String adUnitId;
        try {
            adUnitId = a.getString(R.styleable.ScoutAd_adUnitId);
        } finally {
            a.recycle();
        }

        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        adView = new AdView(context);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(adUnitId);

        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

        adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

        addView(adView);

        adView.loadAd(adRequestBuilder.build());
    }
}
