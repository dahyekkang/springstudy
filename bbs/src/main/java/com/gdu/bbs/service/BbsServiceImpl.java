package com.gdu.bbs.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.gdu.bbs.dao.BbsMapper;
import com.gdu.bbs.dto.BbsDto;
import com.gdu.bbs.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BbsServiceImpl implements BbsService {

  private final BbsMapper bbsMapper;        // service는 언제나 Mapper를 사용해야 한다.
  private final MyPageUtils myPageUtils;
  
  @Override
  public void loadBbsList(HttpServletRequest request, Model model) {
    // page번호, contextPath 받기 위해 request 사용
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));   // request를 사용하는 이유1. page번호 null일 때 처리하기 위해
    String strPage = opt.orElse("1");    // null이면 대신 1 사용
    int page = Integer.parseInt(strPage);
    
    int total = bbsMapper.getBbsCount(); 
    
    int display = 10;
        
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    // map전달하고 list 받아오기
    List<BbsDto> bbsList = bbsMapper.getBbsList(map);
    
    model.addAttribute("bbsList", bbsList);   // model에 저장1
    
    String contextPath = request.getContextPath();  // request를 사용하는 이유2
    model.addAttribute("paging", myPageUtils.getMvcPaging(contextPath + "/bbs/list.do"));   // model에 저장2
    model.addAttribute("total", total);   // model에 저장3
    
    // model에 저장한 건 jsp에선 el로 접근 ${total}
    
  }
  
  @Override
  public BbsDto getBbs(int bbsNo) {
    BbsDto bbs = bbsMapper.getBbs(bbsNo);
    return bbs;
  }
  
  @Override
  public int addBbs(BbsDto bbs) {
    return bbsMapper.insertBbs(bbs); 
  }
  
  @Override
  public int modifyBbs(BbsDto bbs) {
    return bbsMapper.updateBbs(bbs);
  }
  
  @Override
  public int removeBbs(int BbsNo) {
    return bbsMapper.deleteBbs(BbsNo);
  }
  
}
