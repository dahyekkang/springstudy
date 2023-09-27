package com.gdu.app03.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController02 {

  // Spring 4 버전 이후 사용 가능한 @RequestMapping
  // 1. @GetMapping
  // 2. @PostMapping
  
  @GetMapping(value="/notice/list.do")
  public String noticeList() {
    // ViewResolver의 prefix : /WEB-INF/views/
    // ViewResolver의 suffix : .jsp
    return "notice/list";  //   /WEB-INF/views/notice/list.jsp      // 결과를 보여줄 jsp
  }
  
  // 반환이 없는 경우에는 요청 주소를 Jsp 경로로 인식한다.
  // /member/list.do 요청을 /member/list.jsp 경로로 인식한다.
  // /member/list    요청을 /member/list.jsp 경로로 인식한다.
  @GetMapping(value={"/member/list.do", "/member/list"})
  public void memberList() {
    // void일 때(반환타입이 없을 때)는 주소를 경로로 바꿔서 jsp 해석을 해준다. .do라고 되어있으면 .jsp라고 받아서 GetMapping의 list.do를 list.jsp로 해석한다!!!! 대똑똑함
  }
  
}
