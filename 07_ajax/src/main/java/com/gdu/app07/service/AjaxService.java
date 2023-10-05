package com.gdu.app07.service;

import java.util.List;

import com.gdu.app07.dto.AjaxDto;

public interface AjaxService {

  public List<AjaxDto> getDtoList();
  public AjaxDto getDto(String name);
  
}
