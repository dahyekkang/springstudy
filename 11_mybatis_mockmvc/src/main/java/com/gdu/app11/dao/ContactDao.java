package com.gdu.app11.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.gdu.app11.dto.ContactDto;

import lombok.RequiredArgsConstructor;

//ContactMain -> ContactController -> ContactService -> ContactDao -> DB


// Contact table에 접근하기 위한 객체를 따로 만듦

/*
 * DAO
 * 1. Database Access Object
 * 2. 데이터베이스에 접근해서 쿼리문을 실행하고 쿼리문의 실행 결과를 받는 객체이다.
 * 3. 하나의 객체만 만들어서 사용하는 Singleton Pattern으로 객체를 생성한다.
 */

@RequiredArgsConstructor
@Repository
public class ContactDao {
  
  /*
   * Singleton Pattern
   * 1. 오직 하나의 객체만 만들 수 있도록 처리하는 패턴이다.
   * 2. 미리 하나의 객체를 만든 뒤 해당 객체를 가져다 사용할 수 있도록 처리한다.
   * 3. 객체 생성이 불가능하도록 생성자를 호출할 수 없게 만든다.
   */
  
  private final SqlSessionTemplate sqlSessionTemplate;    // @RequiredArgsConstructor에 의해 자동으로 Autowired된다.

  /**
   * 삽입메소드<br>
   * @param contactDto 삽입할 연락처 정보(name, tel, email, address)
   * @return insertCount 삽입된 행(Row)의 개수, 1이면 삽입 성공, 0이면 삽입 실패
   */
  public int insert(ContactDto contactDto) {
    return sqlSessionTemplate.insert("mybatis.mapper.contact.insert", contactDto);
  }
  
  /**
   * 수정 메소드<br>
   * @param contactDto 수정할 연락처 정보(contact_no, name, tel, email, address)
   * @return updateCount 수정된 행(Row)의 개수, 1이면 수정 성공, 0이면 수정 실패
   */
  public int update(ContactDto contactDto) {
    return sqlSessionTemplate.update("mybatis.mapper.contact.update", contactDto);
  }
  
  /**
   * 삭제 메소드<br>
   * @param contact_no 삭제할 연락처 번호
   * @return deleteCount 삭제된 행(Row)의 개수, 1이면 삭제 성공, 0이면 삭제 실패
   */
  public int delete(int contactNo) {
    return sqlSessionTemplate.delete("mybatis.mapper.contact.delete", contactNo);
  }
  
  /**
   * 전체 조회 메소드<br>
   * @return 조회된 모든 연락처 정보(ContactDto)
   */
  public List<ContactDto> selectList(){
    return sqlSessionTemplate.selectList("mybatis.mapper.contact.selectList");
  }
  
  /**
   * 상세 조회 메소드<br>
   * @param contact_no 조회할 연락처 번호
   * @return contactDto 조회된 연락처 정보, 조회된 연락처가 없으면 null 반환
   */
  public ContactDto selectContactByNo(int contactNo) {
    return sqlSessionTemplate.selectOne("mybatis.mapper.contact.selectContactByNo", contactNo);
  }
  
}
