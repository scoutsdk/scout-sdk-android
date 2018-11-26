package com.scoutsdk.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.ScalarType;
import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.scoutsdk.sdk.api.Personas;
import com.scoutsdk.sdk.api.Players;
import com.scoutsdk.sdk.api.Raw;
import com.scoutsdk.sdk.api.Titles;
import com.scoutsdk.sdk.api.Users;
import com.scoutsdk.sdk.type.CustomType;
import com.scoutsdk.sdk.ui.UI;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;

import static android.provider.Settings.Secure.ANDROID_ID;

public class Scout {
    private Context context;
    public final ScoutConfig config;
    private Personas personas;
    private Players players;
    private Raw raw;
    private Titles titles;
    private Users users;
    private UI ui;
    private ApolloClient apolloClient;

    private static final String GRAPH_HTTP_URL = "https://api.scoutsdk.com/graph";
    private static final String GRAPH_WS_URL = "wss://api.scoutsdk.com/graph";

    public Scout(Context context, ScoutConfig config) {
        this.context = context;
        this.config = config;

        Context appContext = context.getApplicationContext();
        ScoutBroadcaster broadcaster = new ScoutBroadcaster(appContext);
        appContext.registerReceiver(broadcaster,  new IntentFilter(ScoutBroadcaster.ACTION));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
//                                .header("Authorization", null)
                            .header("Accept", "application/com.scoutsdk.graph+json; version=1.1.0; charset=utf8")
                            .header("Scout-App", config.getClientId())
                            .header("Scout-Device", getDeviceId());

                    return chain.proceed(requestBuilder.build());
                })
                .build();

        String graphWSUrl = Uri.parse(GRAPH_WS_URL)
                .buildUpon()
                .appendQueryParameter("app", config.getClientId())
                .appendQueryParameter("device", getDeviceId())
//                .appendQueryParameter("access_token", null)
                .build()
                .toString();

        CustomTypeAdapter<Object> dynamicTypeAdapter = new CustomTypeAdapter<Object>() {
            @Override public Object decode(CustomTypeValue value) {
                return value.value;
            }

            @Override public CustomTypeValue encode(Object value) {
                return CustomTypeValue.GraphQLJson.fromRawValue(value);
            }
        };

        apolloClient = ApolloClient.builder()
                .serverUrl(GRAPH_HTTP_URL)
                .okHttpClient(okHttpClient)
                .subscriptionTransportFactory(new WebSocketSubscriptionTransport.Factory(graphWSUrl, okHttpClient))
                .addCustomTypeAdapter(CustomType.DYNAMIC, dynamicTypeAdapter)
                .addCustomTypeAdapter(CustomType.RAWDATA, dynamicTypeAdapter)
                .build();

        this.personas = new Personas(appContext, config, apolloClient);
        this.players = new Players(appContext, config, apolloClient);
        this.raw = new Raw(appContext, config, apolloClient);
        this.titles = new Titles(appContext, config, apolloClient);
        this.users = new Users(appContext, config, apolloClient);
//        this.ui = new UI(appContext, broadcaster);
    }

    public Personas personas() {
        return personas;
    }

    public Players players() {
        return players;
    }

    public Raw raw() {
        return raw;
    }

    public Titles titles() {
        return titles;
    }

    public Users users() {
        return users;
    }

    public UI ui() {
        return ui;
    }

    private String getDeviceId() {
        String androidDeviceId = ANDROID_ID;
        try {
            androidDeviceId = AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
        } catch (Exception e) {
            androidDeviceId = ANDROID_ID;
        }
        return androidDeviceId;
    }
}
