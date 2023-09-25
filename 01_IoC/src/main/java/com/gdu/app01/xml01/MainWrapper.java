package com.gdu.app01.xml01;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainWrapper {
  
  public static void ex01() {
 
    // app-context.xml 파일 읽기 (여기에서 <bean> 태그로 정의해 둔 객체가 생성된다.)
    // generic이 xml을 읽고 bean을 만든다! -> new Calculator 할 필요 없고 알아서 돼 있을 것이다.
    AbstractApplicationContext ctx = new GenericXmlApplicationContext("xml01/app-context.xml");
    
    // <bean> 태그로 정의된 객체 가져오기
    // 같은 패키지 내에 있으므로 Calculator import할 필요 없다.
    // calc라는 이름의 bean을 가져오시오. 캐스팅 해야 한다. 하지 않으려면 2번째 파라미터로 캐스팅할 클래스를 사용한다.
    Calculator calculator = (Calculator)ctx.getBean("calc");  // ctx.getBean("calc", Calculator.class) 코드도 동일함
    
    // 객체 사용하기
    calculator.add(1, 2);
    calculator.sub(3, 4);
    calculator.mul(5, 6);
    calculator.div(7, 8);
    
    // app-context.xml 파일 닫기
    ctx.close();
    
    // tomcat으로 실행하는 것도 아니고, server로 실행하는 것도 아니고,
    // java main이므로 java application으로 실행하는 것이다.
    
  }
  
  public static void ex02() {
    
    // app-context.xml 파일 읽어서 <bean> 태그로 정의된 객체 만들기
    AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("xml01/app-context.xml");
    
    // 객체 가져오기(man, woman)
    Person man = (Person)ctx.getBean("man");
    Person woman = ctx.getBean("woman", Person.class);
    
    // 객체 확인
    System.out.println(man.getName() + ", " + man.getAge());
    man.getCalculator().add(1, 2);
    System.out.println(woman.getName() + ", " + woman.getAge());
    woman.getCalculator().add(3, 4);
    
    // app-context.xml 파일 닫기
    ctx.close();
    
  }

  public static void main(String[] args) {

    ex01();
    ex02();
    
  }

}
