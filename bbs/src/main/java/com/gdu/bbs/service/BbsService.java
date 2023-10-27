package com.gdu.bbs.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.gdu.bbs.dto.BbsDto;

public interface BbsService {

  public void loadBbsList(HttpServletRequest request, Model model);    // 요청에서 파라미터를 뺀다.
  /*
   * 요청 파라미터 말고 다른 것도 써야되면 request 사용!!
   * 
   * request 사용하는 이유
   * 1. getParameter로 page 받으려고 getParameter(page)
   * 2. getContextPath()를 제공해서
   * 
   * contextPath가 필요없는 상황이 오면, 그냥 int page로 바뀌어도 된다!
   * 
   * 저장하고 싶은 게 2개일 때, model에 저장하는데 서비스한테 너가 저장해라. 하는 것임
   * 만약 저장하는 게 목록밖에 없으면 그냥 목록을 반환해도 된다. 그러면 그걸 controller가 받아서 저장한다.
   */
  
  public BbsDto getBbs(int bbsNo);
  public int addBbs(BbsDto bbs);
  public int modifyBbs(BbsDto bbs);
  public int removeBbs(int BbsNo);
  
}
