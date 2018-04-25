package com.yiyi.farm.init.state;

import com.yiyi.farm.enumeration.http.HttpMethodEnum;
import com.yiyi.farm.util.StringUtil;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author peach
 * @date 2018-04-25 14:51:02
 * @description put请求
 */
public class PutAnnotation implements AnnotationState {
    private PutMapping putMapping;

    public PutAnnotation(PutMapping putMapping) {
        this.putMapping = putMapping;
    }

    @Override
    public HttpMethodEnum getHttpMethod() {
        return HttpMethodEnum.PUT;
    }

    @Override
    public String getUrl() {
        return StringUtil.handleUrl(putMapping.value()[0]);
    }
}
