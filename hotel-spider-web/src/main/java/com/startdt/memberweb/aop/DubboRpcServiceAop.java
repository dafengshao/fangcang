package com.startdt.memberweb.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**   
* @Description: TODO(用一句话描述该类做什么) 
* @author hewenfeng   
* @date 2017年11月28日 下午5:30:53 
* @version V1.0   
*/
@Component
@Aspect
public class DubboRpcServiceAop {
	//@Around("execution(* com.startdt.memberweb.dubbo..*.*(..))")  
	public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint){  
	    System.out.println("环绕通知的目标方法名："+proceedingJoinPoint.getSignature().getName());  
	    try {  
	        Object obj = proceedingJoinPoint.proceed();  
	        return obj;  
	    } catch (Throwable throwable) {  
	        throwable.printStackTrace();  
	    }  
	    return null;  
	} 
}
