package com.gdu.app03.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.app03.dto.BlogDto;


// 모든 메소드의 RequestMapping에 중복되는 부분이 있다면 클래스 레벨로 RequestMapping을 뺄 수 있다!
@RequestMapping("/blog")  // "/blog"로 시작하는 요청을 처리하는 컨트롤러가 된다.
                          // 모든 메소드의 요청에 "/blog"가 자동으로 삽입된다.
@Controller
public class MyController03 {
  
  /*
   * 1. HttpServletRequest request를 이용한 요청 파라미터 처리
   *  1) Java EE 표준 방식이다.
   *  2) 파라미터뿐만 아니라 HttpSession session, String contextPath 와 같은 정보도 꺼낼 수 있으므로 여전히 강력한 도구이다.
   */
  
  

  // @RequestMapping("/detail.do")  // GET 방식의 method는 생략할 수 있다.(디폴트) value만 작성할 땐 value= 부분도 생략할 수 있다.
  public String blogDetail(HttpServletRequest request, Model model) {    // 이 방법(HttpServletRequest)을 사용하면 parameter뿐만 아니라 session, contextPath 등 많은 것을 얻어낼 수 있다. // forwarding할 정보를 model에 저장한다.
    // ViewResolver의 prefix : /WEB-INF/views/
    // ViewResolver의 suffix : .jsp
    String blogNo = request.getParameter("blogNo");
    model.addAttribute("blogNo", blogNo);   // model을 이용하여 forward
    return "blog/detail"; //  /WEB-INF/views/blog/detail.jsp로 forward한다. (스프링에서 forwarding한다는 말이 없으면 forward 한 것이다.(forward가 기본 방식) redirect 하고싶을 때만 적어주는 코드가 따로 존재한다.)
    // forward는 request를 전달한다. 따라서 request에 정보 저장 후 전달을 해야한다.
    // forward할 때 정보를 저장하는 방법은 request에 setAttribute 하는 것
    // 그런데 request가 아닌 model에 저장할 것임! (request.setAttribute 대신 model.addAttribute)
  }
  
  
  /*
   * 2. @RequestParam을 이용한 요청 파라미터 처리
   *  1) 파라미터의 개수가 적은 경우에 유용하다.
   *  2) 주요 메소드
   *    (1) value        : 요청 파라미터의 이름
   *    (2) required     : 요청 파라미터의 필수 여부(디폴트 true - 요청 파라미터가 없으면 오류 발생)
   *    (3) defaultValue : 요청 파라미터가 없는 경우에 사용할 값
   *  3) @RequestParam을 생략할 수 있다.
   */
  
  /*
   * // @RequestParam 생략
    @RequestMapping("/detail.do")
    public String blogDetail2(int blogNo, Model model) {   // 요청에서 blogNo 파라미터를 가져와서 int형으로 변환(Integer.parseInt)
    model.addAttribute("blogNo", blogNo); // "blogNo"라는 이름으로 blogNo값을 저장
    return "blog/detail";   // 다음 경로로 가시오.
  }
  
  */
  
  // @RequestMapping("/detail.do")
  public String blogDetail2(@RequestParam(value="blogNo", required=false, defaultValue="1") int blogNo, Model model) {   // 요청에서 blogNo 파라미터를 가져와서 int형으로 변환(Integer.parseInt)
    model.addAttribute("blogNo", blogNo); // "blogNo"라는 이름으로 blogNo값을 저장
    return "blog/detail";   // 다음 경로로 가시오.
  }
  
  
  /*
   * 3. 커맨드 객체를 이용한 요청 파라미터 처리
   *  1) 요청 파라미터를 필드로 가지고 있는 객체를 커맨드 객체라고 한다.(BlogDto)
   *  2) 요청 파라미터를 필드에 저장할 때 Setter가 사용된다.
   *  3) 요청 파라미터가 많은 경우에 유용하다.
   *  4) 커맨드 객체는 자동으로 Model에 저장된다. 저장될 때 객체명(dto)이 아닌 클래스명(BlogDto)으로 저장된다.(클래스명을 LowerCamelCase로 바꿔서 저장한다.(blogDto))
   */
  
  // @RequestMapping("/detail.do")
  public String blogDetail3(BlogDto dto) {   // Model에 저장된 이름은 dto가 아니라 blogDto 이다.  // BlogDto를 첫 글자 소문자로 바꿔서 blogDto로 저장한다.
    return "blog/detail";
  }
  
  // @ModelAttribute를 이용해서 Model에 저장되는 커맨드 객체의 이름을 지정할 수 있다.
  
  @RequestMapping("/detail.do")
  public String blogDetail4(@ModelAttribute("dto") BlogDto blogDto) {   // Model에 저장되는 이름은 dto이다. 이름을 바꾸고 싶을 때, ModelAttribute를 사용하면 된다.
    return "blog/detail";
  }
  
}
