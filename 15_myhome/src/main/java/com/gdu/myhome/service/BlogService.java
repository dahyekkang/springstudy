package com.gdu.myhome.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.myhome.dto.BlogDto;

public interface BlogService {

  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest);
  public int addBlog(HttpServletRequest request);
  public void blogImageBatch();
  public void loadBlogList(HttpServletRequest request, Model model);   // 반환할 게 많아서 반환을 안 하는 방식으로 처리하고 model에 전달
  // 상세보기(조회수 증가(redirect) + 상세조회(forward) 분리해서 하기!)
  public int increaseHit(int blogNo);
  public BlogDto getBlog(int blogNo);
  public int modifyBlog(HttpServletRequest request);
  public int removeBlog(int blogNo);
  
  public Map<String, Object> addComment(HttpServletRequest request);
  public Map<String, Object> loadCommentList(HttpServletRequest request);    // ajax은 페이지 이동없이 데이터만 주고 받으므로 model이 필요없다!
  public Map<String, Object> addCommentReply(HttpServletRequest request);
  
  public Map<String, Object> removeComment(int commentNo);
}
