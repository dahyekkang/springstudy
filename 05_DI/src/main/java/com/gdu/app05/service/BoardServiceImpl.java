package com.gdu.app05.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gdu.app05.dao.BoardDao;
import com.gdu.app05.dto.BoardDto;

public class BoardServiceImpl implements BoardService {
  
  /*
   * BoardDao DI 처리 방법
   * 
   * 1. BoardDao 타입의 객체를 Spring Container에 넣는다. (아래 3가지 방법 중 하나 이용)
   *  1) <bean> 태그            : /WEB-INF/spring/root-context.xml
   *  2) @Configuration + @Bean : com.gdu.app05.config.AppConfig.java
   *  3) @Component            
   *  
   * 2. @Autowired 를 이용해서 Spring Container에서 BoardDao 타입의 객체를 가져온다. (아래 3가지 방법 중 하나 이용)
   *  1) 필드에 주입하기
   *  2) 생성자에 주입하기
   *  3) Setter 형식의 메소드에 주입하기
   */

  // private BoardDao boardDao = new BoardDao();   // 이건 spring이 아닌데, spring 방식으로 바꿀 것이다!
  @Autowired
  private BoardDao boardDao;
  
  @Override
  public List<BoardDto> getBoardList() {
    return boardDao.getBoardList();
  } 

}
