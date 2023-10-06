package com.gdu.app07.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gdu.app07.dto.AjaxDto;
import com.gdu.app07.service.AjaxService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/ajax1")
@RequiredArgsConstructor
@Controller
public class AjaxController1 {

  private final AjaxService ajaxService;
  
  @ResponseBody     // 메소드의 반환 값이 응답 데이터이다. 응답 데이터 타입은 RequestMapping에 produces에 적는다. 일반적으로 return은 jsp인데 jsp가 아닐 때 알려주는 annotation.
  @RequestMapping(value="/list.do", method=RequestMethod.GET, produces="application/json; charset=UTF-8") // produces : 응답 데이터 타입
  // 실제 반환값은 list인데, json이라고 적어주면, jackson 데이터 바인더(라이브러리)가 list에서 json으로 알아서 변환한다!
  public List<AjaxDto> list() {
    return ajaxService.getDtoList();   // jackson 라이브러리가 List<AjaxDto>를 json 데이터(배열)로 자동 변환한다.
  }
  
  @ResponseBody
  @RequestMapping(value="/detail.do", method=RequestMethod.GET, produces="application/json; charset=UTF-8")
  public AjaxDto detail(@RequestParam(value="name") String name) {
    return ajaxService.getDto(name);    // jackson 라이브러리가 AjaxDto를 json 데이터(객체)로 자동 변환한다.
  }
  
}
