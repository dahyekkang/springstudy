package com.gdu.myhome.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.myhome.dto.UploadDto;

public interface UploadService {
  
  public boolean addUpload(MultipartHttpServletRequest multipartRequest) throws Exception;  // 파일이어서 HttpServletRequest로 못 받는다.
  // ajax으로 목록 반환
  public Map<String, Object> getUploadList(HttpServletRequest request);   // page만 넘길거면 int로 해도 되는데 혹시 모르니 HttpServletRequest로
  public void loadUpload(HttpServletRequest request, Model model);
  
  public ResponseEntity<Resource> download(HttpServletRequest request);
  public ResponseEntity<Resource> downloadAll(HttpServletRequest request);
  
  public void removeTempFiles();
  
  public UploadDto getUpload(int uploadNo);
  public int modifyUpload(UploadDto upload);
  
  public Map<String, Object> getAttachList(HttpServletRequest request);
  
  public Map<String, Object> removeAttach(HttpServletRequest request);
  
  public Map<String, Object> addAttach(MultipartHttpServletRequest multipartRequest) throws Exception;
  
  public int removeUpload(int uploadNo);
}
