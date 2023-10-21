package com.gdu.board.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gdu.board.dto.BoardDto;

public interface BoardService {
  
  public List<BoardDto> getBoardList();
  public int addBoard(HttpServletRequest request);
  public BoardDto getBoard(HttpServletRequest request);
  public int removeBoard(HttpServletRequest request);
}
