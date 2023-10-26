package com.gdu.board.service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.gdu.board.dao.BoardDao;
import com.gdu.board.dto.BoardDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

  private final BoardDao boardDao;
  @Override
  public List<BoardDto> getBoardList() {
    return boardDao.getBoardList();
  }
  

  @Override
  public int addBoard(HttpServletRequest request) {
    BoardDto board = new BoardDto();
    board.setAuthor(request.getParameter("author"));
    board.setTitle(request.getParameter("title"));
    board.setContent(request.getParameter("content"));
    board.setHit(Integer.parseInt(request.getParameter("hit")));
    board.setIp(request.getParameter("ip"));
    return boardDao.addBoard(board);
  }
  
  @Override
  public BoardDto getBoard(HttpServletRequest request) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("no"));
    int no = Integer.parseInt(opt.orElse("0"));
    return boardDao.getBoard(no);
  }
  
  @Override
  public int removeBoard(HttpServletRequest request) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("no"));
    int no = Integer.parseInt(opt.orElse("0"));
    return boardDao.removeBoard(no);
  }
  
}
