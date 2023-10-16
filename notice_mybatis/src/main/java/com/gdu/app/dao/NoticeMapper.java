package com.gdu.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.app.dto.NoticeDto;

@Mapper   // xml과 연결하기 위해 mybatis에서 제공하는 @Mapper 사용
public interface NoticeMapper {
  int modifyNotice(NoticeDto noticeDto);
  NoticeDto getNotice(int noticeNo);
  int addNotice(NoticeDto noticeDto);
  List<NoticeDto> getNoticeList();
}
