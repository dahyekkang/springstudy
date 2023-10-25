package com.gdu.myhome.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface FreeService {

  public int addFree(HttpServletRequest request);   // contents 제외하면 사용자가 입력한 게 아님 작성자의 email정보는 session에 저장된 user에 있다!
  public void loadFreeList(HttpServletRequest request, Model model);
  
}
