package com.yiyi.farm.init.state;

import com.yiyi.farm.enumeration.http.HttpMethodEnum;
import com.yiyi.farm.util.StringUtil;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author peach
 * @date 2018-04-25 14:49:204
 * @description post请求
 */
public class PostAnnotation implements AnnotationState {
    private PostMapping postMapping;

    public PostAnnotation(PostMapping postMapping) {
        this.postMapping = postMapping;
    }

    @Override
    public HttpMethodEnum getHttpMethod() {
        return HttpMethodEnum.POST;
    }

    @Override
    public String getUrl() {
        return StringUtil.handleUrl(postMapping.value()[0]);
    }
}
