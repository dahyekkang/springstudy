package com.gdu.myapp2.aop;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j        // 로그
@Aspect       // AOP 사용할 것
@Component    // Bean 대신 사용
public class LogAop {
  
  // 포인트컷 : 어떤 메소드에서 동작하는지 표현식으로 작성
  @Pointcut("execution(* com.gdu.myapp2.controller.*Controller.*(..))")
  public void setPointCut() { }   // 포인트컷에 등록을 위한 메소드여서 이름만 있으면 된다.(파라미터X, 반환X, 이름은 마음대로, 내용도 필요없다.)
  
  // 어드바이스 : 포인트컷에서 실제로 동작할 내용
  @Before("setPointCut()")
  public void doLog(JoinPoint joinPoint) {     // JoinPoint : 어드바이스로 전달되는 메소드
    
    // 1. HttpServletRequest
    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = servletRequestAttributes.getRequest();
    
    // 2. 요청 파라미터 -> Map 변환
    Map<String, String[]> map = request.getParameterMap();
    
    // 3. 요청 파라미터 출력 형태 만들기
    String params = "";
    if(map.isEmpty()) {
      params += "No Parameter";
    } else {
      for(Map.Entry<String, String[]> entry : map.entrySet()) {
        params += entry.getKey() + ":" + Arrays.toString(entry.getValue()) + " ";
      }
    }
    
    // 4. 로그 찍기 (치환 문자 {} 활용)
    log.info("{} {}", request.getMethod(), request.getRequestURI());    // 요청 방식, 요청 주소
    log.info("{}", params);                                             // 요청 파라미터
    
  }
  
}
