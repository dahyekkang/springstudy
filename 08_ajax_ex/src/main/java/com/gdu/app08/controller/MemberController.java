package com.gdu.app08.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.app08.service.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class MemberController {

  private final MemberService memberService;
  
  @ResponseBody   // 반환하는 데이터가 jsp가 아닐 때 사용
  @GetMapping(value="/member/health.check", produces="application/json; charset=UTF-8")
  public Map<String, Object> bmiInfo(@RequestParam("memberNo") int memberNo){
    return memberService.getBmiInfo(memberNo);
  }
  
  @ResponseBody
  @GetMapping(value="/member/profile.display", produces="application/octet-stream")
  public byte[] profile(@RequestParam("memberNo") int memberNo) {
    return memberService.getProfileImage(memberNo);
  }
  
}
