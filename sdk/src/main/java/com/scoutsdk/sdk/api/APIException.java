package com.scoutsdk.sdk.api;

import com.apollographql.apollo.api.Error;

import java.util.List;

public class APIException extends Exception {
    private List<Error> errorList;

    public APIException(List<Error> errorList) {
        this.errorList = errorList;
    }

    @Override
    public String getMessage() {
        return errorList.get(0).message();
    }
}
