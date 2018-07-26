package com.scoutsdk.sdk.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

public class UI {
    private final int PICK_FRIENDS_REQUEST_CODE = 1000;

    public void pickFriends(Activity activity) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.putExtra("page", "search");
        intent.setComponent(new ComponentName(activity, ScoutUIActivity.class));

        activity.startActivityForResult(intent, PICK_FRIENDS_REQUEST_CODE);
    }
}
