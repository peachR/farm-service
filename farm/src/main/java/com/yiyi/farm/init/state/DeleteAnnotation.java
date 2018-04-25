package com.yiyi.farm.init.state;

import com.yiyi.farm.enumeration.http.HttpMethodEnum;
import com.yiyi.farm.util.StringUtil;
import org.springframework.web.bind.annotation.DeleteMapping;

public class DeleteAnnotation implements AnnotationState {
    private DeleteMapping deleteMapping;


    public DeleteAnnotation(DeleteMapping deleteMapping) {
        this.deleteMapping = deleteMapping;
    }

    @Override
    public HttpMethodEnum getHttpMethod() {
        return HttpMethodEnum.DELETE;
    }

    @Override
    public String getUrl() {
        return StringUtil.handleUrl(deleteMapping.value()[0]);
    }
}
