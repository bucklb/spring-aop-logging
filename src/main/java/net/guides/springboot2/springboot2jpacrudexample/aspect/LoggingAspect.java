package net.guides.springboot2.springboot2jpacrudexample.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution.
 * 
 * @author Ramesh Fadatare
 *
 */

// TODO : places where we might want to redact data (as it's personally identifiable) before logging
// TODO : can we exclude stuff with the pointcut (so don't log "checkPassword" call and similar)

/*
    Might be easier to play with stuff if it's split in a meaningful way.
    Make it easier to toggle off playing with things by just commenting out @aspect

    Look at defining pointcuts with various matcing approaches and then play with combining them
    and get an idea how convoluted the combinations might get
 */

//@Aspect
@Component
@Order(-10)
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());



	/**
	 * Run before the method execution.
	 * @param joinPoint
	 */
	@Before("execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.addEmployee(..))")
	public void logBefore(JoinPoint joinPoint) {
		LogUtil.doLog("Enter(logBefore)",joinPoint);
	}

	/**
	 * Run after the method returned a result.
	 * @param joinPoint
	 */
	@After("execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.addEmployee(..))")
	public void logAfter(JoinPoint joinPoint) {
		LogUtil.doLog("Leave(logAfter) ",joinPoint);
	}

	/**
	 * Run after the method returned a result, intercept the returned result as well.
	 * @param joinPoint
	 * @param result
	 */
	@AfterReturning(pointcut = "execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.deleteEmployee(..))",
			returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		LogUtil.doLog("Leave(logReturn)",joinPoint, result);
	}

	// See if can get multiple aspects to fire (so before & afterReturning)
	@AfterReturning(pointcut = "execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.getEmployeeById(..))",
			returning = "result")
	public void logAfterReturninggET(JoinPoint joinPoint, Object result) {
		LogUtil.doLog("Leave(logReturn)",joinPoint, result);
	}


	/**
	 * Run around the method execution.
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.getEmployeeById(..))")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		if (log.isDebugEnabled()) {
			LogUtil.doLog("Enter(logAround)",joinPoint);
		}
		try {
			Object result = joinPoint.proceed();
			if (log.isDebugEnabled()) {
				LogUtil.doLog("Leave(logAround)",joinPoint,result);
			}
			return result;
		}
		catch (IllegalArgumentException e) {
			// TODO : prettify this??
			// If we have logged the entry (and parameters) what do we gain by doing more than just logging exception description?
			log.error("Illegal argument: {} in {}.{}()",
					Arrays.toString(joinPoint.getArgs()),
					joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName());
			throw e;
		}

	}

	/**
	 * Advice that logs methods throwing exceptions.
	 *
	 * @param joinPoint join point for advice
	 * @param e         exception
	 */

//	@AfterThrowing(pointcut = "execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.updateEmployee(..))", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {

		// TODO : Might want this as generic call if we want to catch exceptions in other places??  Pass error.getCause as "result" ??
		log.debug("logAfterThrowing running .....");
		log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), error.getCause() != null ? error.getCause() : "NULL");
	}

//	@AfterThrowing(pointcut = "execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.getEmployeeById(..))", throwing = "error")
	public void logAfterThrowingGet(JoinPoint joinPoint, Throwable error) {

		// TODO : Might want this as generic call if we want to catch exceptions in other places??  Pass error.getCause as "result" ??
		log.debug("logAfterThrowing Get running .....");
		log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), error.getCause() != null ? error.getCause() : "NULL");
	}


	// Can we get every service logged?
	@Before("execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.*(..))")
	public void logBeforeEMP(JoinPoint joinPoint) {
		LogUtil.doLog("Enter(logB4_EMP)",joinPoint);
	}

	// Can we get every service logged?
// fails->	@Before("execution(* net.guides.springboot2.springboot2jpacrudexample.service.*(..))")
	@Before("execution(* net.guides.springboot2.springboot2jpacrudexample.service.*.*(..))")
	public void logBeforeSVC(JoinPoint joinPoint) {
		LogUtil.doLog("Enter(logB4_SVC)",joinPoint);
	}


}
