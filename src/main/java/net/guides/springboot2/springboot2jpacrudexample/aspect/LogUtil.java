package net.guides.springboot2.springboot2jpacrudexample.aspect;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

// Want the basic log mechanism (doLog) available across the various aspects
//@Component
public class LogUtil {

//    private final static Logger log = LoggerFactory.getLogger(this.getClass());
    private final static Logger log = LoggerFactory.getLogger("net.guides.springboot2.springboot2jpacrudexample.aspect.logUtil");

    // Might be sensible to try and consolidate some of the calls in one go
    public static void doLog(String type, JoinPoint joinPoint) {
        // Kick it on, but with null result TODO : or do we need a specialist "don't even go there" object ??
        doLog(type, joinPoint, null);
    }

    // type supposed to allow distinction between before/after/etc.  TODO : use an enumerator ??
    public static void doLog(String type, JoinPoint joinPoint, Object result) {

        // best way to get (possible) result displayed?
        // TODO : Might be that null is a valid result, so need to revisit?? if(result!=DontTry) { ....
        String logTmplt = "{}: {}.{}() with argument[s] = {}";
        if(result!=null) logTmplt += " result = {}";

        log.debug(logTmplt, type,
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()),
                result);
    }



}
