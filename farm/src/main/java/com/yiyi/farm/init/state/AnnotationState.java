package com.yiyi.farm.init.state;

import com.yiyi.farm.enumeration.http.HttpMethodEnum;

/**
 * @author peach
 * @date 2018-04-25 14:44:18
 * @description 状态模式，判定注解类型
 */
public interface AnnotationState {
    HttpMethodEnum getHttpMethod();
    String getUrl();
}
