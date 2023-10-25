package com.gdu.myhome.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import com.gdu.myhome.dto.UserDto;

public interface UserService {
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception;
  public String getNaverLoginURL(HttpServletRequest request) throws Exception;
  public String getNaverLoginAccessToken(HttpServletRequest request) throws Exception;
  public UserDto getNaverProfile(String accessToken) throws Exception;
  public UserDto getUser(String email);
  public void naverJoin(HttpServletRequest request, HttpServletResponse response); 
  public void naverLogin(HttpServletRequest request, HttpServletResponse response, UserDto naverProfile) throws Exception;
  
  public void logout(HttpServletRequest request, HttpServletResponse response);
  public ResponseEntity<Map<String, Object>> checkEmail(String email);    // jsp 반환할 때 Map을 반환하거나 ResponseEntitiy에 Map을 넣어서 반환. 이것도 Map을 반환하는 건데 @Responsebody만 안 쓰는 것이다!
  public ResponseEntity<Map<String, Object>> sendCode(String email);
  public void join(HttpServletRequest request, HttpServletResponse response);   // agree(event)에서 int인데 on, off가 들어가 있어서 command 객체 사용 불가!
  public ResponseEntity<Map<String, Object>> modify(HttpServletRequest request);
  public void modifyPw(HttpServletRequest request, HttpServletResponse response);
  public void leave(HttpServletRequest request, HttpServletResponse response);
  public void inactiveUserBatch();    // service가 아닌, scheduler가 부른다!
  public void active(HttpSession session, HttpServletRequest request, HttpServletResponse response);
}
