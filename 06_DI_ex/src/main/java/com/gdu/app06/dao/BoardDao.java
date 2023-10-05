package com.gdu.app06.dao;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdu.app06.dto.BoardDto;

public class BoardDao {
  
  private BoardDto boardDto1;
  private BoardDto boardDto2;
  private BoardDto boardDto3;

  
  @Autowired  // Setter 형식 메소드에 주입. 3개를 따로 만든 뒤 annotation을 3개 넣으면 안 되고, 이렇게 직접 만들어서 1개로 해야 한다!
  public void setBean(BoardDto boardDto1, BoardDto boardDto2, BoardDto boardDto3) {
    this.boardDto1 = boardDto1;
    this.boardDto2 = boardDto2;
    this.boardDto3 = boardDto3;
  }

  public List<BoardDto> getBoardList() {
    return Arrays.asList(boardDto1, boardDto2, boardDto3);
  }
  
  public BoardDto getBoardByNo(int boardNo) {
    
    BoardDto boardDto = null;
    
    if(boardDto1.getBoardNo() == boardNo) {
      boardDto = boardDto1;
    } else if(boardDto2.getBoardNo() == boardNo) {
      boardDto = boardDto2;
    } else if(boardDto3.getBoardNo() == boardNo) {
      boardDto = boardDto3;
    }
    return boardDto;    // boardDto1 or 2 or 3 반환 하거나 null 반환
  }
}
