package com.gdu.app01.anno02;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class MainWrapper {

  public static void main(String[] args) {

    // AppConfig.java에 등록된 bean 생성
    AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    
    // bean 가져오기
    User user = ctx.getBean("user", User.class);
    Board board = ctx.getBean("board", Board.class);
    
    // 확인
    System.out.println(user.getUserNo() + ", " + user.getUserId());
    System.out.println(board.getTitle() + ", " + board.getEditor());
    
    // ctx 닫기
    ctx.close();
    
  }

}
