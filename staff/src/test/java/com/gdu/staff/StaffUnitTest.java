package com.gdu.staff;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdu.staff.dao.StaffMapper;
import com.gdu.staff.dto.StaffDto;

/*
 * JUnit 처리 방법
 * 1. spring-test dependency를 추가한다.
 * 2. @RunWith를 추가한다. (클래스레벨)
 * 3. @ContextConfiguration을 추가한다. (클래스레벨)
 *    ContactDao 객체(Bean)을 생성한 방법에 따라서 아래 3가지 방식 중 선택한다.
 *  1) <bean> 태그 : @ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/root-context.xml")
 *  2) @Bean       : @ContextConfiguration(classes=AppConfig.class)
 *  3) @Component  : @ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")
 */

// JUnit4를 이용한다.
@RunWith(SpringJUnit4ClassRunner.class)

//객체 생성 방법이 어떻게 돼있느냐. ApplicationContext를 무엇을 이용했냐. bean을 이용했는지, @Bean을 이용했는지, @Component를 이용했는지
//<bean>로 작업했으면 root-context.xml로 만들었을 거고, @Bean으로 작업했으면 자바로 만들었기 때문에 AppConfig.java에 있을 것이다.
//@Repository(@Component)로 작업한 것은 servlet-context.xml에 <context:component-scan>태그에 있다. component-scan을 알려주면 된다!

// StaffMapper Bean 생성 방법을 알려준다.
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")

public class StaffUnitTest {

  @Autowired  // Spring Container에서 StaffMapper 객체(Bean)를 가져온다.
  private StaffMapper staffMapper;
  
  @Before       // 테스트 메소드 수행 전 삽입부터 해라
  public void 삽입테스트() {
    StaffDto staff = new StaffDto("99999", "김기획", "기획부", 5000);
    int addResult = staffMapper.insertStaff(staff);
    assertEquals(1, addResult);    // addResult가 1이면 테스트 성공이다.
  }
  
  @Test       // 테스트를 수행한다.
  public void 조회테스트() {
    String sno = "99999";
    StaffDto staff = staffMapper.getStaff(sno);   // 조회되면 있는 거고 조회 안 되면 없는 것이다.
    assertNotNull(staff);        // staff가 not null이면 테스트 성공이다.
  }
  
  // getStaffList

}
