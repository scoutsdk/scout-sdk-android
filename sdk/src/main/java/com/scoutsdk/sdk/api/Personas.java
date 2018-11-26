package com.scoutsdk.sdk.api;

import android.content.Context;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloSubscriptionCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.scoutsdk.sdk.PersonaQuery;
import com.scoutsdk.sdk.PlayerQuery;
import com.scoutsdk.sdk.PlayerUpdateSubscription;
import com.scoutsdk.sdk.ScoutConfig;

import org.jetbrains.annotations.NotNull;

import java9.util.function.Consumer;

public class Personas {
    private ApolloClient apolloClient;

    public Personas(Context appContext, ScoutConfig config, ApolloClient apolloClient) {
        this.apolloClient = apolloClient;
    }

    public void get(String id, Consumer<Response<PersonaQuery.Data>> callback) {
        apolloClient.query(
                PersonaQuery.builder()
                        .id(id)
                        .build()
        ).enqueue(new ApolloCall.Callback<PersonaQuery.Data>() {

            @Override public void onResponse(@NotNull Response<PersonaQuery.Data> res) {
                callback.accept(res);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                callback.accept(null);
            }
        });
    }
}
