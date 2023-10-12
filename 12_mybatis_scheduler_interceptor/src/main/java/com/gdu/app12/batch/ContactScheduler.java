package com.gdu.app12.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gdu.app12.service.ContactService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component    // 우리가 만든 메소드는 @Component annotation 사용
public class ContactScheduler {

  private final ContactService contactService;
  
  @Scheduled(cron="0 0/1 * * * ?")
  public void doSomething() {
    
    // 1분마다 가장 예전에 등록된 연락처를 삭제하는 스케쥴러
    // DELETE - ContactMapper/contactMapper - ContactService/ContactServiceImpl
    
    contactService.deleteOldestContact();
    log.info("doSomething()");
    
  }
  
}
