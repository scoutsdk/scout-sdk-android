package com.scoutsdk.sdk;

public class ScoutConfigBuilder {
    private String appId;

    public ScoutConfigBuilder appId(String appId) {
        this.appId = appId;
        return this;
    }

    public ScoutConfig build() {
        return new ScoutConfig(appId);
    }
}
