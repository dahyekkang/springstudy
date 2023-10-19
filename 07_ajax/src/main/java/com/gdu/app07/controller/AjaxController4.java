package com.gdu.app07.controller;

import java.io.File;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// 이미지 반환 controller

@Controller
public class AjaxController4 {

  @GetMapping(value="/ajax4/display.do", produces="application/octet-stream")   // "application/octet-stream" : 이진 데이터(0, 1) 자체를 의미한다.  파일 자체의 데이터 타입
  public ResponseEntity<byte[]> display(@RequestParam("path") String path         // ResponseEntity사용하면 ResponseBody 안 써도 됨
                                      , @RequestParam("filename") String filename) {
    
    ResponseEntity<byte[]> responseEntity = null;
    
    // 파일을 읽기 위해 byte배열로 복사해놓으려면 try-catch가 있어야 한다
    try {
      
      // File 객체 (만들지 않고, 읽기만 할 것임)
       File file = new File(path, filename);
       
       // File 객체를 byte[]로 변환하는 Spring 클래스 : FileCopyUtils
       byte[] b = FileCopyUtils.copyToByteArray(file);
      
       // ResponseEntity 객체 생성
       responseEntity = new ResponseEntity<byte[]>(b, HttpStatus.OK);
       
    } catch(Exception e) {
      e.printStackTrace();
    }
    
    return responseEntity;
    
  }
  
  
}
