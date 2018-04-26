package com.yiyi.farm.init;

import com.yiyi.farm.annotation.AuthScan;
import com.yiyi.farm.annotation.Login;
import com.yiyi.farm.annotation.Permission;
import com.yiyi.farm.entity.access.AccessAuthEntity;
import com.yiyi.farm.enumeration.http.HttpMethodEnum;
import com.yiyi.farm.facade.redis.RedisService;
import com.yiyi.farm.init.state.*;
import com.yiyi.farm.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author peach
 * @date 2018-04-25 11:09:54
 * @description 初始化权限信息
 */
@AuthScan("com.yiyi.farm.controller")
public class InitAuth implements CommandLineRunner {

    @Autowired
    private RedisService redisService;

    //反斜杠
    private static final String BACK_SLASH = "/";

    private Map<String, AccessAuthEntity> accessAuthMap = MapUtil.newHashMap();

    private static final String[] EMPTY_ARRAY = new String[0];

    @Override
    public void run(String... strings) throws Exception {
        loadAccessAuth();
    }

    private void loadAccessAuth() throws IOException{
        AuthScan authScan = AnnotationUtil.getAnnotationValueByClass(this.getClass(), AuthScan.class).get();
        String pkgName = authScan.value();

        List<Class<?>> classes = ClassUtil.getClasses(pkgName);
        for(Class<?> clazz : classes){
            Method[] methods = clazz.getMethods();
            if(ArrayUtil.isEmpty(methods))
                continue;
            else{
                buildAccessMethod(methods);
            }
        }

        redisService.hmset(RedisPrefixUtil.ACCESS_AUTH_PREFIX, this.accessAuthMap, null);
    }

    private void buildAccessMethod(Method[] methods){
        final Map<String, AccessAuthEntity> accessAuthEntityMap = this.accessAuthMap;
        for(Method method : methods){
            AccessAuthEntity accessAuthEntity = buildAccess(method);
            String key = generateKey(accessAuthEntity);
            accessAuthEntityMap.put(key, accessAuthEntity);
        }
    }

    private String generateKey(AccessAuthEntity accessAuthEntity){
        HttpMethodEnum httpMethodEnum = accessAuthEntity.getHttpMethodEnum();
        return httpMethodEnum.getMsg() + accessAuthEntity.getUrl();
    }

    private AccessAuthEntity buildAccess(Method method){
        boolean isLogin = AnnotationUtil.getAnnotationValueByMethod(method, Login.class)
                .map(login -> ((Login)login).value()).orElse(false);
        String[] permssion = AnnotationUtil.getAnnotationValueByMethod(method, Permission.class)
                .map(permission -> ((Permission)permission).value()).orElse(EMPTY_ARRAY);

        AnnotationState annotationState = getMethodAnnotation(method);
        String url = annotationState.getUrl();
        HttpMethodEnum httpMethodEnum = annotationState.getHttpMethod();

        return new AccessAuthEntity(url,method.getName(),httpMethodEnum,isLogin,permssion);

    }

    @SuppressWarnings("unchecked")
    private AnnotationState getMethodAnnotation(Method method) {
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
        else
            return new DeleteAnnotation(deleteMapping);
    }


}
