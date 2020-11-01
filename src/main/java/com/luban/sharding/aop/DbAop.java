package com.luban.sharding.aop;

import com.luban.sharding.annotation.MyDataSource;
import com.luban.sharding.utils.DbUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class DbAop {

    @Before(value = "execution(* com.luban.sharding.mapper..*.*(..))")
    public void before(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MyDataSource annotation = method.getAnnotation(MyDataSource.class);
        if(annotation==null){
            DbUtil.setDb(DbUtil.master);
           return;
        }
        System.out.println(annotation.value()+"---------------");
        DbUtil.setDb(annotation.value());
    }

}
