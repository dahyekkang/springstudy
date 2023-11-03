package com.gdu.myhome.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface UploadService {
  public boolean addUpload(MultipartHttpServletRequest multipartRequest) throws Exception;  // 파일이어서 HttpServletRequest로 못 받는다.
  // ajax으로 목록 반환
  public Map<String, Object> getUploadList(HttpServletRequest request);   // page만 넘길거면 int로 해도 되는데 혹시 모르니 HttpServletRequest로
}
