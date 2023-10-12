package com.gdu.app11.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.app11.dao.ContactDao;
import com.gdu.app11.dto.ContactDto;

import lombok.RequiredArgsConstructor;

@Transactional    // txTest() 메소드쪽에 해도 되고, 지금 위치인 클래스쪽에 해도 된다. AppConfig에 @EnableTransactionManagement    도 해주어야 한다. 원하는 동작 : 웹 주소창에 tx.do 했을 때, 이름, 전화번호,..가 삽입되지 않아야 한다. (원자성)
@RequiredArgsConstructor    // private final ContactDao contactDao;에 @Autowired를 하기 위한 코드이다.
@Service    // ContactService 타입의 객체(Bean)을 Spring Container에 저장한다.
public class ContactServiceImpl implements ContactService {
  
  private final ContactDao contactDao;

  @Override
  public int addContact(ContactDto contactDto) {
    int addResult = contactDao.insert(contactDto);
    return addResult;
  }

  @Override
  public int modifyContact(ContactDto contactDto) {
    int modifyResult = contactDao.update(contactDto);
    return modifyResult;
  }

  @Override
  public int deleteContact(int contactNo) {
    int deleteResult = contactDao.delete(contactNo);
    return deleteResult;
  }

  // @Transactional(readOnly=true) : 클래스에서 @Transactional을 사용하여 전체적으로 transaction을 걸고, database 수정이 없는 select들에는 transaction처리 X (안 해도 되지만 성능을 위해!)
  @Transactional(readOnly = true)   // 조회용(성능 이점)
  @Override
  public List<ContactDto> getContactList() {
    return contactDao.selectList();
  }

  @Transactional(readOnly = true)   // 조회용(성능 이점)
  @Override
  public ContactDto getContactByNo(int contactNo) {
    return contactDao.selectContactByNo(contactNo);
  }
  
  @Override
  public void txTest() {

    // @Transactional을 활용한 트랜잭션 처리 테스트 메소드
    
    // "성공1개+실패1개" DB 처리를 동시에 수행했을 때, 모두 실패로 되는지 확인하기
    
    // 성공
    contactDao.insert(new ContactDto(0, "이름", "전화번호", "이메일", "주소", null));
    // AppConfig의 public Advisor advisor()에서 @Bean에 주석을 걸면 주소창에 tx.do했을 때, 위 내용이 삽입된 채로 txTest()메소드가 보인다.
    // 하나만 성공하고 하나는 실패했는데 성공한 행이 보이면 문제가 생긴다. 트랜잭션 처리가 되지 않은 것임!
    
    // 실패
    contactDao.insert(new ContactDto());    // NAME 칼럼은 NOT NULL이므로 전달된 이름이 없으면 Exception이 발생한다.
    
  }

}
