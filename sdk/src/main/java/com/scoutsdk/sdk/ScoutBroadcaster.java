package com.scoutsdk.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.squareup.moshi.Moshi;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import java9.util.function.Consumer;

public class ScoutBroadcaster extends BroadcastReceiver {
    public static final String ACTION = "com.scoutsdk.sdk.CALLBACK";

    private Context context;
    private Map<Long, Consumer<String>> callbacks = new HashMap<>();
    private Random random = new Random();

    private final Moshi moshi = new Moshi.Builder()
            .build();

    public ScoutBroadcaster(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long callbackId = intent.getLongExtra("callbackId", 0L);
        String serialized = intent.getStringExtra("data");
        Consumer<String> callback = callbacks.remove(callbackId);
        callback.accept(serialized);
    }

    public long registerCallback(Consumer<String> callback) {
        long id = nextLong(random);
        while (id == 0) id = nextLong(random);
        callbacks.put(id, callback);
        return id;
    }

    public static void callback(Context appContext, long callbackId, String json) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtra("callbackId", callbackId);
        intent.putExtra("data", json);
        appContext.sendBroadcast(intent);
    }

    long nextLong(Random rng) {
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
            val = bits % Long.MAX_VALUE;
        } while (bits-val+(Long.MAX_VALUE-1) < 0L);
        return val;
    }
}
