package com.gdu.app11;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.extern.slf4j.Slf4j;

// 통합테스트

// JUnit4를 사용한다.
@RunWith(SpringJUnit4ClassRunner.class)         // junit4를 쓸 것 이다.

// 테스트에서 사용할 Bean이 @Component로 생성되었다.
@ContextConfiguration(locations="file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml")  // servlet-context.xml의 <context:component-scan>(빈으로 등록될 준비를 마친 클래스들을 스캔하여, 빈으로 등록해주는 것)로 인해 bean이 등록되기 때문에 이 경로를 적어준다.

// 메소드 이름 순으로 테스트를 수행한다.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)   // 테스트를 수행하는 순서 : 메소드의 알파벳순

// WebApplicationContext를 사용할 수 있다. (Spring Container에 Bean을 생성해 둔다.)
@WebAppConfiguration                            // Tomcat이 필요하다.

// 로그
@Slf4j
public class ContactControllerTest {

  // MockMvc 객체가 생성될 때 필요한 WebApplicationContext
  // WebApplicationContext가 하는 일 : MockMvc라는 객체를 가지고 테스트를 수행하는 건데 WebApplicationContext를 기반으로 수행하기 때문에 WebApplicationContext를 만든다.
  @Autowired
  private WebApplicationContext webApplicationContext;    // @WebAppConfiguration이 bean을 생성해준다. 가져오는 건 @Autowired로.
  
  // MockMvc 객체 : 테스트를 수행하는 객체
  private MockMvc mockMvc;
  
  // 테스트 수행 이전에 MockMvc mockMvc 객체를 만든다.
  @Before
  public void setUp() throws Exception {    // setUp이라는 메소드를 사용. 오류 처리 필요
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();    // 인터페이스가 아니라 클래스가 필요하다.(MockMvcBuilders)
  }
  
  // 삽입테스트
  // 요청 방식 : POST
  // 요청 주소 : /contact/add.do
  // 요청 파라미터 : name, tel, email, address
  @Test
  public void test01_삽입() throws Exception {
    // 삽입하고 삽입 결과 로그 찍기
    log.info(mockMvc
              .perform(MockMvcRequestBuilders               // 요청 수행
                    .post("/contact/add.do")                // 요청 방식, 주소
                    .param("name", "뽀로로")                // 요청 파라미터 (파라미터에 값 삽입)
                    .param("tel", "02-111-1111")
                    .param("email", "pororo@naver.com")
                    .param("address", "pororo village"))
              .andReturn()                                  // 요청 결과 (실행결과 받아오기)
              .getFlashMap()                                // 요청 결과가 저장된 flash attribute를 Map으로 가져옴 (ContacController 보면 삽입은 flash로 저장했었음)
              .toString());
  }
  
  // 상세조회테스트
  // 요청 방식 : GET
  // 요청 주소 : /contact/detail.do
  // 요청 파라미터 : contactNo
  @Test
  public void test02_상세조회() throws Exception {
    log.info(mockMvc
              .perform(MockMvcRequestBuilders
                        .get("/contact/detail.do")
                        .param("contactNo", "1"))
              .andReturn()
              .getModelAndView()    // 이게 model임. 추가로, 반환할 jsp까지 다루는 클래스였는데 이제는 분리가 됐음
              .getModelMap()
              .toString());
  }
  
  
}
