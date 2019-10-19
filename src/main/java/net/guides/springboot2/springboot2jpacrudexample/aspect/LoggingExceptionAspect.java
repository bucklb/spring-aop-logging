package net.guides.springboot2.springboot2jpacrudexample.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*
    Play with AOP and exceptions in relative isolation (and so we can isolate the playing simply)
 */

@Aspect
@Component
@Order(200)
public class LoggingExceptionAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());




    @AfterThrowing(pointcut = "execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.updateEmployee(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {

        System.out.println("logAfterThrowing");

        // TODO : Might want this as generic call if we want to catch exceptions in other places??  Pass error.getCause as "result" ??
        log.debug("logAfterThrowing running .....");
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), error.getCause() != null ? error.getCause() : "NULL");
    }

    // make it generic around get?
    @AfterThrowing(pointcut = "execution(* net.guides.springboot2.springboot2jpacrudexample.controller.EmployeeController.get*(..))", throwing = "error")
    public void logAfterThrowingGet(JoinPoint joinPoint, Throwable error) {

//        System.out.println("logAfterThrowingGet");

        // TODO : Might want this as generic call if we want to catch exceptions in other places??  Pass error.getCause as "result" ??
        log.debug("logAfterThrowing Get running .....");
        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), error.getCause() != null ? error.getCause() : "NULL");
    }




}
