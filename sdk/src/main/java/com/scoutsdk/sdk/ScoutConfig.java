package com.scoutsdk.sdk;

public class ScoutConfig {
    private final String clientId;
    private final String graphVersion = "1.1.0";


    public ScoutConfig(final String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getGraphVersion() {
        return graphVersion;
    }
}