package com.gdu.myhome.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.myhome.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping(value="/user")
@RequiredArgsConstructor
@Controller
public class UserController {

  private final UserService userService;
  
  @GetMapping(value="/login.form")
  public String loginForm() {
    return "user/login";
  }
  
  @PostMapping(value="/login.do")
  public void login(HttpServletRequest request, HttpServletResponse resonse) {
    userService.login(request, resonse);
  }
  
  @GetMapping(value="/logout.do")   // a링크로 로그아웃 했음
  public void logout(HttpServletRequest request, HttpServletResponse resonse) {
    userService.logout(request, resonse);
  }
  
  
  
  
}
