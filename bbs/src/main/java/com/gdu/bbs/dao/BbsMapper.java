package com.gdu.bbs.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.bbs.dto.BbsDto;

@Mapper
public interface BbsMapper {    // 인터페이스 BbsMapper를 호출하면 실제로는 bbsMapper.xml이 실행된다!
  
  public List<BbsDto> getBbsList(Map<String, Object> map);
  public int getBbsCount();
  public BbsDto getBbs(int bbsNo);
  public int insertBbs(BbsDto bbs);
  public int updateBbs(BbsDto bbs);
  public int deleteBbs(int bbsNo);
}
