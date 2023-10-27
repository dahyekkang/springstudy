package com.gdu.myhome.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.myhome.dto.BlogDto;

@Mapper
public interface BlogMapper {
  public int insertBlog(BlogDto blog);
}
