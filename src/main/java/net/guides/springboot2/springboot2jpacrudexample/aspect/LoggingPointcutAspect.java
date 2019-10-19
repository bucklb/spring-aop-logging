package net.guides.springboot2.springboot2jpacrudexample.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*
    Might be easier to play with stuff if it's split in a meaningful way.
    Make it easier to toggle off playing with things by just commenting out @aspect

    Look at defining pointcuts with various matcing approaches and then play with combining them
    and get an idea how convoluted the combinations might get
 */

//@Aspect
@Component
@Order(10)
public class LoggingPointcutAspect {

    // Declare pointcuts up front & see how they combine

    // Anything in the service package
    @Pointcut(value="execution(* net.guides.springboot2.springboot2jpacrudexample.service.*.*(..))")
    public void anySvcMethod() { }

    // Anything in the controller package
    @Pointcut(value="execution(* net.guides.springboot2.springboot2jpacrudexample.controller.*.*(..))")
    public void anyConMethod() { }

    // Can we use wildcards as part of names (anf force inclusion/exclusion using it)?
    // This should cover ANY Update call in the system (service/controller/etc)!!
    @Pointcut(value="execution(* net.guides.springboot2.springboot2jpacrudexample.*.*.*update*(..))")
    public void updateCall() { }

    // In theory can wildcard the lot ...
    @Pointcut(value="execution(* *update*(..))")
    public void anyUpdate() { }


    // Log controller calls using up front pointcut?
    @Before("anyConMethod()")
    public void logBeforeCON(JoinPoint joinPoint) {
		LogUtil.doLog("Enter(logB4_CON)",joinPoint);
    }

    // Can we avoid having separate aspects for the various bits. Bundle stuff together
    @Before("anyConMethod() || anySvcMethod()")
    public void logBeforeALL(JoinPoint joinPoint) {
        LogUtil.doLog("Enter(logB4_ALL)",joinPoint);
    }


    // Apply the wildcard stuff to restrict to MATCH and assess
    @Before("updateCall()")
    public void logBeforeUpdate(JoinPoint joinPoint) {
        LogUtil.doLog("Enter(logB4_UPT)",joinPoint);
    }

    // Apply the wildcard stuff to EXCLUDE on MATCH and assess
    @Before("anySvcMethod() && !updateCall()")
    public void logBeforeNotUpdate(JoinPoint joinPoint) {
		LogUtil.doLog("Enter(logB4_NOT)",joinPoint);
    }

    // Apply the wildcard stuff to EXCLUDE on MATCH and assess part II
    @Before(" (anyConMethod() || anySvcMethod())  && !updateCall()")
    public void logBeforeNoUpdate(JoinPoint joinPoint) {
        LogUtil.doLog("Enter(logB4_NOU)",joinPoint);
    }


    // Pointcut as a function of OTHER pointcuts
    @Pointcut(" (anyConMethod() || anySvcMethod())  && !updateCall()")
    public void logBeforeNoUpdatePC(){
    }

    // Apply the wildcard stuff to EXCLUDE on MATCH and assess part II
    @Before("logBeforeNoUpdatePC()")
    public void logBeforeNoUpdatePC(JoinPoint joinPoint) {
        LogUtil.doLog("Enter(logB4_NUP)",joinPoint);
    }


    // Attempting a pointcut on ANYTHING that includes an update is clearly too wide a net.  Expect exceptions :(
//    // Apply the wildcard stuff to restrict to MATCH and assess
//    @Before("anyUpdate()")
//    public void logBeforeAnyUpdate(JoinPoint joinPoint) {
//        LogUtil.doLog("Enter(logB4_xxx)",joinPoint);
//    }



}
