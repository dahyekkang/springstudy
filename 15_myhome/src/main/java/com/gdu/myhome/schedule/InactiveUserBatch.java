package com.gdu.myhome.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gdu.myhome.service.UserService;

import lombok.RequiredArgsConstructor;

//@EnableScheduling           // scheduling을 허용하라 AppConfig에도 붙여줬음. 여러 개의 Scheduling을 사용하기 위해 붙여둬서 여기에 안 붙여도 된다!
@RequiredArgsConstructor    // autowired작업을 위해(private final)
@Component
public class InactiveUserBatch {

  private final UserService userService;
  
  @Scheduled(cron="0 0 0 1/1 * ?")  // 언제 동작할건지 시간 적기 (공백 필수)  // 매일 자정에 동작
  // @Scheduled(cron="0 28 9 1/1 * ?")
  public void execute() {
    userService.inactiveUserBatch();
  }
  
}
