package com.yjxxt.crm.interceptor;

import com.yjxxt.crm.exceptions.NoLoginException;
import com.yjxxt.crm.mapper.UserMapper;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *拦截未登录
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //若未登录则拦截
        //获取登录的用户id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        if(userId==null || userService.selectByPrimaryKey(userId)==null){
            throw new NoLoginException("用户未登录");
        }

        return true;
    }
}
