package com.yiyi.farm.init.state;

import com.yiyi.farm.util.AnnotationUtil;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;

public class AnnotationStateFactory {
    /**
     * 根据方法获取其RequestMapping注解状态(GET,POST,PUT,DELETE)
     * @param method
     * @return
     */
    @SuppressWarnings("unchecked")
    public static AnnotationState getMethodAnnotation(Method method) {
        GetMapping getMapping = AnnotationUtil.<GetMapping>getAnnotationValueByMethod(method, GetMapping.class)
                .orElse(null);
        PostMapping postMapping = AnnotationUtil.<PostMapping>getAnnotationValueByMethod(method, PostMapping.class)
                .orElse(null);
        PutMapping putMapping = AnnotationUtil.<PutMapping>getAnnotationValueByMethod(method, PutMapping.class)
                .orElse(null);
        DeleteMapping deleteMapping = AnnotationUtil.<DeleteMapping>getAnnotationValueByMethod(method, DeleteMapping.class)
                .orElse(null);
        if(getMapping != null)
            return new GetAnnotation(getMapping);
        else if(postMapping != null)
            return new PostAnnotation(postMapping);
        else if(putMapping != null)
            return new PutAnnotation(putMapping);
        else if(deleteMapping != null)
            return new DeleteAnnotation(deleteMapping);
        return new NullPointerAnnotation();
    }
}
