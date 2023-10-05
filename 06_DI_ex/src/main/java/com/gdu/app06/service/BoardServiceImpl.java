package com.gdu.app06.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdu.app06.dao.BoardDao;
import com.gdu.app06.dto.BoardDto;

public class BoardServiceImpl implements IBoardService {

  private BoardDao boardDao;

  @Autowired    // boardDao라는 게 만들어져 있다면 만들어진 객체를 여기로 가져와서 (boardDao)  그 값을 필드로 넘겨준다(this.boardDao).
  public void setBoardDao(BoardDao boardDao) {
    this.boardDao = boardDao;
  }

  @Override
  public List<BoardDto> getBoardList() {
    return boardDao.getBoardList();
  }

  @Override
  public BoardDto getBoardByNo(int boardNo) {
    return boardDao.getBoardByNo(boardNo);
  }

}
