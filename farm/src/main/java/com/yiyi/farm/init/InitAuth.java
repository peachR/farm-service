package com.yiyi.farm.init;

import com.yiyi.farm.annotation.AuthScan;
import com.yiyi.farm.annotation.Login;
import com.yiyi.farm.annotation.Permission;
import com.yiyi.farm.entity.access.AccessAuthEntity;
import com.yiyi.farm.enumeration.http.HttpMethodEnum;
import com.yiyi.farm.facade.redis.RedisService;
import com.yiyi.farm.init.state.*;
import com.yiyi.farm.util.*;
import com.yiyi.farm.util.generator.AccessAuthGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

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
@Component
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
            if(!clazz.isInterface())
                continue;
            Method[] methods = clazz.getMethods();
            if(ArrayUtil.isEmpty(methods))
                continue;
            else{
                String prefixUrl = getPrefixUrl(clazz);
                buildAccessMethod(methods, StringUtil.handleUrl(prefixUrl));
            }
        }

        redisService.hmset(RedisPrefixUtil.ACCESS_AUTH_PREFIX, this.accessAuthMap);
    }

    /**
     * 获取url前缀，即申明在类上的RequestMapping的值
     * @param clazz
     * @return
     */
    private String getPrefixUrl(Class<?> clazz) {
        String prefix = "";
        if(clazz.isAnnotationPresent(RequestMapping.class)){
            RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
            prefix = requestMapping.value()[0];
        }
        return prefix;
    }

    /**
     * 构建所有方法的权限实体，存在映射表中
     * 键由{@code generateKey}方法生成
     * @param methods
     */
    private void buildAccessMethod(Method[] methods, String prefixUrl){
        final Map<String, AccessAuthEntity> accessAuthEntityMap = this.accessAuthMap;
        for(Method method : methods){
            AccessAuthEntity accessAuthEntity = buildAccess(method, prefixUrl);
            if(accessAuthEntity != null) {
                String key = generateKey(accessAuthEntity);
                accessAuthEntityMap.put(key, accessAuthEntity);
            }
        }
    }

    /**
     * 根据权限实体，生成其保存在redis中的唯一键
     * @param accessAuthEntity
     * @return
     */
    private String generateKey(AccessAuthEntity accessAuthEntity){
        HttpMethodEnum httpMethodEnum = accessAuthEntity.getHttpMethodEnum();
        return AccessAuthGenerator.getInstance().getKey(httpMethodEnum.getMsg() + accessAuthEntity.getUrl());
    }

    /**
     * 根据方法，构建方法的权限实体
     * @param method
     * @param prefix 前缀，class上RequestMapping的url
     * @return
     */
    private AccessAuthEntity buildAccess(Method method, String prefix){
        boolean isLogin = AnnotationUtil.getAnnotationValueByMethod(method, Login.class)
                .map(login -> login.value()).orElse(false);
        String[] permssion = AnnotationUtil.getAnnotationValueByMethod(method, Permission.class)
                .map(permission -> permission.value()).orElse(EMPTY_ARRAY);

        AnnotationState annotationState = AnnotationStateFactory.getMethodAnnotation(method);
        String url = annotationState.getUrl();
        HttpMethodEnum httpMethodEnum = annotationState.getHttpMethod();
        if(permssion != EMPTY_ARRAY)
            isLogin = true;

        return AccessAuthEntity.ofNullable(prefix + url,method.getName(),httpMethodEnum,isLogin,permssion);

    }
}
