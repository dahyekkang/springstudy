package com.gdu.app02.anno02;

import java.sql.Connection;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MyJdbcDao {

  private Connection con;
  private MyJdbcConnection myJdbcConnection;
  
  private Connection getConnection() {
    AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);              // 실제로 bean이 만들어진다.
    myJdbcConnection = ctx.getBean("myJdbcConnection", MyJdbcConnection.class); // bean을 가져온다.
    ctx.close();
    return myJdbcConnection.getConnection();
  }
  
  private void close() {
    try {
      if(con != null) {
        con.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void add() {
    con = getConnection();
    System.out.println("add() 호출");
    close();
  }
  
  public void remove() {
    con = getConnection();
    System.out.println("remove() 호출");
    close();
  }
  
  public void modify() {
    con = getConnection();
    System.out.println("modify() 호출");
    close();
  }
  
  public void select() {
    con = getConnection();
    System.out.println("select() 호출");
    close();
  }
  
}
