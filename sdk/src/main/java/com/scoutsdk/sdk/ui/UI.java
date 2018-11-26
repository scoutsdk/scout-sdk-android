package com.scoutsdk.sdk.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.scoutsdk.sdk.ScoutBroadcaster;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import java9.util.function.Consumer;

public class UI {
    private Context appContext;
    private ScoutBroadcaster broadcaster;

    private final Moshi moshi = new Moshi.Builder()
            .build();

    public UI(Context appContext, ScoutBroadcaster broadcaster) {
        this.appContext = appContext;
        this.broadcaster = broadcaster;
    }

    public void search(Consumer<Object> callback) {
        goToPageWithParams(Object.class, "search", null, callback);
    }

    public void pickFriends(Consumer<Object> callback) {
        goToPageWithParams(Object.class, "friends", null, callback);
    }

    private <T> void goToPageWithParams(Type returnType, String page, Map<String, Object> params, Consumer<T> callback) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        intent.putExtra("page", page);
        intent.setComponent(new ComponentName(appContext, ScoutUIActivity.class));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        JsonAdapter<Object> adapter = moshi.adapter(returnType);

        long callbackId = broadcaster.registerCallback((json) -> {
            T obj;
            try {
                obj = (T)adapter.fromJson(json);
            } catch (IOException e) {
                obj = null;
            }
            callback.accept(obj);
        });

        intent.putExtra("callbackId", callbackId);

        appContext.startActivity(intent);
    }
}
