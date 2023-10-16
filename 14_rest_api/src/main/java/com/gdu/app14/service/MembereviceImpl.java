package com.gdu.app14.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

}
