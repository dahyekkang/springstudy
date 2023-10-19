package com.gdu.app14.controller;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gdu.app14.dto.MemberDto;
import com.gdu.app14.service.MemberService;

import lombok.RequiredArgsConstructor;


/*
 * REST(REpresentational State Transfer)
 * 1. 요청 주소를 작성하는 새로운 방식이다.
 * 2. 변수를 파라미터로 추가하는 Query String 방식이 아니다.
 * 3. 변수를 주소에 포함시키는 Path Variable 방식을 사용한다.
 * 4. "요청 주소(URL) + 요청 방식(Method)"을 합쳐서 요청을 구분한다.
 * 5. 예시
 *           URL         Method
 *  1) 목록  /members    GET        /members/page/1                   (/members?page=1로 생각하면 안 된다!)
 *  2) 상세  /members/1  GET        member중 1번에 있는 애를 가져와라
 *  3) 삽입  /members    POST       누군지 알아야 하니까 1이 붙는다. 1번과 3번이 같은 주소지만 Method가 다르므로 다른 것이다!
 *  4) 수정  /members    PUT        그냥 MVC할 때는 사용하지 않아도 된다. REST개발할 때만 사용      // POST와 유사한 방식(주소창 사용X)
 *  5) 삭제  /member/1   DELETE     /members/4,3,2            // 누군지 알아야 하니까 1이 붙는다.   // GET과 유사한 방식(주소창 사용O)
 *  
 *  삭제에서 /members/4,3,2 : ,로 분리해서 삭제할 데이터를 String으로 받아서 split사용하여 배열로 만들어서 배열을 리스트로 바꿔서 mybatis에서 foreach로 순회처리.
 *  
 *  주소는 2가지밖에 안 나오지만 Method가 4개이다! (GET, POST, PUT, DELETE)
 */


@RequiredArgsConstructor
@RestController   // 모든 메소드에 @ResponseBody를 추가한다.  // RestController로 쓰겠다. MemberController는 이제 jsp를 반환하는 경우는 없다. 모든 경우에 데이터를 반환하겠다.
public class MemberController {

  private final MemberService memberService;
  
  // 회원 등록 요청
  @RequestMapping(value="/members", method=RequestMethod.POST, produces="application/json")
  public Map<String, Object> registerMember(@RequestBody MemberDto memberDto, HttpServletResponse response){  // Dto랑 Map 중 Dto 선택  // 파라미터로 데이터가 오는 게 아니라, JSON 덩어리가 POST방식으로 본문에 포함돼서 전달되는 방식이다.
    return memberService.register(memberDto, response);
  }
  
  // 회원 목록 요청
  // 경로에 포함되어 있는 변수 {p}는 @PathVariable을 이용해서 가져올 수 있다.
  @RequestMapping(value="/members/page/{p}", method=RequestMethod.GET, produces="application/json")
  public Map<String, Object> getMembers(@PathVariable(value="p", required=false) Optional<String> opt) {    // (@PathVariable int page)로 해도 된다. 만약 여러 개의 경로가 온다면 사용 불가!
    int page = Integer.parseInt(opt.orElse("1"));     // null일 때, 1을 받는다.
    return memberService.getMembers(page);
  }
  
  // 회원 조회 요청
  @RequestMapping(value="/members/{mNo}", method=RequestMethod.GET, produces="application/json")
  public Map<String, Object> getMember(@PathVariable(value="mNo") int memberNo) {
    return memberService.getMember(memberNo);
  }
  
  // 회원 정보 수정 요청
  @RequestMapping(value="/members", method=RequestMethod.PUT, produces="application/json")
  public Map<String, Object> modifyMember(@RequestBody MemberDto memberDto){  // @RequestBody : index의 응답에서 어딨는지 알려주면 꺼내서 사용
    return memberService.modifyMember(memberDto);
  }
  
  // 회원 정보 삭제 요청
  @RequestMapping(value="/member/{memberNo}", method=RequestMethod.DELETE, produces="application/json")
  public Map<String, Object> removeMember(@PathVariable(value="memberNo") int memberNo) {
    return memberService.removeMember(memberNo);
  }
  
  // 회원들 정보 삭제 요청
  @RequestMapping(value="/members/{memberNoList}", method=RequestMethod.DELETE, produces="application/json")
  public Map<String, Object> removeMembers(@PathVariable(value="memberNoList") String memberNoList){
    return memberService.removeMembers(memberNoList);
  }
  
}
