package com.gdu.myhome.service;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.gdu.myhome.dao.UserMapper;
import com.gdu.myhome.dto.UserDto;
import com.gdu.myhome.util.MySecurityUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;
  private final MySecurityUtils mySecurityUtils;      // 암호화할 때 필요한 클래스
  
  @Override
  public void login(HttpServletRequest request, HttpServletResponse response) {   // 직접 응답하겠다. 컨트롤러 응답 X 서비스가 직접 응답. 주로 응답이 여러가지일 때 사용!
    
    String email = request.getParameter("email");
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    
    Map<String, Object> map = Map.of("email", email, "pw", pw);
    
    UserDto user = userMapper.getUser(map);
    
    if(user != null) {
      
      request.getSession().setAttribute("user", user);
      userMapper.insertAccess(email);
      // insert, update, delete 이후엔 redirect! 반환타입이 없어서 컨트롤러를 통한 이동이 아님.
      try {
        response.sendRedirect(request.getParameter("referer"));  // try-catch가 필요한 구문   // redirect
      } catch(Exception e) {
        e.printStackTrace();
      }
    } else {
      try {
        response.setContentType("text/html; charset=UTF-8");    // 그런 이메일 없다 -> alert창 띄우려면 script할 것이다.
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("alert('일치하는 회원 정보가 없습니다.')");
        // out.println("history.back()");    // 다시 로그인 페이지로 돌아간다. 안 쓰는 게 좋다. 그냥 어디로 갈 지를 정해주는 게 나음
        out.println("location.href='" + request.getContextPath() + "/main.do'");
        out.println("</script>");
        out.flush();
        out.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    
  }
  
  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    
    HttpSession session = request.getSession();
    
    session.invalidate();   // session 초기화 (session 비우는 것)
    
    try {
      response.sendRedirect(request.getContextPath() + "/main.do");  // try-catch가 필요한 구문   // redirect
    } catch(Exception e) {
      e.printStackTrace();
    }
    
  }
  
}
