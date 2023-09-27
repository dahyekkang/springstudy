package com.gdu.app02.anno02;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MyJdbcService {
  
  private MyJdbcDao myJdbcDao;    // service는 dao를 필요로 한다.
  
  public void add() {
    myJdbcDao.add();
  }
  
  public void remove() {
    myJdbcDao.remove();
  }
  
  public void modify() {
    myJdbcDao.modify();
  }
  
  public void select() {
    myJdbcDao.select();
  }
  
}
