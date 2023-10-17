package com.gdu.app14.service;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.gdu.app14.dao.MemberMapper;
import com.gdu.app14.dto.MemberDto;
import com.gdu.app14.util.PageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service  // @Service를 붙이면 Component가 된다.
public class MembereviceImpl implements MemberService {

  private final MemberMapper memberMapper;
  private final PageUtil pageUtil;    // 가져오려면 pageutil을 bean으로 만들어주어야 한다. 그래야 @Autowired로 가져올 수 있음!
  
  @Override
  public Map<String, Object> register(MemberDto memberDto, HttpServletResponse response) {

    Map<String, Object> map = null;
   
    try {   
      
      int addResult = memberMapper.insertMember(memberDto);
      map = Map.of("addResult", addResult);
      
    } catch(DuplicateKeyException e) {    // UNIQUE 칼럼에 중복 값이 전달된 경우에 발생함
      
      // 기본키로 설정된 항목을 침해하는 입력이 들어왔을 때 발생하는 에러로, 동일한 아이디를 등록하려 할 때 나는 오류이다.
      // service의 catch block에서 만든 응답은 ajax의 success로 가지 않고, ajax의 error로 간다!
      

      try {
        response.setContentType("text/plain");    // 일반 텍스트 동작
        PrintWriter out = response.getWriter();
        response.setStatus(500);                         // 예외객체 jqXHR의 status 속성으로 확인함
        out.print("이미 사용 중인 아이디입니다. ");      // 예외객체 jqXHR의 responseText 속성으로 확인함
        out.flush();
        out.close();        
      } catch(Exception e2) {
        e.printStackTrace();
      }
      
    } catch (Exception e) {
      System.out.println(e.getClass().getName()); // 발생한 예외 클래스 이름 확인
    }
        
     return map;
  }
  
  @Override
  public Map<String, Object> getMembers(int page) {

    int total = memberMapper.getMemberCount();    // 총 몇 개?
    int display = 2;    // 한 페이지에 몇 개?
    
    pageUtil.setPaging(page, total, display);     // 계산이 끝난다!
    
    Map<String, Object> map = Map.of("begin", pageUtil.getBegin(), "end", pageUtil.getEnd());
    
    List<MemberDto> memberList = memberMapper.getMemberList(map);
    String paging = pageUtil.getAjaxPaging();
    
    return Map.of("memberList", memberList, "paging", paging);
  }

  @Override
  public Map<String, Object> getMember(int memberNo) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("member", memberMapper.getMember(memberNo));
    return map;   // key나 value가 null이면 of 메소드 사용 불가하므로 위처럼 Map.of 사용X
  }
  
  @Override
  public Map<String, Object> modifyMember(MemberDto memberDto) {
    int modifyResult = memberMapper.updateMember(memberDto);
    return Map.of("modifyResult", modifyResult);    // null일리가 없어서 Map.of 사용O
  }
  
  @Override
  public Map<String, Object> removeMember(int memberNo) {
    return Map.of("removeResult", memberMapper.deleteMember(memberNo));
  }
  
  @Override
  public Map<String, Object> removeMembers(String memberNoList) {
    List<String> list = Arrays.asList(memberNoList.split(","));
    return Map.of("removeResult", memberMapper.deleteMembers(list));
  }

}
