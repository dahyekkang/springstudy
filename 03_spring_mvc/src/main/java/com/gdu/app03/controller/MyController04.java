package com.gdu.app03.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@SessionAttributes("title")   // Model에 title이 저장되면 HttpSession에도 같은 값을 저장한다.
@Controller
public class MyController04 {
  
  /******************************** 세션 정보 저장하기 ***********************************/
  
  /*
   * 1. HttpServletRequest로부터 HttpSession 얻기
   */
  // @GetMapping("/article/add.do")
  public String add(HttpServletRequest request) {
    
    // ViewResolver의 prefix : "/WEB-INF/views/"
    // ViewResolver의 suffix : ".jsp"
    
    HttpSession session = request.getSession();
    session.setAttribute("title", request.getParameter("title"));
    
    return "article/result";  // /WEBINF/views/article/result.jsp
  }
  
  
  /*
   * 2. HttpSession 선언하기 (스프링에서 지원하는 방법)
   */
  // @GetMapping("/article/add.do")
  public String add2(HttpSession session, HttpServletRequest request) {
    session.setAttribute("title", request.getParameter("title"));
    return "article/result";
  }
  
  
  /*
   * 3. @SessionAttributes
   *  1) 클래스 레벨의 annotation이다. (Controller처럼 클래스에 붙이는 annotation)
   *  2) Model에 값을 저장하면 HttpSession에 함께 저장된다. (session.setAttribute 없이)
   */
  
  @GetMapping("/article/add.do")
  public String add3(HttpServletRequest request, Model model) {
    model.addAttribute("title", request.getParameter("title")); // @Controller만 있을 때, 모델로 저장시키는 건 request에 저장하는 거여서 result.jsp의 ${title}은 출력이 된다. ${sessionScope.title}은 출력되지 않음
    return "article/result";
  }
  
  
  
  
  /******************************** 세션 정보 삭제하기 ***********************************/
  
  /*
   * 1. HttpSession의 invalidate() 메소드
   */
  
  // @GetMapping("/article/main.do")
  public String main(HttpSession session) {
    
    // session 정보 초기화
    session.invalidate();     // 골라서 지우는 removeAttribute도 가능하다.
    
    return "index";
    
  }
  
  
  /*
   * 2. SessionStatus의 setComplete() 메소드 ★
   */
  
  @GetMapping("/article/main.do")
  public String main2(SessionStatus sessionStatus) {
    
    // Session에 저장된 session attribute의 삭제
    sessionStatus.setComplete();  // sessionStatus를 complete해준다.
    
    return "index";   // 삭제 된 후에 index로 돌아가서 첫 화면 보여주기
  }

  
  /******************************** 세션 정보 확인하기 ***********************************/
  
  /*
   * 1. HttpSession의 getAttribute() 메소드
   */
  // @GetMapping("/article/confirm.do")
  public String comfirm(HttpSession session) {
    
    String title = (String)session.getAttribute("title");
    System.out.println(title);
    
    return "index";
  }
  
  
  /*
   * 2. @SessionAttribute
   */
  @GetMapping("/article/confirm.do")
  public String comfirm2(@SessionAttribute("title") String title) {   // session에 저장된 "title" 속성을 String title에 저장한다.
    System.out.println(title);
    return "index";
  }
    
}
