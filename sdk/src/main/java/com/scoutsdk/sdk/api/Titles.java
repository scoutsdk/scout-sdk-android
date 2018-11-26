package com.scoutsdk.sdk.api;

import android.content.Context;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.scoutsdk.sdk.PersonaQuery;
import com.scoutsdk.sdk.ScoutConfig;
import com.scoutsdk.sdk.TitleQuery;
import com.scoutsdk.sdk.TitlesQuery;

import org.jetbrains.annotations.NotNull;

import java9.util.function.Consumer;

public class Titles {
    private ApolloClient apolloClient;

    public Titles(Context appContext, ScoutConfig config, ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
    }

    public void get(String identifier, Consumer<Response<TitleQuery.Data>> callback) {
        apolloClient.query(
                TitleQuery.builder()
                        .identifier(identifier)
                        .build()
        ).enqueue(new ApolloCall.Callback<TitleQuery.Data>() {

            @Override public void onResponse(@NotNull Response<TitleQuery.Data> res) {
                callback.accept(res);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.accept(null);
            }
        });
    }

    public void list(Consumer<Response<TitlesQuery.Data>> callback) {
        apolloClient.query(
                TitlesQuery.builder()
                    .build()
        ).enqueue(new ApolloCall.Callback<TitlesQuery.Data>() {

            @Override public void onResponse(@NotNull Response<TitlesQuery.Data> res) {
                callback.accept(res);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.accept(null);
            }
        });
    }
}
