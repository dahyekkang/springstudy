package com.gdu.myhome.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.myhome.dto.UserDto;
import com.gdu.myhome.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping(value="/user")
@RequiredArgsConstructor
@Controller
public class UserController {
  
  private final UserService userService;
  
  @GetMapping("/login.form")
  public String loginForm(HttpServletRequest request, Model model) throws Exception {
    // referer : 이전 주소가 저장되는 요청 Header 값
    String referer = request.getHeader("referer");
    model.addAttribute("referer", referer == null ? request.getContextPath() + "/main.do" : referer);
    // 네이버로그인-1
    model.addAttribute("naverLoginURL", userService.getNaverLoginURL(request));
    return "user/login"; 
  }
  
  @GetMapping("/naver/getAccessToken.do")   // 진짜 로그인하는 게 아니고, accessToken 받는 메소드
  public String getAccessToken(HttpServletRequest request) throws Exception {
    
    // 네이버로그인-2
    String accessToken = userService.getNaverLoginAccessToken(request);
    return "redirect:/user/naver/getProfile.do?accessToken=" + accessToken;
    
  }
  
  @GetMapping("/naver/getProfile.do")   // accessToken 받아야 한다! (위 메소드에서 가져옴)
  public String getProfile(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {   // 사용자 프로필 정보를 UserDto로 넘긴다!
  
    // 네이버로그인-3
    UserDto naverProfile = userService.getNaverProfile(request.getParameter("accessToken"));
    // System.out.println(naverProfile);      // 요청한 사람의 프로필 정보가 들어있음
    
    // 네이버 로그인 후속 작업(처음 시도 : 간편가입, 이미 가입 : 로그인)(email을 가진 유저가 DB에 있는지 확인해야한다.)
    UserDto user = userService.getUser(naverProfile.getEmail());
    if(user == null) {
      // 네이버 간편가입 페이지로 이동 (정보가 입력된 화면으로 이동)
      model.addAttribute("naverProfile", naverProfile);     // 네이버 프로필을 저장시켜놓고 가입 화면으로 forward 할 것이다.
      return "user/naver_join";   // jsp에서 el로 naverProfile을 받을 수 있다.
    } else {
      // naverProfile로 로그인 처리하기
      userService.naverLogin(request, response, naverProfile);
      return "redirect:/main.do";
    }
    
  }
  
  
  @PostMapping("/naver/join.do")
  public void naverJoin(HttpServletRequest request, HttpServletResponse response) {
    userService.naverJoin(request, response);
  }
  
  @PostMapping("/login.do")
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {   // 반환 타입이 void : 서비스 안에서 직접 이동한다.(redirect)
    userService.login(request, response);
  }
  
  @GetMapping("/logout.do")
  public void logout(HttpServletRequest request, HttpServletResponse response) {   // 반환 타입이 void : 서비스 안에서 직접 이동한다.(redirect)
    userService.logout(request, response);
  }
  
  @GetMapping("/agree.form")
  public String agreeForm() {
    return "user/agree";
  }
  
  @GetMapping("/join.form")
  public String joinForm(@RequestParam(value="service", required=false, defaultValue="off") String service
                       , @RequestParam(value="event", required=false, defaultValue="off") String event
                       , Model model) {
    String rtn = null;
    if(service.equals("off")) {
      rtn = "redirect:/main.do";
    } else {
      model.addAttribute("event", event);  // user 폴더 join.jsp로 전달하는 event는 "on" 또는 "off" 값을 가진다.
      rtn = "user/join";
    }
    return rtn;
  }
  
  
  @GetMapping(value="/checkEmail.do", produces=MediaType.APPLICATION_JSON_VALUE)  // "application/json"이랑 같다!
  public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
    return userService.checkEmail(email);
  }
  
  @GetMapping(value="/sendCode.do", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> sendCode(@RequestParam String email) {
    return userService.sendCode(email);
  }
  
  
  @PostMapping(value="/join.do")
  public void join(HttpServletRequest request, HttpServletResponse response) {    // 반환 타입이 void : 서비스 안에서 직접 이동한다.(location.href)
    userService.join(request, response);
  }
  
  @GetMapping("/mypage.form")
  public String mypageForm() {
    return "user/mypage";
  }
  
  @PostMapping(value="/modify.do", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> modify(HttpServletRequest request) {
    return userService.modify(request);
  }
  
  @GetMapping("/modifyPw.form")
  public String modifyPwForm() {
    return "user/pw";
  }
  
  @PostMapping("/modifyPw.do")
  public void modifyPw(HttpServletRequest request, HttpServletResponse response) {
    userService.modifyPw(request, response);
  }
  
  @PostMapping("/leave.do")
  public void leave(HttpServletRequest request, HttpServletResponse response) {
    userService.leave(request, response);
  }
  
  @GetMapping("/active.form")     // redirect 이동은 GetMapping
  public String activeForm() {
    return "user/active";
  }
  
  @GetMapping("/active.do")
  public void active(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
    userService.active(session, request, response);
  }

}