package com.yiyi.farm.init.state;

import com.yiyi.farm.enumeration.http.HttpMethodEnum;
import com.yiyi.farm.util.StringUtil;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author peach
 * @date 2018-04-25 14:47:51
 * @description get请求
 */
public class GetAnnotation implements AnnotationState {
    private GetMapping getMapping;

    public GetAnnotation(GetMapping getMapping) {
        this.getMapping = getMapping;
    }

    @Override
    public HttpMethodEnum getHttpMethod() {
        return HttpMethodEnum.GET;
    }

    @Override
    public String getUrl() {
        return StringUtil.handleUrl(getMapping.value()[0]);
    }
}
