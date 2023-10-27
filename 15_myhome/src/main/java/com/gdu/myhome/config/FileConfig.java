package com.gdu.myhome.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

// 파일첨부용

@Configuration
public class FileConfig {

  @Bean
  public MultipartResolver multipartResolver() {
    CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();   // Service와  ServiceImpl의 관계와 같다.
    commonsMultipartResolver.setDefaultEncoding("UTF-8");
    commonsMultipartResolver.setMaxUploadSize(1024 * 1024 * 100);       // 전체 첨부 파일의 최대 크기 100MB (1024=1KB, 1024*1024=1MB, 1024*1024*100=100MB)
    commonsMultipartResolver.setMaxUploadSizePerFile(1024 * 1024 * 10); // 개별 첨부 파일으 최대 크기 10MB
    return commonsMultipartResolver;
  }
  
}
