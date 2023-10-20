package com.gdu.myhome.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

public interface UserService {
  public void login(HttpServletRequest request, HttpServletResponse response);
  public void logout(HttpServletRequest request, HttpServletResponse response);
  // jsp 반환할 때 Map을 반환하거나 ResponseEntitiy에 Map을 넣어서 반환 이것도 Map을 반환하는 건데 @Responsebody만 안 쓰는 것이다!
  public ResponseEntity<Map<String, Object>> checkEmail(String email);
  public ResponseEntity<Map<String, Object>> sendCode(String email);
}
