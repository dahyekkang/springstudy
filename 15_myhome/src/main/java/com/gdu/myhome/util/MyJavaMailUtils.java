package com.gdu.myhome.util;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@PropertySource("classpath:email.properties") // 읽어들일 properties 파일 적어주고. @Autowired로. (AppConfig)
@Component
public class MyJavaMailUtils {

  @Autowired
  private Environment env;
  
  public void sendJavaMail(String to, String title, String contents) {
    
    try {
      
      // Properties 객체 생성
      Properties properties = new Properties();
      properties.put("", properties)
      
    }
    
  }
  
}
