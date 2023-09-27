package com.gdu.app02.anno02;

import java.sql.Connection;
import java.sql.DriverManager;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MyJdbcConnection {
  private String driver;    // oracle.jdbc.OracleDriver             // 어떤 db를 사용하는지
  private String url;       // jdbc:oracle:thin:@127.0.0.1:1521:xe  // db의 접속 주소
  private String user;      // GD                                   // db 사용자
  private String password;  // 1111                                 // db 비밀번호
  
  public Connection getConnection() {
    Connection con = null;    // connection이 실패했을 때를 위해 초기화 작업
    try {
      Class.forName(driver);    // driver 로드 - 클래스 이름이 driver에 들어있다. 무슨 database를 쓴다.(mysql인지 다른 것인지)
      con = DriverManager.getConnection(url, user, password);   // drivermanager에 getconnection을 부르면 connection을 만들어준다.
    } catch (Exception e) {
        e.printStackTrace();
    }
    return con;
  }
}
