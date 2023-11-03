package com.gdu.myhome.intercept;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * ShouldNotLoginInterceptor
 * 로그아웃 상태일 때 가능한 기능을 로그인 상태로 요청할 때 로그인 여부를 점검하는 인터셉터
 */


@Component    // Bean으로 등록하기 위해 객체로 만든다.
public class ShouldNotLoginInterceptor implements HandlerInterceptor {    // intercept를 만들 때 꼭 인터페이스 지정
  
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    
    HttpSession session = request.getSession();
    
    if(session != null && session.getAttribute("user") != null) {   // 로그인이 되어 있음
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      out.println("alert('해당 기능은 사용할 수 없습니다.')");
      out.println("location.href='" + request.getContextPath() + "/main.do'");
      out.println("</script>");
      out.flush();
      out.close();
      return false;   // 가로챈 컨트롤러 요청이 동작하지 않는다.
    }
    return true;     // 가로챈 컨트롤러 요청이 동작한다.
  }
}
