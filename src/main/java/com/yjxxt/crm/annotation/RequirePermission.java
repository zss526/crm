package com.yjxxt.crm.annotation;


import java.lang.annotation.*;

/**
 * 自定义注解 记录资源码
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    String code() default "";
}
