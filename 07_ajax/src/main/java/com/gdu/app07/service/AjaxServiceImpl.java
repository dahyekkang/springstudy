package com.gdu.app07.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gdu.app07.dao.AjaxDao;
import com.gdu.app07.dto.AjaxDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor    // 초기화 되지않은 final 필드에 대해 생성자를 생성해준다.
@Service
public class AjaxServiceImpl implements AjaxService {

  private final AjaxDao ajaxDao;
  
  @Override
  public List<AjaxDto> getDtoList() {
    return ajaxDao.getDtoList();
  }

  @Override
  public AjaxDto getDto(String name) {
    int no = 0;
    if(name.equals("뽀로로")) {
      no = 1;
    } else if(name.equals("루피")) {
      no = 2;
    } else if(name.equals("크롱")) {
      no = 3;
    }
    return ajaxDao.getDto(no);
  }

}
