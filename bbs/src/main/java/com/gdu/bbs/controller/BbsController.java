package com.gdu.bbs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.bbs.dto.BbsDto;
import com.gdu.bbs.service.BbsService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor  // @Autowired를 사용해야 하는데 생성자는 @Autowired를 생략할 수 있다. 일반 생성자는 못 하고, final 필드용 생성자를 사용하는 해당 annotation 사용
@Controller
public class BbsController {

  // Controller가 사용하는 건 언제나 Service다.
  // 인터페이스가 있을 땐 항상 인터페이스로 선언
  private final BbsService bbsService;
  
  
  // select는 model에 저장한 뒤 forward 한다.
  @GetMapping("/list.do")   // model에 저장해서 forward
  public String list(HttpServletRequest request, Model model) {  // ajax 처리가 아닌 이상 controller에서 String을 반환하면 jsp반환한다는 뜻
    bbsService.loadBbsList(request, model);   // 필요한 거 다 전달함! Controller는 더 할 게 없음. 어디로 갈지만 정해주면 됨
    return "bbs/list";
  }
  
  /*
   * Model에 넣을 데이터가 2개 이상이면 service에서 값 저장
   * Model에 넣을 데이터가 1개만 있으면 controller에서 값 저장
   */
  
  
  // select는 model에 저장한 뒤 forward 한다.
  @GetMapping("/detail.do")   // model에 저장해서 forward
  public String detail(@RequestParam(value="bbsNo", required=false, defaultValue="0") int bbsNo   // RequestParam의 단점 : 오류 처리가 안 된다. 해결 방안 required=false(꼭 필요한 건 아니다.) defaultValue="0"(전달안됐으면 0으로)
                      , Model model) {    // select 결과를 저장할 때 model을 사용한다. (select -> model 필요)
    BbsDto bbs = bbsService.getBbs(bbsNo);
    model.addAttribute("bbs", bbs);
    return "bbs/detail";
  }
  
  /*
  // ModelAndView 는 이동할 곳(setViewName)과 저장할 값(addObject)를 한 번에 넣어서 return하는 클래스(옛날 모델임 이제 사용 잘 안 함)
  @GetMapping("/detail.do")
  public ModelAndView detail2(@RequestParam(value="bbsNo", required=false, defaultValue="0") int bbsNo   // RequestParam의 단점 : 오류 처리가 안 된다. 해결 방안 required=false(꼭 필요한 건 아니다.) defaultValue="0"(전달안됐으면 0으로)
                      , Model model) {    // select 결과를 저장할 때 model을 사용한다. (select -> model 필요)
    BbsDto bbs = bbsService.getBbs(bbsNo);
    ModelAndView mav = new ModelAndView();
    mav.setViewName("bbs/detail");
    mav.addObject("bbs", bbs);
    return mav;
  }
*/ 
  
  // insert는 redirectAttributes 에 저장한 뒤 redirect 한다.
  @PostMapping("/add.do")   // add 후에는 redirect 한다!
  public String add(BbsDto bbs, RedirectAttributes attr) {
    int addResult = bbsService.addBbs(bbs);
    attr.addFlashAttribute("addResult", addResult);
    return "redirect:/list.do";   // redirect 뒤에는 mapping 적기
  }
  
  // update는 redirectAttributes 에 저장한 뒤 redirect 한다.
  @PostMapping("/modify.do")
  public String modify(BbsDto bbs, RedirectAttributes attr) {
    int modifyResult = bbsService.modifyBbs(bbs);
    attr.addFlashAttribute("modifyResult", modifyResult);
    return "redirect:/detail.do?bbsNo=" + bbs.getBbsNo();
  }
  
  // delete는 redirectAttributes 에 저장한 뒤 redirect 한다.
  @PostMapping("/remove.do")
  public String remove(@RequestParam(value="bbsNo", required=false, defaultValue="0") int bbsNo
                     , RedirectAttributes attr) {
    int removeResult = bbsService.removeBbs(bbsNo);
    attr.addFlashAttribute("removeResult", removeResult);
    return "redirect:/list.do";
  }
}
