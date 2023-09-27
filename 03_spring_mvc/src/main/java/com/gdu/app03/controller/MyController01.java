package com.gdu.app03.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller   // 컨트롤러를 만드는 Annotation   // 얘를 controller로 만들고 싶으면 일케. 얘가 servlet이 된다! HttpServletRequest or HttpServletResponse가 된다!
public class MyController01 {

  // doGet이나 doPost가 정의되어 있지 않다. 그냥 원하는 메소드를 만들면 된다.
  
  /*
   * @Controller
   * 
   * 요청이나 응답을 처리할 메소드를 만든다.
   * 
   * 1. 반환타입
   *  1) String : 응답할 Jsp의 이름을 반환한다.
   *  2) void   : 컨트롤러가 호출한 서비스에서 직접 응답한다(반환 없이). 요청 주소를 Jsp 이름으로 인식한다.(주소 자체를 jsp로 보겠다!)
   *  3) 기타   : 비동기 통신(ajax(jsp를 안 바꾸고! 화면은 그대로 고정시켜놓고!))에서 데이터를 응답한다. (Map, 객체, List 등)
   *  
   * 2. 메소드명
   *  - 아무 일도 안 한다. 이름이 없을 순 없으니 주기는 하지만 아무 일도 안 함.
   *  
   * 3. 매개변수
   *  1) (필요에 따라) HttpServletRequest를 선언해서 사용할 수 있다.
   *  2) (필요에 따라) HttpServletResponse를 선언해서 사용할 수 있다.
   *  3) Model을 선언해서 forward할 정보를 저장할 수 있다.
   *  4) HttpSession을 선언해서 사용할 수 있다.
   *  
   *  4. 요청(@RequestMapping : 어떤 주소로 어떤 요청이 왔을 때 이 메소드가 동작한다!)
   *    1) 메소드 : GET, POST
   *    2) URL    : 요청 주소
   *    
   *  // Spring의 기본은 forward이며, 모두 forward 한 거다!!!
   */
  
  // value="/"         : contextPath 요청을 의미한다. http://localhost/app03/ 주소를 의미한다.
  // value="/main.do"  : /main.do 요청을 의미한다.    http://localhost/app03/main.do 주소를 의미한다.
  @RequestMapping(value={"/", "/main.do"}, method=RequestMethod.GET)    // GET방식의 contextPath 요청이 있을 때, /WEB-INF/views/index.jsp를 열어라
  public String welcome() {
    // ViewResolver의 prefix : /WEB-INF/views/
    // ViewResolver의 suffix : .jsp
    return "index";   //       /WEB-INF/views/index.jsp
  }
  
  @RequestMapping(value="/board/list.do", method=RequestMethod.GET)   // value는 return 경로의 jsp를 열어주는 경로이다.(정확히 얘기하면 forward하는 것이다. request에 저장된 정보를 가지고 이동한다!)
  public String boardList() {
    // ViewResolver의 prefix : /WEB-INF/views/
    // ViewResolver의 suffix : .jsp
    return "board/list";  //   /WEB-INF/views/board/list.jsp
  }
  
  
}
