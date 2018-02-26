package com.feicui.atm.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对象的属性（字段/列）名注解
 * @author 宁强
 * 这些注解在运行时被读取并且可以使用
 * 创建时间：2018-2-14 22:55:29
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String value();
}
