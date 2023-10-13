package com.gdu.app13.controller;


import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gdu.app13.service.FileService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FileController {
  
  private final FileService fileService;

  // form 태그 업로드
  @RequestMapping(value="/upload.do", method=RequestMethod.POST)
  public String upload(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) {    // 파일이 첨부돼있는 경우(type=file)엔 HttpServletRequest를 사용할 수 없다.
    
    // 값을 service한테 넘겨서 service가 upload를 수행할 수 있게 해주면 된다.
    int addResult = fileService.upload(multipartRequest);
    redirectAttributes.addFlashAttribute("addResult", addResult);
    return "redirect:/main.do";
  }
  
  // ajax 업로드
  @RequestMapping(value="/ajax/upload.do", method=RequestMethod.POST, produces="application/json")    // Encoding은 안 함! result가 0아니면 1이어서~
  @ResponseBody   // ajax처리하는 컨트롤러에 써주는 annotation. 이걸 안 붙이고 싶으면 두 가지 방법이 있음. @Controller를 RestController로 바꿀 것! 아니면 ResponseEntitiy를 쓰는 것도 있다! springstudy 7,8장 다시 공부하셈
  // {"success":true} json 보낼 거면 객체, Map 중에   Map  을 사용한다 !
  // []               배열 보낼거면                   List 를 사용한다 !
  // Map이나 List를 준비해서 보내주기만 하면 Jackson이 자동으로 변환해준다~
  public Map<String, Object> ajaxUpload(MultipartHttpServletRequest multipartRequest) {   // redirect하지 않는다. 성공, 실패의 data만 ajax으로 보내주는 것임
    return fileService.ajaxUpload(multipartRequest);
  }
  
}
