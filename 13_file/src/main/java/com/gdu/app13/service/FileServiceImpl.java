package com.gdu.app13.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.app13.util.MyFileUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service      // @Autowired 준비!
public class FileServiceImpl implements FileService {

  private final MyFileUtil myFileUtil;
  
  // form 태그 업로드
  @Override
  public int upload(MultipartHttpServletRequest multipartRequest) {
    
    // 첨부된 파일들의 목록
    List<MultipartFile> files = multipartRequest.getFiles("files");
    
    // 순회
    for(MultipartFile multipartFile : files) {
      
      // 첨부 여부 확인
      if(multipartFile != null && !multipartFile.isEmpty()) {
        
        try {
          
          // 첨부 파일의 저장될 경로 가져오기
          String path = myFileUtil.getPath();
          
          // 저장될 경로의 디렉터리 만들기
          File dir = new File(path);
          if(!dir.exists()) {
            dir.mkdirs();
          }
          
          // 첨부 파일의 원래 이름 알아내기
          String originalName = multipartFile.getOriginalFilename();
          
          // 첨부 파일이 저장될 이름 가져오기
          String filesystemName = myFileUtil.getFilesystemName(originalName);
          
          // 첨부 파일의 File 객체
          File file = new File(dir, filesystemName);
          
          // 첨부 파일 저장하기
          multipartFile.transferTo(file);   // 업로드할 multipartFile을 file로 복사해서 보내겠다.
          
        } catch (Exception e) {
          e.printStackTrace();
        }
        
      }
      
    }
    
    return 0;
  }
  
  
  // ajax 업로드
  // 요청만 달라졌을 뿐이지 코드 비슷하다. 반환값만 좀 달라짐 (ajax은 map을 반환한다.)
  @Override
  public Map<String, Object> ajaxUpload(MultipartHttpServletRequest multipartRequest) {
    
    // 첨부된 파일들의 목록
    List<MultipartFile> files = multipartRequest.getFiles("files");
    
    // 순회
    for(MultipartFile multipartFile : files) {
      
      // 첨부 여부 확인
      if(multipartFile != null && !multipartFile.isEmpty()) {
        
        try {
          
          // 첨부 파일의 저장될 경로 가져오기
          String path = myFileUtil.getPath();
          
          // 저장될 경로의 디렉터리 만들기
          File dir = new File(path);
          if(!dir.exists()) {
            dir.mkdirs();
          }
          
          // 첨부 파일의 원래 이름 알아내기
          String originalName = multipartFile.getOriginalFilename();
          
          // 첨부 파일이 저장될 이름 가져오기
          String filesystemName = myFileUtil.getFilesystemName(originalName);
          
          // 첨부 파일의 File 객체
          File file = new File(dir, filesystemName);
          
          // 첨부 파일 저장하기
          multipartFile.transferTo(file);   // 업로드할 multipartFile을 file로 복사해서 보내겠다.
          
        } catch (Exception e) {
          e.printStackTrace();
        }
        
      }
      
    }
      
    return Map.of("success", true);   // 무조건 성공을 반환시킨 것임
    
  }

}
