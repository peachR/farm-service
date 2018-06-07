package com.yiyi.farm.init.state;

import com.yiyi.farm.enumeration.http.HttpMethodEnum;

public class NullPointerAnnotation implements AnnotationState {
    @Override
    public HttpMethodEnum getHttpMethod() {
        return null;
    }

    @Override
    public String getUrl() {
        return null;
    }
}
