package com.gdu.app03.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MyController05 {
  
  /*
   * redirect 하는 방법
   * 1. return "redirect:이동경로";
   *    public String add(){
   *      return "redirect:/list.do";
   *    }
   *    
   * 2. location.href='이동경로';
   *    public void add(HttpServletResponse response) {
   *      PrintWriter out = response.getWriter();
   *      out.println("<script>");
   *      out.println("location.href='이동경로'");
   *      out.println("</script>");
   *    }
   *    
   * 스크립트 태그를 굳이 한다는 건 주로 alert창을 띄워준 후 어디론가 이동하고 싶을 때 사용한다.
   */
  
  /*
   * redirect 이동경로
   * 1. 반드시 URLMapping 값을 작성한다.
   * 2. 이동할 JSP 경로를 작성할 수 없다.
   */

  // 삽입을 할 때 title이 비어있는지에 대한 결과를 목록으로 넘길 것이다.
  // @RequestMapping(value="/faq/add.do", method=RequestMethod.POST)
  public String add(HttpServletRequest request) {     // model은 사용할 수 없다! redirect는 파라미터를 자동으로 전달할 수 없기 때문!
    
    // 요청 파라미터
    String title = request.getParameter("title");
    
    // title이 빈 문자열이면 add 실패로 가정(DB 처리할 때 insert 성공은 1, 실패는 0이다.)
    int addResult = title.isEmpty() ? 0 : 1;
    
    // addResult를 가지고 faq 목록보기로 이동
    
    return "redirect:/faq/list.do?addResult=" + addResult;    // 파라미터를 직접 넘겨준다.
  }
  
  // @RequestMapping(value="/faq/list.do", method=RequestMethod.GET)
  public String list(@RequestParam(value="addResult", required=false) String addResult, Model model) {  // int addResult로 하면 addResult파라미터 자체가 없을 때, @RequestParam내부에서 자체적으로 작동하는 Integer.parseInt 작동 시에 숫자로 바꿀 파라미터가 아예 없어서 error가 발생하므로 int가 아닌 String으로 처리한다!
    
    model.addAttribute("addResult", addResult);
    
    // ViewResolver prefix :   /WEB-INF/views/
    // ViewResolver suffix :   .jsp
    return "faq/list";  //     /WEB-INF/views/faq/list.jsp
  }
  
  @RequestMapping(value="/faq/add.do", method=RequestMethod.POST)
  public String add2(HttpServletRequest request
                   , RedirectAttributes redirectAttributes) {     // redirect 상황에서 값을 전달할 때 사용한다. spring3버전 이후에서 사용 가능!
    
    // 요청 파라미터
    String title = request.getParameter("title");
    
    // title이 빈 문자열이면 add 실패
    int addResult = title.isEmpty() ? 0 : 1;
    
    // faq 목록보기로 redirect 할 때 addResult를 "flash attribute"로 곧바로 전달하기
    redirectAttributes.addFlashAttribute("addResult", addResult);
    
    // faq 목록보기로 redirect
    return "redirect:/faq/list.do";
  }
  
  @RequestMapping(value="/faq/list.do", method=RequestMethod.GET)
  public String list2() {
    return "faq/list";
  }

}
