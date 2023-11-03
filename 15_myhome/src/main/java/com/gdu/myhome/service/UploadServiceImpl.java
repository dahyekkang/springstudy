package com.gdu.myhome.service;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
                    .toFile(thumbnail); // thumbnail로  제작
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
  
}
