package com.gdu.app10.aop;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class AroundAop {

  // Around로 쓸 때에는 이전과 이후에 모두 호출해야 하는 경우에만 사용해라.
  
  // 포인트컷 : 언제 동작하는가?
  @Pointcut("execution(* com.gdu.app10.controller.*Controller.*(..))")
  public void setPointCut() { }
  
  // 어드바이스 : 무슨 동작을 하는가?
  @Around("setPointCut()")
  public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    
    /*
     * Around 어드바이스
     * 1. 반환타입 : Object
     * 2. 메소드명 : 마음대로
     * 3. 매개변수 : ProceedingJoinPoint
     */
    
    
    log.info("=========================================================================");  // 포인트컷 실행 이전에 실행(@Before 이전에 동작)
    
    Object obj = proceedingJoinPoint.proceed();                                             // 포인트컷이 실행되는 시점(오류 처리 필요)
    
    log.info("{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));     // 포인트컷 실행 이후에 실행(@After 이전에 동작)
    
    return obj;
    
    // around - before - 포인트컷 - around - after
    
  }
  
}
