package com.gdu.app07.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdu.app07.dto.AjaxDto;
import com.gdu.app07.service.AjaxService;

import lombok.RequiredArgsConstructor;

@RequestMapping(value="/ajax2")
@RestController   // ajax 전용 Controller. 모든 메소드에 @ResponseBody를 추가하는 @Controller이다. 
@RequiredArgsConstructor
public class AjaxController2 {

  private final AjaxService ajaxService;
  
  @GetMapping(value="/list.do", produces=MediaType.APPLICATION_JSON_VALUE)
  public List<AjaxDto> getDtoList() {
    return ajaxService.getDtoList();
  }
  
  @GetMapping(value="/detail.do", produces=MediaType.APPLICATION_JSON_VALUE)
  public AjaxDto getDto(@RequestParam(value="name") String name) {
    return ajaxService.getDto(name);
  }
  
}
