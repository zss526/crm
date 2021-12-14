package com.yjxxt.crm.aop;

import com.yjxxt.crm.annotation.RequirePermission;
import com.yjxxt.crm.exceptions.NoLoginException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 *  后端级别的访问控制
 */
@Component
@Aspect
public class PermissionProxy {

    @Autowired
    private HttpSession session;

    @Around(value = "@annotation(com.yjxxt.crm.annotation.RequirePermission)")
    public Object around(ProceedingJoinPoint pjp){
        //获取资源码
        List<String> permissions = (List<String>) session.getAttribute("permissions");
        if(permissions==null ||permissions.size()==0){
            throw new NoLoginException("未登录");
        }
        Object result=null;
        //获取方法注解的资源码进行判断
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        RequirePermission requirePermission = methodSignature.getMethod().getDeclaredAnnotation(RequirePermission.class);
        if(!(permissions.contains(requirePermission))){
            throw new NoLoginException("权限不够，兄弟");
        }
        try {
            result=pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
}
