package com.gdu.prj02.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.prj02.dao.BoardDao;
import com.gdu.prj02.dto.BoardDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor    // private final BoardDao boardDao에 Bean을 주입하기 위한 코드입니다.
@Service  // new BoardServiceImpl()를 해서 Bean으로 저장합니다.
public class BoardServiceImpl implements BoardService {
  
  // @Autowired    // @Reposoitory로 만든 Bean을 가져오세요.
  private final BoardDao boardDao;

  @Override
  public int add1(HttpServletRequest request) {
    int boardNo = Integer.parseInt(request.getParameter("boardNo"));
    String title = request.getParameter("title");
    BoardDto boardDto = new BoardDto();
    boardDto.setBoardNo(boardNo);
    boardDto.setTitle(title);
    int result = boardDao.insertBoard1(boardDto);
    return result;
  }

  @Override
  public int add2(int boardNo, String title) {
    BoardDto boardDto = new BoardDto();
    boardDto.setBoardNo(boardNo);
    boardDto.setTitle(title);
    int result = boardDao.insertBoard1(boardDto);
    return result;
  }

  @Override
  public int add3(BoardDto boardDto) {
    int result = boardDao.insertBoard3(boardDto);
    return result;

  }

}
