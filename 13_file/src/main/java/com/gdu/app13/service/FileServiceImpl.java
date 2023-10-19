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
  
  @Override
  public Map<String, Object> ckeditorUpload(MultipartFile upload, String contextPath) {
    
    // 이미지 저장할 경로
    String path = myFileUtil.getPath();
    File dir = new File(path);
    if(!dir.exists()) {
      dir.mkdirs();
    }
    
    // 이미지 저장할 이름 (원래 이름 + 저장할 이름)
    String originalFilename = upload.getOriginalFilename();
    String filesystemName = myFileUtil.getFilesystemName(originalFilename);   // 최종적으로 저장할 파일 이름
    
    // 이미지 File 객체
    File file =  new File(dir, filesystemName);
    
    // File 객체를 참고하여, MultipartFile upload 첨부 이미지 저장
    try {
      upload.transferTo(file);    // 업로드를 파일객체 정보(dir, filesystemName)로 보내라.
    } catch(Exception e) {
      e.printStackTrace();
    }
    
    // CKEditor로 저장된 이미지를 확인할 수 있는 경로를 {"url": "http://locacalhost:8080/app13/..."} 방식으로 반환해야 함
    
    System.out.println(contextPath + path + "/" + filesystemName);
    
    return Map.of("url", contextPath + path + "/" + filesystemName
                , "uploaded", true);   // contextPath가 필요하다! contextPath는 request가 제공. request 선언할 수 있는 곳 Controller.
    
    /*
     * CKEditor로 반환할 url
     *    http://localhost:8080/app13/storage/2023/10/18/3cb8723ddd9843c382abd17e0dffa49d.jpg
     * 
     * servlet-context.xml에
     *    /storage/** 주소로 요청을 하면    /storage/ 디렉터리의 내용을 보여주는 <resources>
     * 
     */
  }

}
