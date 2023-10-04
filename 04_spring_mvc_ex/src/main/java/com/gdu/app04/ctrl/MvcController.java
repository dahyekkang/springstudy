package com.gdu.app04.ctrl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.app04.vo.ArticleVo;

@Controller
public class MvcController {

  // DispatcherServlet(servlet-context.xml)에서 ViewResolver를 제거했으므로 JSP의 전체 경로를 모두 작성해야 한다.
  
  @RequestMapping(value="/", method=RequestMethod.GET)    // value -> contextPath 경로를 의미한다!
  public String main() {
    return "/WEB-INF/main.jsp";
  }
  
  @RequestMapping(value="/write.do", method=RequestMethod.GET)
  public String write() {
    return "/WEB-INF/article/write.jsp";
  }
  
  // 요청 파라미터 처리 방법 3가지!
  
  // 1. HttpServletRequest 이용
  // @RequestMapping(value="/register.do", method=RequestMethod.POST)
  public String register(HttpServletRequest request, Model model) {   // model : forwarding data 전달용
    int articleNo = Integer.parseInt(request.getParameter("articleNo"));
    String title = request.getParameter("title");
    String content = request.getParameter("content");
    model.addAttribute("articleNo", articleNo);
    model.addAttribute("title", title);
    model.addAttribute("content", content);
    
    return "/WEB-INF/article/result.jsp";
  }
  
  // 2. @RequestParam 이용
  // 장점 : @RequestParam(value="articleNo") 부분 생략 가능하다!
  // @RequestMapping(value="/register.do", method=RequestMethod.POST)
  public String register2(@RequestParam(value="articleNo") int articleNo
                        , @RequestParam(value="title") String title
                        , @RequestParam(value="content") String content
                        , Model model) {
    ArticleVo vo = new ArticleVo(articleNo, title, content);
    model.addAttribute("vo", vo);
    return "/WEB-INF/article/result.jsp";
  }
  
  // 3. 객체 이용
  //요청을 모두 command 객체로 만든다. 파라미터를 요청할 수 있는 필드를 가진 ArticleVo 객체를 이용하면 getter, setter를 알아서 가져와서 알아서 만든다.
  // @RequestMapping(value="/register.do", method=RequestMethod.POST)
  public String register3(ArticleVo vo) {   // Model model없이 이 자체로 command 객체는 model에 ArticleVo가 articleVo로 변경되어 저장된다.
    return "/WEB-INF/article/result.jsp";
  }
  
  @RequestMapping(value="/register.do", method=RequestMethod.POST)
  public String register4(@ModelAttribute(value="atcVo") ArticleVo vo) {   // 저장은 원래 model없이 자동으로 되는거고, 모델 이름을 arcVo로 설정! (2번째 방법처럼 직접 이름을 결정)
    return "/WEB-INF/article/result.jsp";
  }
  
}
