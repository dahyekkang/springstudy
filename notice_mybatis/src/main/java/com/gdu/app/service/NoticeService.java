package com.gdu.app.service;

import java.util.List;

import com.gdu.app.dto.NoticeDto;

// 전달받은 거 전해주고 반환할 거 반환하는 역할

public interface NoticeService {
  
  int modifyNotice(NoticeDto noticeDto);
 
  NoticeDto getNotice(int noticeNo);
  
  int addNotice(NoticeDto noticeDto);
  
  List<NoticeDto> getNoticeList();
  
}
