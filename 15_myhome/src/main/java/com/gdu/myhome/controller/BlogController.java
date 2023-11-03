package com.gdu.myhome.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.myhome.dto.BlogDto;
import com.gdu.myhome.service.BlogService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/blog")
@RequiredArgsConstructor
@Controller
public class BlogController {

  private final BlogService blogService;
  
  @GetMapping("/list.do")
  public String list(HttpServletRequest request, Model model) {
    blogService.loadBlogList(request, model);
    return "blog/list";
  }
  
  @GetMapping("/write.form")
  public String write() {
    return "blog/write";
  }
  
  // 이미지 받아서 저장 시키고 저장시킨 걸 반환해야 한다. json파일로!
  @ResponseBody     // produces 나오면 세트로 나온다!
  @PostMapping(value="/imageUpload.do", produces="application/json")   // 첨부에 관련된 건 전부 Post 사용(주소창에 파라미터로 X)
  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest) { // HttpServletRequest는 글자 아닌 것을 못 받아서.
    return blogService.imageUpload(multipartRequest);
  }
  
  @PostMapping("/addBlog.do")
  public String addBlog(HttpServletRequest request, RedirectAttributes redirectAttributes) {   // 파일 첨부는 이미 완료됐고, 이미지를 표시하기 위한 이미지 태그 형태로 넘어가는 거라서 단순 텍스트 넘기기 하면 됨
    int addResult = blogService.addBlog(request);
    redirectAttributes.addFlashAttribute("addResult", addResult);
    return "redirect:/blog/list.do";
  }
  
  @GetMapping("/increaseHit.do")
  public String increaseHit(@RequestParam(value="blogNo", required=false, defaultValue="0") int blogNo) {
    int increaseResult = blogService.increaseHit(blogNo);
    if(increaseResult == 1) {
      return "redirect:/blog/detail.do?blogNo=" + blogNo;
    } else {
      return "redirect:/blog/list.do";
    }
  }
  
  @GetMapping("/detail.do")
  public String detail(@RequestParam(value="blogNo", required=false, defaultValue="0") int blogNo
                     , Model model) {
    BlogDto blog = blogService.getBlog(blogNo);
    model.addAttribute("blog", blog);
    return "blog/detail";
  }
  
  // DB에 안 가서 성능이 좋아진다!
  // model에 저장할 필요가 없다. 이미 저장돼있다. (커맨드 객체를 자동으로 뷰까지 전달한다. 별도로 model.addAttribute를 할 필요가 없다.
  // 모델에 실어줄 때, blog라는 이름으로 실어줘라!
  @PostMapping("/edit.form")
  public String edit(@ModelAttribute("blog") BlogDto blog) {   // BlogDto나 request로 받을 수 있다.
    return "blog/edit";
  }
  
  @PostMapping("/modifyBlog.do")
  public String modifyBlog(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int modifyResult = blogService.modifyBlog(request);
    redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
    return "redirect:/blog/detail.do?blogNo=" + request.getParameter("blogNo");
  }
  
  @PostMapping("/remove.do")
  public String remove(@RequestParam(value="blogNo", required=false, defaultValue="0") int blogNo
                     , RedirectAttributes redirectAttributes) {
    int removeResult = blogService.removeBlog(blogNo);
    redirectAttributes.addFlashAttribute("removeResult", removeResult);
    return "redirect:/blog/list.do";
  }

  // 댓글
  @ResponseBody
  @PostMapping(value="/addComment.do", produces="application/json")
  public Map<String, Object> addComment(HttpServletRequest request) {
    return blogService.addComment(request);
  }
  
  @ResponseBody
  @GetMapping(value="/commentList.do", produces="application/json")
  public Map<String, Object> commentList(HttpServletRequest request) {
    return blogService.loadCommentList(request);
  }
  
  // 답글
  @ResponseBody
  @PostMapping(value="/addCommentReply.do", produces="application/json")
  public Map<String, Object> addCommentReply(HttpServletRequest request) {
    return blogService.addCommentReply(request);
  }
  
  @ResponseBody
  @PostMapping(value="/removeComment.do", produces="application/json")
    public Map<String, Object> removeComment(@RequestParam(value="commentNo", required=false, defaultValue="0") int commentNo) {
      return blogService.removeComment(commentNo);
    }
  
}
