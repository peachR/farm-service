package com.yiyi.farm;

import com.yiyi.farm.annotation.Login;
import com.yiyi.farm.controller.invite.InviteController;
import com.yiyi.farm.controller.invite.InviteControllerImpl;
import com.yiyi.farm.enumeration.accessAuth.Permission;
import com.yiyi.farm.rsp.Result;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class FarmClient {
    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//        Result result = restTemplate.getForObject("http://localhost:8080/invite/redEnvelope?phone=13478569427&startTime=1518107481&endTime=1522115764&totalConsume=100&chargeConsume=50", Result.class);
//        System.out.println(result);
        Class<InviteController> clazz= InviteController.class;
        Method[] methods = clazz.getMethods();
        for(Method method: methods){
            if(method.isAnnotationPresent(Login.class)){
                System.out.println(method);
                System.out.println(method.getAnnotation(Login.class).value());
            }
            if(method.isAnnotationPresent(com.yiyi.farm.annotation.Permission.class)){
                System.out.println(method);
                System.out.println(method.getAnnotation(com.yiyi.farm.annotation.Permission.class).getClass().getAnnotation(Login.class).value());
            }
        }
    }
}
