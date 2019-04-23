package com.chixing.common;

public enum ResultStatus {

    SUCCESS("success"),
    ERROR("error"),
    FAIL("fail"),
    NEED_LOGIN("need_login"),
    HAS_LOGIN("has_login");


    private String value;
    ResultStatus( String value){
        this.value = value;
    }
}
