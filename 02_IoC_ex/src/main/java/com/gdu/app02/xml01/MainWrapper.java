package com.gdu.app02.xml01;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainWrapper {

  public static void main(String[] args) {

    AbstractApplicationContext ctx = new GenericXmlApplicationContext("xml01/appCtx.xml");
    Person p1 = ctx.getBean("person1", Person.class);
    Person p2 = ctx.getBean("person2", Person.class);
    System.out.println(p1);
    System.out.println(p2);
    ctx.close();

  }

}