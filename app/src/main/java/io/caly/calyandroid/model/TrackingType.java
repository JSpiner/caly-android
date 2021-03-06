package io.caly.calyandroid.model;

/**
 * Copyright 2017 JSpiner. All rights reserved.
 *
 * @author jspiner (jspiner@naver.com)
 * @project CalyAndroid
 * @since 17. 2. 28
 */

public enum  TrackingType {

    CLICK("click");

    public final String value;

    TrackingType(final String value){
        this.value = value;
    }
}
