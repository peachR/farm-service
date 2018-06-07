package com.yiyi.farm.handler;

import com.yiyi.farm.entity.access.AccessAuthEntity;
import com.yiyi.farm.entity.user.AdminEntity;
import com.yiyi.farm.excpetion.CommonException;
import com.yiyi.farm.excpetion.ExpCodeEnum;
import com.yiyi.farm.facade.redis.RedisService;
import com.yiyi.farm.util.ArrayUtil;
import com.yiyi.farm.util.RedisPrefixUtil;
import com.yiyi.farm.util.StringUtil;
import com.yiyi.farm.util.generator.AccessAuthGenerator;
import com.yiyi.farm.util.generator.KeyGenerator;
import com.yiyi.farm.util.generator.SessionGenerator;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author peach
 * @date 2018-05-08 16:27:34
 * @description 权限控制处理类
 */
@Component
@Aspect
public class AccessAuthHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private KeyGenerator sessionGenerator = SessionGenerator.getInstance();
    private KeyGenerator accessGenerator = AccessAuthGenerator.getInstance();

    @Value("$session.sessionIdName}")
    private String sessionIdName;

    @Autowired
    private RedisService redisService;


    @Pointcut("execution(public * com.yiyi.farm.controller..*.*(..))")
    public void accessAuth(){}

    /**
     * 拦截所有控制方法，在执行前验证权限
     */
    @Before("accessAuth()")
    public void authentication(){
        HttpServletRequest request = getRequest();

        String sessionId = getSessionId(request);

        AdminEntity adminEntity = (AdminEntity) redisService.get(sessionGenerator.getKey(sessionId));

        AccessAuthEntity accessAuthEntity = getAccessAuthEntity(request);

        if(accessAuthEntity == null)
            return;

        doAuthentication(adminEntity, accessAuthEntity);

    }


    /**
     * 根据请求用户身份和方法要求权限进行权限验证
     * @param adminEntity
     * @param accessAuthEntity
     */
    private void doAuthentication(AdminEntity adminEntity, AccessAuthEntity accessAuthEntity){
        //assert accessAuthEntity != null
        if(!accessAuthEntity.isLogin())
            return;
        checkLogin(adminEntity, accessAuthEntity);
        checkPermission(adminEntity, accessAuthEntity);
    }

    /**
     * 权限验证
     * @param adminEntity
     * @param accessAuthEntity
     */
    private void checkPermission(AdminEntity adminEntity, AccessAuthEntity accessAuthEntity) {
        String[] needPermissions = accessAuthEntity.getPermission();
        List<String> havePermissions = adminEntity.getRoles().stream().map(
                permission -> permission.getDescription()
        ).collect(Collectors.toList());
        for(String needPermission : needPermissions){
            if(!havePermissions.contains(needPermission))
                throw new CommonException(ExpCodeEnum.NO_PERMISSION);
        }
    }

    /**
     * 登录验证
     * @param adminEntity
     * @param accessAuthEntity
     */
    private void checkLogin(AdminEntity adminEntity, AccessAuthEntity accessAuthEntity) {
        if(accessAuthEntity.isLogin() && adminEntity == null)
            throw new CommonException(ExpCodeEnum.UNLOGIN);
    }

    /**
     * 获取该方法所需权限
     * @param request
     * @return
     */
    private AccessAuthEntity getAccessAuthEntity(HttpServletRequest request) {
        String url = StringUtil.handleUrl(request.getServletPath());
        String method = request.getMethod();

        return (AccessAuthEntity)redisService.hget(RedisPrefixUtil.ACCESS_AUTH_PREFIX, accessGenerator.getKey(method + url));
    }

    /**
     * 获取本次请求用户的sessionid
     * @param request
     * @return
     */
    private String getSessionId(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String sessionID = null;
        if(!ArrayUtil.isEmpty(cookies)){
            for(Cookie cookie: cookies){
                if(sessionIdName.equals(cookie.getName())){
                    sessionID = cookie.getValue();
                    break;
                }
            }
        }
        return sessionID;
    }

    /**
     * 获取当前请求对象
     * @return
     */
    private HttpServletRequest getRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)requestAttributes;
        return servletRequestAttributes.getRequest();
    }
}
