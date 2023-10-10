package com.gdu.app09;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gdu.app09.dao.ContactDao;
import com.gdu.app09.dto.ContactDto;

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

// ContactDao Bean 생성 방법을 알려준다.
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")

// 테스트 메소드의 이름 오름차순(알파벳순)으로 테스트를 수행한다.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class ContactUnitTest {

  @Autowired  // Spring Container에서 ContactDao 객체(Bean)를 가져온다.
  private ContactDao contactDao;
  
  @Test       // 테스트를 수행한다.
  public void test01_삽입테스트() {
    ContactDto contactDto = new ContactDto(0, "이름", "연락처", "이메일", "주소", "");
    int insertResult = contactDao.insert(contactDto);
    assertEquals(1, insertResult);    // insertResult가 1이면 테스트 성공이다.
  }
  
  @Test       // 테스트를 수행한다.
  public void test02_조회테스트() {
    int contact_no = 1;
    ContactDto contactDto = contactDao.selectContactByNo(contact_no);
    assertNotNull(contactDto);        // assertDto가 not null이면 테스트 성공이다.
  }
  
  @Test       // 테스트를 수행한다.
  public void test03_삭제테스트() {
    int contact_no = 1;
    int deleteResult = contactDao.delete(contact_no);
    assertEquals(1, deleteResult);    // deleteResult가 1이면 테스트 성공이다.
    // assertNull(contactDao.selectContactByNo(contact_no));   select 결과가 null이면 테스트 성공이다.
  }

}
