package com.gdu.app05.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gdu.app05.dao.BoardDao;
import com.gdu.app05.dto.BoardDto;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


  
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

  
// @Component   // spring container에 저장이 될 수 있도록

// @AllArgsConstructor    // final 필드 전용 annotation이 따로 있다.
@RequiredArgsConstructor  // final field 전용 생성자
                          // @Autowired를 이용한 생성자 주입을 위해서 추가한다.
@Service    // 서비스 계층(Business Layer) 전용 @Component, Spring Container에 BoardService 타입의 boardServiceImpl 객체를 생성해 둔다.
public class BoardServiceImpl implements BoardService {
  
  // private BoardDao boardDao = new BoardDao();   // 이건 spring이 아닌데, spring 방식으로 바꿀 것이다!
  
  // 주입된 boardDao 객체의 변경 방지를 위해 final 처리한다.
  private final BoardDao boardDao;
  
  // @Autowired // spring4 이후부터 생략 가능!
//  public BoardServiceImpl(BoardDao boardDao) {    // 생성자에 주입
//    super();
//    this.boardDao = boardDao;
//  } 
  
  
  @Override
  public List<BoardDto> getBoardList() {
    return boardDao.getBoardList();
  }



}
