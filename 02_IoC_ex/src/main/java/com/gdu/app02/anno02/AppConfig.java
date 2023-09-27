package com.gdu.app02.anno02;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public MyJdbcConnection myJdbcConnection() {
    MyJdbcConnection myJdbcConnection = new MyJdbcConnection();
    myJdbcConnection.setDriver("oracle.jdbc.OracleDriver");
    myJdbcConnection.setUrl("jdbc:oracle:thin:@127.0.0.1:1521:xe");
    myJdbcConnection.setUser("GD");
    myJdbcConnection.setPassword("1111");
    return myJdbcConnection;
  }
  
  @Bean
  public MyJdbcDao myJdbcDao() {
    return new MyJdbcDao();
  }
  
  @Bean
  public MyJdbcService myJdbcService() {
    return new MyJdbcService(myJdbcDao());
  }
  
}
