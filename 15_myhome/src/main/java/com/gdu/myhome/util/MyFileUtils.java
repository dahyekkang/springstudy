package com.gdu.myhome.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component    // @Autowired로 불러다 쓰기 위해
public class MyFileUtils {

  // 블로그 작성 시 사용된 이미지가 저장될 경로 반환하기
  public String getBlogImagePath() {
    LocalDate today = LocalDate.now();    // 오늘
    return "/blog/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(today);
    }
  
  // 파일이 저장될 이름 반환하기
  public String getFilesystemName(String originalFileName) {
    
    /*  UUID.확장자  */    // Universal Unique Id : 중복없는 값을 임의로  만들어주는 자바 클래스
    
    String extName = null;
    // tar.gz(확장자)로 끝나는지. 마지막 마침표를 찾아서 잘라내면 확장자라고 판단하는데 얜 예외다.
    if(originalFileName.endsWith("tar.gz")) {   // 확장자에 마침표가 포함되는 예외 경우를 처리한다.
      extName = "tar.gz";
    } else {
      String[] arr = originalFileName.split("\\.");   // [.] 또는 \\.  (정규식에서 .는 모든 문자이기 때문에 이 둘 중 하나를 사용해야 한다.)
      extName = arr[arr.length - 1];
    }
    
    return UUID.randomUUID().toString().replace("-", "") + "." + extName;
    
  }
  
  // 블로그 이미지가 저장된 어제 경로를 반환
  public String getBlogImagePathInYesterday() {
    LocalDate date = LocalDate.now();
    date = date.minusDays(1);   // 1일 전
    return "/blog/" + DateTimeFormatter.ofPattern("yyyy/MM/dd").format(date);
  }
  
}
