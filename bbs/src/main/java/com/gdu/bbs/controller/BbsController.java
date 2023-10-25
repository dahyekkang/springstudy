package com.gdu.bbs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.bbs.service.BbsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor  // @Autowired를 사용해야 하는데 생성자는 @Autowired를 생략할 수 있다. 일반 생성자는 못 하고, final 필드용 생성자를 사용하는 해당 annotation 사용
@Controller
public class BbsController {

  // Controller가 사용하는 건 언제나 Service다.
  // 인터페이스가 있을 땐 항상 인터페이스로 선언
  private final BbsService bbsService;
  
  @GetMapping("/bbs/list.do")
  public String list(HttpServletRequest request, Model model) {  // ajax 처리가 아닌 이상 controller에서 String을 반환하면 jsp반환한다는 뜻
    bbsService.loadBbsList(request, model);   // 필요한 거 다 전달함! Controller는 더 할 게 없음. 어디로 갈지만 정해주면 됨
    return "bbs/list";
  }
}
