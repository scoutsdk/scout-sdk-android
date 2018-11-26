package com.scoutsdk.sdk;

public class ScoutConfigBuilder {
    private String clientId;

    public ScoutConfigBuilder clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public ScoutConfig build() {
        return new ScoutConfig(clientId);
    }
}
