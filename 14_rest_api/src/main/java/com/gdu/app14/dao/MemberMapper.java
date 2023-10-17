package com.gdu.app14.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.app14.dto.MemberDto;

@Mapper   // spring이 제공x, my batis가 제공하는 annotation으로, xml과 연결시켜준다. AppConfig의 @MapperScan으로 @Mapper를 찾는다.
public interface MemberMapper {

  // 삽입
  public int insertMember(MemberDto memberDto);   // 메소드의 이름은 쿼리문의 id와 동일하게. 파라미터는 쿼리문의 파라미터 타입과 동일하게.
  
  // 목록
  public List<MemberDto> getMemberList(Map<String, Object> map);   // 메소드의 이름은 쿼리문의 id와 동일하게. 파라미터는 쿼리문의 파라미터 타입과 동일하게.
  
  // 전체 개수
  public int getMemberCount();
  
  // 회원 조회
  public MemberDto getMember(int memberNo);
  
  // 회원 정보 수정
  public int updateMember(MemberDto memberDto);
  
  // 회원 정보 삭제
  public int deleteMember(int memberNo);
  
  // 회원들 정보 삭제
  public int deleteMembers(List<String> list);
  
}
