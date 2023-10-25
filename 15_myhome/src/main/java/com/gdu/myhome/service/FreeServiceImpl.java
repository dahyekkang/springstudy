package com.gdu.myhome.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.gdu.myhome.dao.FreeMapper;
import com.gdu.myhome.dao.UserMapper;
import com.gdu.myhome.dto.FreeDto;
import com.gdu.myhome.util.MyPageUtils;
import com.gdu.myhome.util.MySecurityUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FreeServiceImpl implements FreeService {

  private final FreeMapper freeMapper;
  private final MyPageUtils myPageUtils;
  private final MySecurityUtils mySecurityUtils;
  
  @Override
  public int addFree(HttpServletRequest request) {
    
    // 작성화면에서 넘어오는 data는 email, contents 두 개뿐이다.
    String email = request.getParameter("email");
    String contents = mySecurityUtils.preventXSS(request.getParameter("contents"));
    
    FreeDto free = FreeDto.builder()      // free 생성
                    .email(email)
                    .contents(contents)
                    .build();
    return freeMapper.insertFree(free);   // 0 or 1 반환
  }
  
  @Override
  public void loadFreeList(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));   // 페이지 번호 null처리
    int page = Integer.parseInt(opt.orElse("1"));                               // null이면 1페이지
    
    int display = 10;     // 한 화면에 몇 개씩 보여줄지
    
    int total = freeMapper.getFreeCount();    // 총 게시글 개수
    
    myPageUtils.setPaging(page, total, display);    // 이거 해야 freeMapper의 getFreeList에서 begin과 end값을 구할 수 있음
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin(), 
                                     "end", myPageUtils.getEnd());
    
    List<FreeDto> freeList = freeMapper.getFreeList(map);
    
    model.addAttribute("freeList", freeList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/free/list.do"));  // 여기 있는 모든 번호는 이동하는 경로가 /free/list.do?page=1 이렇게 생겼고 페이지번호만 바뀜
    model.addAttribute("beginNo", total - (page - 1) * display);  // 21-(2-1)*10 = 11 : 2페이지 시작값
  }
  
}
