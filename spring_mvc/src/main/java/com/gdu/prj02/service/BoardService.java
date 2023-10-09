package com.gdu.prj02.service;

import javax.servlet.http.HttpServletRequest;

import com.gdu.prj02.dto.BoardDto;

public interface BoardService {

  public int add1(HttpServletRequest request);
  public int add2(int boardNo, String title);
  public int add3(BoardDto boardDto);
  
}
