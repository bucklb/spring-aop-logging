package net.guides.springboot2.springboot2jpacrudexample.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution.
 * 
 * @author Ramesh Fadatare
 *
 */

// TODO : places where we might want to redact data (as it's personally identifiable) before logging
// TODO : can we exclude stuff with the pointcut (so don't log "checkPassword" call and similar)



@Aspect
@Component
public class LoggingAspect {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	// Might be sensible to try and consolidate some of the calls in one go
	private void doLog(String type, JoinPoint joinPoint) {
		// Kick it on, but with null result TODO : or do we need a specialist "don't even go there" object ??
		doLog(type, joinPoint, null);
	}

	// type supposed to allow distinction between before/after/etc.  TODO : use an enumerator ??
	private void doLog(String type, JoinPoint joinPoint, Object result) {

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



	/**
	 * Run before the method execution.
	 * @param joinPoint
	 */
	@Before("execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.addEmployee(..))")
	public void logBefore(JoinPoint joinPoint) {
//		doLog("Enter(logBefore)",joinPoint);
	}

	/**
	 * Run after the method returned a result.
	 * @param joinPoint
	 */
	@After("execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.addEmployee(..))")
	public void logAfter(JoinPoint joinPoint) {
//		doLog("Leave(logAfter) ",joinPoint);
	}

	/**
	 * Run after the method returned a result, intercept the returned result as well.
	 * @param joinPoint
	 * @param result
	 */
	@AfterReturning(pointcut = "execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.deleteEmployee(..))",
			returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
//		doLog("Leave(logReturn)",joinPoint, result);
	}

	// See if can get multiple aspects to fire (so before & afterReturning)
	@AfterReturning(pointcut = "execution(* net.guides.springboot2.springboot2jpacrudexample.service.EmployeeService.getEmployeeById(..))",
			returning = "result")
	public void logAfterReturninggET(JoinPoint joinPoint, Object result) {
		doLog("Leave(logReturn)",joinPoint, result);
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
//			doLog("Enter(logAround)",joinPoint);
		}
		try {
			Object result = joinPoint.proceed();
			if (log.isDebugEnabled()) {
//				doLog("Leave(logAround)",joinPoint,result);
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
//		doLog("Enter(logB4_EMP)",joinPoint);
	}

	// Can we get every service logged?
// fails->	@Before("execution(* net.guides.springboot2.springboot2jpacrudexample.service.*(..))")
	@Before("execution(* net.guides.springboot2.springboot2jpacrudexample.service.*.*(..))")
	public void logBeforeSVC(JoinPoint joinPoint) {
//		doLog("Enter(logB4_SVC)",joinPoint);
	}

	// Declare pointcuts up front & see how they combine
	@Pointcut(value="execution(* net.guides.springboot2.springboot2jpacrudexample.service.*.*(..))")
	public void anySvcMethod() { }

	@Pointcut(value="execution(* net.guides.springboot2.springboot2jpacrudexample.controller.*.*(..))")
	public void anyConMethod() { }




	// Log controller calls using up front pointcut?
	@Before("anyConMethod()")
	public void logBeforeCON(JoinPoint joinPoint) {
//		doLog("Enter(logB4_CON)",joinPoint);
	}

	// Can we avoid having separate aspects for the various bits. Bundle stuff together
	@Before("anyConMethod() || anySvcMethod()")
	public void logBeforeALL(JoinPoint joinPoint) {
		doLog("Enter(logB4_ALL)",joinPoint);
	}

	// Can we use wildcards as part of names (anf force inclusion/exclusion using it)?
	// This should cover ANY Update call in the system (service/controller/etc)!!
	@Pointcut(value="execution(* net.guides.springboot2.springboot2jpacrudexample.*.*.*update*(..))")
	public void updateCall() { }

	// Apply the wildcard stuff to restrict to MATCH and assess
	@Before("updateCall()")
	public void logBeforeUpdate(JoinPoint joinPoint) {
		doLog("Enter(logB4_UPT)",joinPoint);
	}

	// Apply the wildcard stuff to EXCLUDE on MATCH and assess
	@Before("anySvcMethod() && !updateCall()")
	public void logBeforeNotUpdate(JoinPoint joinPoint) {
//		doLog("Enter(logB4_NOT)",joinPoint);
	}

	// Apply the wildcard stuff to EXCLUDE on MATCH and assess part II
	@Before(" (anyConMethod() || anySvcMethod())  && !updateCall()")
	public void logBeforeNoUpdate(JoinPoint joinPoint) {
		doLog("Enter(logB4_NOU)",joinPoint);
	}





}
