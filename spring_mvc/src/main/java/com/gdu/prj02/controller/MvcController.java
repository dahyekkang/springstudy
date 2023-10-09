package com.gdu.prj02.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller   // 해당 클래스를 controller로 사용합니다.
public class MvcController {
  
  // value="/"         : URL이 /ContextPath인 요청을 처리합니다.          http://localhost:8080/prj02/를 의미합니다.
  // value="/main.do"  : URL이 /ContextPath/main.do인 요청을 처리합니다.  http://localhost:8080/prj02/main.do를 의미합니다.
  @RequestMapping(value={"/", "/main.do"}, method=RequestMethod.GET)   // 요청(주소, 방식)에 따라서 동작할 메소드를 결정합니다.
  public String index() {
    // return의 기본 동작은 어떤 JSP로 forward(전달, 이동)할 것인지 작성합니다.
    return "index";
  }

}
