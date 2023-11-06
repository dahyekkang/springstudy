package com.gdu.myhome.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.myhome.dao.UploadMapper;
import com.gdu.myhome.dto.AttachDto;
import com.gdu.myhome.dto.UploadDto;
import com.gdu.myhome.dto.UserDto;
import com.gdu.myhome.util.MyFileUtils;
import com.gdu.myhome.util.MyPageUtils;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@Transactional
@RequiredArgsConstructor
@Service
public class UploadServiceImpl implements UploadService {

  private final UploadMapper uploadMapper;
  private final MyFileUtils myFileUtils;
  private final MyPageUtils myPageUtils;
  
  @Override
  public boolean addUpload(MultipartHttpServletRequest multipartRequest) throws Exception {
    
    String title = multipartRequest.getParameter("title");
    String contents = multipartRequest.getParameter("contents");
    int userNo = Integer.parseInt(multipartRequest.getParameter("userNo"));
    
    UploadDto upload = UploadDto.builder()
                            .title(title)
                            .contents(contents)
                            .userDto(UserDto.builder()
                                      .userNo(userNo)
                                      .build())
                            .build();
    
    int uploadCount = uploadMapper.insertUpload(upload);          // 이 위까지는 uploadNo가 들어있지 않았음
     
    List<MultipartFile> files = multipartRequest.getFiles("files");   // spring에서는 첨부된 파일을 MultipartFile이라고 부른다. 한 개면 getFile() 여러 개면 getFiles()사용
    
    // 첨부 없을 때 : [MultipartFile[field="files", filename=, contentType=application/octet-stream, size=0]]
    // 첨부 1개     : [MultipartFile[field="files", filename="animal1.jpg", contentType=image/jpeg, size=123456]]
    
    int attachCount;
    if(files.get(0).getSize() == 0) {   // 첨부가 없음 이름으로 확인 (getOriginalFilename().isEmpty())
      attachCount = 1;
    } else {
      attachCount = 0;
    }
    
    for(MultipartFile multipartFile : files) {
      if(multipartFile != null && !multipartFile.isEmpty()) {         // 첨부 파일이 있는지
        String path = myFileUtils.getUploadPath();
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String originalFilename = multipartFile.getOriginalFilename();
        String filesystemName = myFileUtils.getFilesystemName(originalFilename);
        File file = new File(dir, filesystemName);
        
        multipartFile.transferTo(file);   // 예외처리를 필요로 하므로 throws 사용
        
        String contentType = Files.probeContentType(file.toPath());  // 이미지의 Content-Type : image/jpg, image/png 등 image로 시작한다.
        int hasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;    // java에서 null 체크가 필요하면 조건문에서 먼저 쓴다!!!!!! (NullPointerException)
        
        if(hasThumbnail == 1) {
          // Thumbnailator jar파일 이용 (크기 줄이는 것 아님. 아예 사이즈가 작은 파일을 따로 준비한다. (ex)s_image1 / m_image1 / l_image1)
          File thumbnail = new File(dir, "s_" + filesystemName);  // small 이미지를 의미하는 s_을 덧붙임
          Thumbnails.of(file)           // 원본을
                    .size(100, 100)     // 가로 100px, 세로 100px
                    .toFile(thumbnail); // thumbnail로 제작
        }
        
        AttachDto attach = AttachDto.builder()
                              .path(path)
                              .originalFilename(originalFilename)
                              .filesystemName(filesystemName)
                              .hasThumbnail(hasThumbnail)
                              .uploadNo(upload.getUploadNo())
                              .build();
        
        attachCount += uploadMapper.insertAttach(attach);
        
        
      }   // if
      
    }     // for
    
    return (uploadCount == 1) && (files.size() == attachCount);
  
  }
  
  @Transactional(readOnly=true)
  @Override
  public Map<String, Object> getUploadList(HttpServletRequest request) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = uploadMapper.getUploadCount();
    int display = 9;    // 3 x 3 목록 만들기 위해
    
    myPageUtils.setPaging(page, total, display);  // begin, end 계산
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd()); 
    
    // 전달하고 목록 받기
    List<UploadDto> uploadList = uploadMapper.getUploadList(map);
   
    return Map.of("uploadList", uploadList
                , "totalPage", myPageUtils.getTotalPage());   // 전체 페이지 이후에도 가져오려고 할 수도 있어서 전체 페이지 수도 같이 보낸다.
  }
  
  @Transactional(readOnly=true)
  @Override
  public void loadUpload(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("uploadNo"));
    int uploadNo = Integer.parseInt(opt.orElse("0"));
    
    model.addAttribute("upload", uploadMapper.getUpload(uploadNo));
    model.addAttribute("attachList", uploadMapper.getAttachList(uploadNo));
    
    
  }
  
  @Override
  public ResponseEntity<Resource> download(HttpServletRequest request) {
    
    // 첨부 파일의 정보 가져오기
    int attachNo = Integer.parseInt(request.getParameter("attachNo"));
    AttachDto attach = uploadMapper.getAttach(attachNo);
    
    // 첨부 파일 File 객체 -> Resource 객체
    File file = new File(attach.getPath(), attach.getFilesystemName());
    Resource resource = new FileSystemResource(file);   // Resource화
    
    // 첨부 파일이 없으면 다운로드 취소
    if(!resource.exists()) {
      return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);  // 404
    }
    
    // 다운로드 횟수 증가하기
    uploadMapper.updateDownloadCount(attachNo);
    
    // 사용자가 다운로드 받을 파일의 이름 결정 (User-Agent값에 따른 인코딩 처리)
    String originalFilename = attach.getOriginalFilename();   // 그대로 내보내면 인코딩 깨질 수 있음
    String userAgent = request.getHeader("User-Agent");       // 요청헤더.
    
    // 인코딩 시 try-catch문 필요
    try {
      // IE (InternetExplorer)
      if(userAgent.contains("Trident")) {
        originalFilename = URLEncoder.encode(originalFilename, "UTF-8").replace("+", " ");    // 공백이 "+"로 바뀌므로 다시 변경해준다.
      }
      // Edge
      else if(userAgent.contains("Edg")) {
        originalFilename = URLEncoder.encode(originalFilename, "UTF-8");
      }
      // Other
      else {
        originalFilename = new String(originalFilename.getBytes("UTF-8"), "ISO-8859-1");
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    
    // 다운로드 응답 헤더 만들기
    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "application/octet-stream");   // 이진 binary 데이터- 여기서 content-type을 명시했기 때문에 produces 작업하지 않아도 된다.
    header.add("Content-Disposition", "attachment; filename=" + originalFilename);   // attachment;는 고정
    header.add("Content-Length", file.length() + "");    //파일의 크기를 문자열로 변경
    
    // 응답
    return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    
  }
  
  @Override
  public ResponseEntity<Resource> downloadAll(HttpServletRequest request) {
    
    // 다운로드할 모든 첨부파일 정보 가져오기
    int uploadNo = Integer.parseInt(request.getParameter("uploadNo"));
    List<AttachDto> attachList = uploadMapper.getAttachList(uploadNo);
    
    // 첨부파일이 없으면 종료
    if(attachList.isEmpty()) {
      return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
    }
    
    // zip 파일을 생성할 경로
    File tempDir = new File(myFileUtils.getTempPath());
    if(!tempDir.exists()) {
      tempDir.mkdirs();
    }
    
    // zip 파일의 이름
    String zipName = myFileUtils.getTempFilename() + ".zip";
    
    // zip 파일의 File 객체
    File zipFile = new File(tempDir, zipName);
    // temp와 관련된 준비 끝

    // zip 파일을 생성하는 출력 스트림
    ZipOutputStream zout = null;
    
    // 첨부 파일들을 순회하면서 zip 파일에 등록하기
    try {
      
      zout = new ZipOutputStream(new FileOutputStream(zipFile));
      
      for(AttachDto attach : attachList) {
        
        // 각 첨부파일들의 원래 이름으로 zip 파일에 등록하기 (이름만 등록)
        ZipEntry zipEntry = new ZipEntry(attach.getOriginalFilename());   // java.util 패키지에 있음
        zout.putNextEntry(zipEntry);
        
        // 각 첨부파일들의 내용을 zip 파일에 등록하기 (실제 파일 등록)
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(new File(attach.getPath(), attach.getFilesystemName())));
        zout.write(bin.readAllBytes());
        
        // 자원 반납
        bin.close();
        zout.closeEntry();
        
        // 다운로드 횟수 증가
        uploadMapper.updateDownloadCount(attach.getAttachNo());
      }
      
      // zout 자원 반납
      zout.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    // 다운로드할 zip 파일의 File 객체 -> Resource 객체
    Resource resource = new FileSystemResource(zipFile);
    
    
    // 다운로드 응답 헤더 만들기
    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "application/octet-stream");   // 이진 binary 데이터- 여기서 content-type을 명시했기 때문에 produces 작업하지 않아도 된다.
    header.add("Content-Disposition", "attachment; filename=" + zipName);   // attachment;는 고정
    header.add("Content-Length", zipFile.length() + "");    //파일의 크기를 문자열로 변경
    
    // 응답
    return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

  }
  
  @Override
  public void removeTempFiles() {
    File tempDir = new File(myFileUtils.getTempPath());
    File[] targetList = tempDir.listFiles();
    if(targetList != null) {
      for(File target : targetList) {
        target.delete();
      }
    }
  }
  
  @Transactional(readOnly=true)
  @Override
  public UploadDto getUpload(int uploadNo) {
    return uploadMapper.getUpload(uploadNo);
  }
  
  @Override
  public int modifyUpload(UploadDto upload) {
    return uploadMapper.updateUpload(upload);
  }
  
  @Override
  public Map<String, Object> getAttachList(HttpServletRequest request) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("uploadNo"));
    int uploadNo = Integer.parseInt(opt.orElse("0"));
    
    return Map.of("attachList", uploadMapper.getAttachList(uploadNo));
  }
  
  @Override
  public Map<String, Object> removeAttach(HttpServletRequest request) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("attachNo"));
    int attachNo = Integer.parseInt(opt.orElse("0"));
    
    // 파일 삭제
    AttachDto attach = uploadMapper.getAttach(attachNo);
    File file = new File(attach.getPath(), attach.getFilesystemName());   // 저장된 경로, 이름
    if(file.exists()) {
      file.delete();
    }
    
    // 썸네일 삭제
    if(attach.getHasThumbnail() == 1) {
      File thumbnail = new File(attach.getPath(),  "s_" + attach.getFilesystemName());
      if(thumbnail.exists()) {
        thumbnail.delete();
      }      
    }
    
    // ATTACH_T에서 삭제
    int removeResult = uploadMapper.deleteAttach(attachNo);
    
    return Map.of("removeResult", removeResult);
  }
  
  @Override
  public Map<String, Object> addAttach(MultipartHttpServletRequest multipartRequest) throws Exception {
    
    // 폼에 어쩌구 주석 못 씀
    List<MultipartFile> files = multipartRequest.getFiles("files");
    
    int attachCount;
    if(files.get(0).getSize() == 0) {   // 첨부가 없음 이름으로 확인 (getOriginalFilename().isEmpty())
      attachCount = 1;
    } else {
      attachCount = 0;
    }
    
    for(MultipartFile multipartFile : files) {
      if(multipartFile != null && !multipartFile.isEmpty()) {         // 첨부 파일이 있는지
        String path = myFileUtils.getUploadPath();
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String originalFilename = multipartFile.getOriginalFilename();
        String filesystemName = myFileUtils.getFilesystemName(originalFilename);
        File file = new File(dir, filesystemName);
        
        multipartFile.transferTo(file);   // 예외처리를 필요로 하므로 throws 사용
        
        String contentType = Files.probeContentType(file.toPath());  // 이미지의 Content-Type : image/jpg, image/png 등 image로 시작한다.
        int hasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;    // java에서 null 체크가 필요하면 조건문에서 먼저 쓴다!!!!!! (NullPointerException)
        
        if(hasThumbnail == 1) {
          // Thumbnailator jar파일 이용 (크기 줄이는 것 아님. 아예 사이즈가 작은 파일을 따로 준비한다. (ex)s_image1 / m_image1 / l_image1)
          File thumbnail = new File(dir, "s_" + filesystemName);  // small 이미지를 의미하는 s_을 덧붙임
          Thumbnails.of(file)           // 원본을
                    .size(100, 100)     // 가로 100px, 세로 100px
                    .toFile(thumbnail); // thumbnail로 제작
        }
        
        AttachDto attach = AttachDto.builder()
                              .path(path)
                              .originalFilename(originalFilename)
                              .filesystemName(filesystemName)
                              .hasThumbnail(hasThumbnail)
                              .uploadNo(Integer.parseInt(multipartRequest.getParameter("uploadNo")))
                              .build();
        
        attachCount += uploadMapper.insertAttach(attach);
        
        
      }   // if
      
    }     // for
    
    return Map.of("attachResult", files.size() == attachCount);
  }
  
  @Override
  public int removeUpload(int uploadNo) {
    
    // 파일 삭제
    List<AttachDto> attachList = uploadMapper.getAttachList(uploadNo);
    for(AttachDto attach : attachList) {
      File file = new File(attach.getPath(), attach.getFilesystemName());   // 저장된 경로, 이름
      if(file.exists()) {
        file.delete();
      }
      
      // 썸네일 삭제
      if(attach.getHasThumbnail() == 1) {
        File thumbnail = new File(attach.getPath(),  "s_" + attach.getFilesystemName());
        if(thumbnail.exists()) {
          thumbnail.delete();
        }      
      }
    }
    
    // UPLOAD_T 삭제
    return uploadMapper.deleteUpload(uploadNo);
  }
  
}
