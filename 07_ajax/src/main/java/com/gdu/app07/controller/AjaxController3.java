package com.gdu.app07.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gdu.app07.dto.AjaxDto;
import com.gdu.app07.service.AjaxService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/ajax3")
@RequiredArgsConstructor    // 만들어져있는 bean을 자동 주입하기 위해서
@Controller
public class AjaxController3 {

  private final AjaxService ajaxService;
  
  // List를 ResponseEntity로 감싼다. ResponseEntity에 List뿐만 아니라 다른 것들도 같이 넣을 수 있다.
  // ResponseEntity에는 ResponseBody가 포함되어 있어서 ResponseBody를 작성하지 않아도 된다.
  // 목록(List)을 ResponseEntity에 넣어서 반환하겠다.
  
  @GetMapping(value="/list.do", produces="application/json; charset=UTF-8")
  public ResponseEntity<List<AjaxDto>> list() {
    return new ResponseEntity<List<AjaxDto>>( // ResponseEntity는 @ResponseBody를 작성할 필요가 없다.
                ajaxService.getDtoList()      // 실제 응답 데이터
              , HttpStatus.OK);               // 응답 코드 (200)
  }
  
  @PostMapping(value="/detail.do")
  public ResponseEntity<AjaxDto> detail(@RequestBody AjaxDto ajaxDto) { // post 방식(@RequestBody)으로 전송된 JSON 데이터(AjaxDto ajaxDto) // get일 때는 요청 파라미터를 찾았지만 post일 때는 요청 본문에서 찾아야하므로 @RequestBody 사용해야 한다.
    // 응답 헤더 : Content-Type(produces="application/json; charset=UTF-8"을 대체한다.)
    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "application/json; charset=UTF-8");    // header에 작성될 수 있는 것 중 Content-Type 을 추가
    // 반환
    return new ResponseEntity<AjaxDto>(                 // ResponseEntity는 @ResponseBody를 작성할 필요가 없다.         
                ajaxService.getDto(ajaxDto.getName())   // 실제 응답 데이터
              , header                                  // 응답 헤더
              , HttpStatus.OK);                         // 응답 코드 (200)
  }
}
