package com.scoutsdk.sdk.api;

import android.content.Context;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloSubscriptionCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.scoutsdk.sdk.PlayerQuery;
import com.scoutsdk.sdk.PlayerUpdateSubscription;
import com.scoutsdk.sdk.ScoutConfig;

import org.jetbrains.annotations.NotNull;

import java9.util.function.Consumer;

public class Players {
    private ApolloClient apolloClient;

    public Players(Context appContext, ScoutConfig config, ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
    }

    public void get(String title, String identifier, Consumer<Response<PlayerQuery.Data>> callback) {
        get(title, identifier, null, callback);
    }

    public void get(String title, String identifier, String segment, Consumer<Response<PlayerQuery.Data>> callback) {
        apolloClient.query(
            PlayerQuery.builder()
                .title(title)
                .identifier(identifier)
                .segment(segment)
                .build()
        ).enqueue(new ApolloCall.Callback<PlayerQuery.Data>() {

            @Override public void onResponse(@NotNull Response<PlayerQuery.Data> res) {
                callback.accept(res);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.accept(null);
            }
        });
    }

    public void subscribe(String title, String identifier, String segment, Consumer<Response<PlayerUpdateSubscription.Data>> callback) {
        apolloClient.subscribe(
                PlayerUpdateSubscription.builder()
                        .title(title)
                        .identifier(identifier)
                        .segment(segment)
                        .build()
        ).execute(new ApolloSubscriptionCall.Callback<PlayerUpdateSubscription.Data>() {
            @Override
            public void onResponse(@NotNull Response<PlayerUpdateSubscription.Data> res) {
                callback.accept(res);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.accept(null);
            }

            @Override
            public void onCompleted() {

            }
        });
    }
}
