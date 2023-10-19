package com.gdu.app13.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface FileService {
  public int upload(MultipartHttpServletRequest multipartRequest);
  public Map<String, Object> ajaxUpload(MultipartHttpServletRequest multipartRequest);
  public Map<String, Object> ckeditorUpload(MultipartFile upload, String contextPath);
}
