package com.gdu.app01.anno01;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration    // IocContainer에 bean을 등록하는 클래스이다.  // @Configuration이 명시된 클래스는 Bean으로 등록됨  // bean을 만드는 역할을 수행하는 자바 클래스가 된다.
public class AppConfig {

  // 메소드를 bean으로 등록하기 위해서 @Bean을 추가한다. (Bean 태그가 된다.)
  
  @Bean
  public Calculator calc() {  // 메소드이름 == bean이름
    return new Calculator();
  }
  
  @Bean
  public Person man() {
    Person person = new Person();
    person.setName("뽀로로");
    person.setAge(20);
    person.setCalculator(calc());
    return person;
  }
  
  @Bean
  public Person woman() {
    return new Person("루피", 20, calc());
  }
  
}
