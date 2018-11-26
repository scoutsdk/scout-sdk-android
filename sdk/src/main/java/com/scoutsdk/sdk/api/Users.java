package com.scoutsdk.sdk.api;

import android.content.Context;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.scoutsdk.sdk.PersonaQuery;
import com.scoutsdk.sdk.ScoutConfig;
import com.scoutsdk.sdk.UserQuery;

import org.jetbrains.annotations.NotNull;

import java9.util.function.Consumer;

public class Users {
    private ApolloClient apolloClient;

    public Users(Context appContext, ScoutConfig config, ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
    }

    public void get(String id, Consumer<Response<UserQuery.Data>> callback) {
        apolloClient.query(
                UserQuery.builder()
                        .id(id)
                        .build()
        ).enqueue(new ApolloCall.Callback<UserQuery.Data>() {

            @Override public void onResponse(@NotNull Response<UserQuery.Data> res) {
                callback.accept(res);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.accept(null);
            }
        });
    }
}
