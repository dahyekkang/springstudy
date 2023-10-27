package com.gdu.myhome.service;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gdu.myhome.dao.BlogMapper;
import com.gdu.myhome.dto.BlogDto;
import com.gdu.myhome.dto.BlogImageDto;
import com.gdu.myhome.util.MyFileUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

  private final BlogMapper blogMapper;
  // 이미지 경로 제공하는 클래스
  private final MyFileUtils myFileUtils;
  
  @Override
  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest) {
    
    // 이미지가 저장될 경로
    String imagePath = myFileUtils.getBlogImagePath();
    File dir = new File(imagePath);
    if(!dir.exists()) {
      dir.mkdirs();
    }
    
    // 이미지 파일 (CKEditor는 이미지를 upload라는 이름으로 보냄)
    MultipartFile upload = multipartRequest.getFile("upload");
    
    // 이미지가 저장될 이름
    String originalFilename = upload.getOriginalFilename();
    String filesystemName = myFileUtils.getFilesystemName(originalFilename);
    
    // 이미지 File 객체
    File file = new File(dir, filesystemName);
    
    // 저장
    try {
      upload.transferTo(file);  // upload 파일 자체를 file의 경로와 이름으로 보내라.
    } catch(Exception e) {
      e.printStackTrace();
    }
    
    // CKEditor로 저장된 이미지의 경로를 JSON 형태로 반환해야 함
    return Map.of("uploaded", true,
                  "url", multipartRequest.getContextPath() + imagePath + "/" + filesystemName);
    // url: "http://localhost:8080/myhome/blog/2023/10/27/파일명"
    // servlet-context.xml에
    // /blog/** 주소 요청을 /blog 디렉터리로 연결하는 <resources> 태그를 추가해야 함
  }
  
  @Override
  public int addBlog(HttpServletRequest request) {
    
    // BLOG_T에 추가할 데이터
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    String ip = request.getRemoteAddr();
    
    // BlogDto 생성
    BlogDto blog = BlogDto.builder()
                      .title(title)
                      .contents(contents)
                      .userNo(userNo)
                      .ip(ip)
                      .build();
    // BLOG_T에 추가
    // BlogMapper의 insertBlog() 메소드를 실행하면
    // 메소드로 전달한 blog 객체에 blogNo 값이 저장된다.
    int addResult = blogMapper.insertBlog(blog);    // mapper에서 blogNo넣는 작업(selectKey태그)을 했기 때문에 blogNo가 포함된 BlogDto blog가 된다.
    
    // BLOG 작성 시 사용한 이미지 목록 (Jsoup 라이브러리 사용)
    Document document = Jsoup.parse(contents);
    Elements elements = document.getElementsByTag("img");   // js같은 코드를 사용 가능. img태그 뽑아내기
    
    if(elements != null) {
      for(Element element : elements) {
        String src = element.attr("src");  // jQuery같은 코드를 사용 가능
        // src 상태 : /myhome/blog/2023/10/27/aaaaa.jpg 여기서 파일 이름만 꺼내야 한다.
        String filesystemName = src.substring(src.lastIndexOf("/") + 1);    // 마지막 슬래시(/) 다음 글자부터 끝까지 가져오기(파일명)
        BlogImageDto blogImage = BlogImageDto.builder()
                                      .blogNo(blog.getBlogNo())    // 서비스 입장에선 blogNo를 모른다. blogNo를 다시 불러와서 max값을 구하든, mapper에서 넣어주어야 한다. mapper에서 넣었으므로 꺼낸다.
                                      .imagePath(myFileUtils.getBlogImagePath())
                                      .filesystemName(filesystemName)
                                      .build();
        blogMapper.insertBlogImage(blogImage);
      }
    }
    
    return addResult;
  }
  
  public void blogImageBatch() {
    
    // 1. 어제 작성된 블로그의 이미지 목록(DB)
    List<BlogImageDto> blogImageList = blogMapper.getBlogImageInYesterday();
    
    // 2. List<BlogImageDto> -> List<Path> (Path는 경로+파일명으로 구성)
    // stream : for문없이 하나씩. map : 모양을 바꿈. 하나씩 꺼낸 걸 리스트에 넣기
    // blogImageDto를 하나씩 꺼내서 /blog/2023/10/26/aaaa.jpg의 형태로 바꿔서
    
    List<Path> blogImagePathList = blogImageList.stream()
                                                .map(blogImageDto -> new File(blogImageDto.getImagePath(), blogImageDto.getFilesystemName()).toPath())
                                                .collect(Collectors.toList());
    
    // 3. 어제 저장된 블로그 이미지 목록 (디렉터리)
    File dir = new File(myFileUtils.getBlogImagePathInYesterday());
    
    // 4. 삭제할 File 객체들
    // 디렉터리에 저장된 모든 것들을 file이라고 하나씩 불러서 경로에 저장되어있는지 확인하겠다.
    File[] targets = dir.listFiles(file -> !blogImagePathList.contains(file.toPath()));
    
    // 5. 삭제
    if(targets != null && targets.length != 0) {
      for(File target : targets) {
        target.delete();
      }
    }
    
  }
  
}
