package com.gdu.app06.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.app06.service.IBoardService;

@RequestMapping("/board")
@Controller
public class BoardController {

  private IBoardService iBoardService;

  @Autowired
  public void setiBoardService(IBoardService iBoardService) {
    this.iBoardService = iBoardService;
  }
  
  @RequestMapping(value="/list.do", method=RequestMethod.GET)  // SELECT 돌리면? GET이다.
  public String list(Model model) {
    model.addAttribute("boardList", iBoardService.getBoardList());    // getBoardList를 호출하면 목록 3개가 나오는데 list.jsp로 전달해야 한다.
    return "board/list";    // /WEB-INF/views/board/list.jsp
  }
  
  @RequestMapping(value="/detail.do", method=RequestMethod.GET)
  public String detail(@RequestParam(value="boardNo", required=false, defaultValue="0") int boardNo   // 필수가 아니므로, 값이 없어도 오류 나오지 않게하라. 없으면 0을 써라
                             , Model model) {   // 상세보기 할 boardNo를 넘겨줘야 하니까.
      model.addAttribute("board", iBoardService.getBoardByNo(boardNo));
      return "board/detail";  // /WEB-INF/views/board/detail.jsp
    }
  
  
}
