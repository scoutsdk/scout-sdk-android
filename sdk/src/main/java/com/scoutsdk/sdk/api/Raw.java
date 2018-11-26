package com.scoutsdk.sdk.api;

import android.content.Context;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloSubscriptionCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.scoutsdk.sdk.PlayerQuery;
import com.scoutsdk.sdk.PlayerUpdateSubscription;
import com.scoutsdk.sdk.RawQuery;
import com.scoutsdk.sdk.ScoutConfig;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import java9.util.function.Consumer;

public class Raw {
    private ApolloClient apolloClient;

    public Raw(Context appContext, ScoutConfig config, ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
    }

    public void query(String title, String method, List<Object> parameters, Consumer<Response<RawQuery.Data>> callback) {
        apolloClient.query(
            RawQuery.builder()
                .title(title)
                .method(method)
                .parameters(parameters)
                .build()
        ).enqueue(new ApolloCall.Callback<RawQuery.Data>() {

            @Override public void onResponse(@NotNull Response<RawQuery.Data> res) {
                callback.accept(res);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.accept(null);
            }
        });
    }
}
