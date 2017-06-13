package cn.superman.web.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.superman.web.permission.Permissions;

/**
 * 用于修饰Controller方法权限的注解
 * 
 * @author 梁浩辉
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AdminPermission {
	Permissions[] value();
}
