package com.gdu.myhome.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.myhome.service.BlogService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/blog")
@Controller
public class BlogController {

  private final BlogService blogService;
  
  @GetMapping("/list.do")
  public String list(HttpServletRequest request, Model model) {
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
}
