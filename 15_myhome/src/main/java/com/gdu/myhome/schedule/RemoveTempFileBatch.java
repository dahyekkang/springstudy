package com.gdu.myhome.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gdu.myhome.service.UploadService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RemoveTempFileBatch {
  
  private final UploadService uploadService;
  
  @Scheduled(cron="0 24 16 * * ?")  // 16시 24분에 임시파일 지우기
  public void execute() {
    uploadService.removeTempFiles();
  }
  
}
