package com.gdu.myapp2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.myapp2.service.NoticeService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/notice")
@RequiredArgsConstructor    // private final NoticeService noticeService;에 @Autowired를 하기 위한 코드이다.
@Controller
public class NoticeController {

  private final NoticeService noticeService;
  
//  @GetMapping("/list.do")
//  public String list(Model model) {
//    List<NoticeDto> noticeList = noticeService.getNoticeList();
//    model.addAttribute("noticeList", noticeList);
//    return "notice/list";
//  }
  
}
