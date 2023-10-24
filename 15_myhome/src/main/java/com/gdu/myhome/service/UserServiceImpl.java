package com.gdu.myhome.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.myhome.dao.UserMapper;
import com.gdu.myhome.dto.InactiveUserDto;
import com.gdu.myhome.dto.UserDto;
import com.gdu.myhome.util.MyJavaMailUtils;
import com.gdu.myhome.util.MySecurityUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;
  private final MySecurityUtils mySecurityUtils;      // 암호화할 때 필요한 클래스
  private final MyJavaMailUtils myJavaMailUtils;
  
  private final String client_id = "Y3_YnXvT1PIn8b0oRnG9";
  private final String client_secret = "dNU2sXe73m";
  
  @Override
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {   // 직접 응답하겠다. 컨트롤러 응답 X 서비스가 직접 응답. 주로 응답이 여러가지일 때 사용!
    
    String email = request.getParameter("email");
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    
    Map<String, Object> map = Map.of("email", email
                                   , "pw", pw);
    
    HttpSession session = request.getSession();
    
    // 휴면 계정인지 확인하기
    InactiveUserDto inactiveUser = userMapper.getInactiveUser(map);
    if(inactiveUser != null) {    // 휴면 테이블에 있는 사람(휴면 계정)
      // 정보저장 (inactiveUser)
      session.setAttribute("inactiveUser", inactiveUser);
      // 이동 (/user/active.form) -> user/active.jsp
      response.sendRedirect(request.getContextPath() + "/user/active.form");
      
    }
    
    UserDto user = userMapper.getUser(map);
    
    if(user != null) {
      
      request.getSession().setAttribute("user", user);
      userMapper.insertAccess(email);
      // insert, update, delete 이후엔 redirect! 반환타입이 없어서 컨트롤러를 통한 이동이 아님.
      response.sendRedirect(request.getParameter("referer"));  // try-catch가 필요한 구문 -> throws 사용   // redirect
    } else {
      response.setContentType("text/html; charset=UTF-8");    // 그런 이메일 없다 -> alert창 띄우려면 script할 것이다.
      PrintWriter out = response.getWriter();
      out.println("<script>");
      out.println("alert('일치하는 회원 정보가 없습니다.')");
      // out.println("history.back()");    // 다시 로그인 페이지로 돌아간다. 안 쓰는 게 좋다. 그냥 어디로 갈 지를 정해주는 게 나음
      out.println("location.href='" + request.getContextPath() + "/main.do'");
      out.println("</script>");
      out.flush();
      out.close();
    }
  }
  
  @Override
  public String getNaverLoginURL(HttpServletRequest request) throws Exception {
    
    // 네이버로그인-1
    // 네이버 로그인 연동 URL을 생성하기 위해 redirect_uri(URLEncoder), state(SecureRandom) 값의 전달이 필요하다.
    // redirect_uri : 네이버로그인-2를 처리할 서버 경로를 작성한다.
    // redirect_uri 값은 네이버 로그인 Callback URL에도 동일하게 등록해야 한다.
    
    String apiURL = "https://nid.naver.com/oauth2.0/authorize";
    String response_type = "code";
    String redirect_uri = URLEncoder.encode("http://localhost:8080" + request.getContextPath() + "/user/naver/getAccessToken.do", "UTF-8");   // 예외 처리 필요 -> throws Exceptio으로 처리
    String state = new BigInteger(130, new SecureRandom()).toString();        // session에 보관하기 위해 변수 처리
    
    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?response_type=").append(response_type);
    sb.append("&client_id=").append(client_id);
    sb.append("&redirect_uri=").append(redirect_uri);
    sb.append("&state=").append(state);
    
    request.getSession().setAttribute("state", state);                        // session에 저장
    
    return sb.toString();
  }
  
  @Override
  public String getNaverLoginAccessToken(HttpServletRequest request) throws Exception {
    // 네이버로그인-2
    // 접근 토큰 발급 요청
    // 네이버로그인-2를 수행하기 위해서는 네이버로그인-1의 응답 결과인 code와 state가 필요하다.
    
    // 네이버로그인-1의 응답 결과
    String code = request.getParameter("code");
    String state = request.getParameter("state");
    
    String apiURL = "https://nid.naver.com/oauth2.0/token";   // 네이버에서 접근토큰발급 요청 URL 가져와야 한다.
    String grant_type = "authorization_code";                 // access_token 발급 받을 때 사용하는 값(갱신이나 삭제 시에는 다른 값을 사용함)
    
    StringBuilder sb = new StringBuilder();
    
    sb.append(apiURL);
    sb.append("?grant_type=").append(grant_type);
    sb.append("&client_id=").append(client_id);
    sb.append("&client_secret=").append(client_secret);
    sb.append("&code=").append(code);
    sb.append("&state=").append(state);
    
    // 요청
    URL url = new URL(sb.toString());
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setRequestMethod("GET");    // 반드시 대문자로 작성
    
    // 응답
    BufferedReader reader = null;
    int responseCode = con.getResponseCode();
    if(responseCode == 200) {
      reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
    } else {
      reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));      
    }
    
    String line = null;
    StringBuilder responseBody = new StringBuilder();
    while((line = reader.readLine()) != null) {
      responseBody.append(line);      // 한 줄씩 StringBuilder에 추가
    }
    
    JSONObject obj = new JSONObject(responseBody.toString());   // NAVER가 제공해준 accessToken(JSON) -> JSON PARSING하는 LIBRARY 필요 - JSON In JAVA !
    return obj.getString("access_token");
  }
  
  @Override
  public UserDto getNaverProfile(String accessToken) throws Exception {

    // 네이버로그인-3
    // 접근 토큰을 전달한 뒤 해당 사용자의 프로필 정보(이름, 이메일, 성별, 휴대전화번호) 받아오기
    // 요청 헤더에 Authorization: Bearer accessToken 정보를 저장하고 요청함
    
    // 요청
    String apiURL = "https://openapi.naver.com/v1/nid/me";
    URL url = new URL(apiURL);
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setRequestMethod("GET");     // 방식은 GET/POST 둘다 되는데 GET으로 하겠다.
    con.setRequestProperty("Authorization", "Bearer " + accessToken);
    
    // 응답
    BufferedReader reader = null;
    int responseCode = con.getResponseCode();
    if(responseCode == 200) {
      reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
    } else {
      reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));      
    }
    
    String line = null;
    StringBuilder responseBody = new StringBuilder();
    while((line = reader.readLine()) != null) {
      responseBody.append(line);      // 한 줄씩 StringBuilder에 추가
    }
    
    // 응답 결과(프로필을 JSON으로 응답) -> UserDto 객체
    JSONObject obj = new JSONObject(responseBody.toString());
    JSONObject response = obj.getJSONObject("response");
    UserDto user = UserDto.builder()
                    .email(response.getString("email"))
                    .name(response.getString("name"))
                    .gender(response.getString("gender"))
                    .mobile(response.getString("mobile"))
                    .build();

    return user;

  }
  
  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    
    HttpSession session = request.getSession();
    
    session.invalidate();   // session 초기화 (session 비우는 것)
    
    try {
      response.sendRedirect(request.getContextPath() + "/main.do");  // try-catch가 필요한 구문   // redirect
    } catch(Exception e) {
      e.printStackTrace();
    }
    
  }
  
  @Transactional(readOnly=true)   // DB 수정이 없기 때문에 성능 향상을 위해 붙여준다.
  @Override
  public ResponseEntity<Map<String, Object>> checkEmail(String email) {
    
    Map<String, Object> map = Map.of("email", email);
    
    boolean enableEmail = userMapper.getUser(map) == null     // 셋 다 null이면 사용 가능한 이메일
                       && userMapper.getLeaveUser(map) == null
                       && userMapper.getInactiveUser(map) == null;
    
    return new ResponseEntity<>(Map.of("enableEmail", enableEmail), HttpStatus.OK);  // ResponseEntity<Map<String,Object>>에서 Map<String,Object>는 생략 가능
  }
  
  
  @Override
  public ResponseEntity<Map<String, Object>> sendCode(String email) {
    
    // RandomString 생성(6자리 문자+숫자 조합의 문자열)
    String code = mySecurityUtils.getRandomString(6, true, true);
    
    // 메일 전송
    myJavaMailUtils.sendJavaMail(email
                              , "myhome 인증 코드"
                              , "<div>인증코드는 <strong>" + code + "</strong>입니다.</div>");
    return new ResponseEntity<>(Map.of("code", code), HttpStatus.OK);
 
  }
  
  @Override
  public void join(HttpServletRequest request, HttpServletResponse response) {
    
    String email = request.getParameter("email");
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    String name = mySecurityUtils.preventXSS(request.getParameter("name"));
    String gender = request.getParameter("gender");
    String mobile = request.getParameter("mobile");
    String postcode = request.getParameter("postcode");
    String roadAddress = request.getParameter("roadAddress");
    String jibunAddress = request.getParameter("jibunAddress");
    String detailAddress = mySecurityUtils.preventXSS(request.getParameter("detailAddress"));
    String event = request.getParameter("event");
    
    UserDto user = UserDto.builder()
                    .email(email)
                    .pw(pw)
                    .name(name)
                    .gender(gender)
                    .mobile(mobile)
                    .postcode(postcode)
                    .roadAddress(roadAddress)
                    .jibunAddress(jibunAddress)
                    .detailAddress(detailAddress)
                    .agree(event.equals("on") ? 1 : 0)
                    .build();
    
    int joinResult = userMapper.insertUser(user);
    
    try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(joinResult == 1) {
        request.getSession().setAttribute("user", userMapper.getUser(Map.of("email", email)));
        userMapper.insertAccess(email);
        out.println("alert('회원 가입되었습니다.')");
        out.println("location.href='" + request.getContextPath() + "/main.do'");   // location.href는 redirect랑 같다.
      } else {
        out.println("alert('회원 가입을 실패했습니다.')");
        out.println("history.go(-2)");    // 두 단계 이전의 페이지로 이동
      }
      
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch(Exception e) {
      e.printStackTrace();
    }
    
  }
  
  @Override
  public ResponseEntity<Map<String, Object>> modify(HttpServletRequest request) {
    
    String name = mySecurityUtils.preventXSS(request.getParameter("name"));
    String gender = request.getParameter("gender");
    String mobile = request.getParameter("mobile");
    String postcode = request.getParameter("postcode");
    String roadAddress = request.getParameter("roadAddress");
    String jibunAddress = request.getParameter("jibunAddress");
    String detailAddress = mySecurityUtils.preventXSS(request.getParameter("detailAddress"));
    String event = request.getParameter("event");
    int agree = event.equals("on") ? 1 : 0;
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    
    UserDto user = UserDto.builder()
                          .name(name)
                          .gender(gender)
                          .mobile(mobile)
                          .postcode(postcode)
                          .roadAddress(roadAddress)
                          .jibunAddress(jibunAddress)
                          .detailAddress(detailAddress)
                          .agree(agree)
                          .userNo(userNo)
                          .build();
    
    int modifyResult = userMapper.updateUser(user);
    
    if(modifyResult == 1) {
      // session의 정보를 수정할 필요가 있다!
      HttpSession session = request.getSession();
      UserDto sessionUser = (UserDto)session.getAttribute("user");
      sessionUser.setName(name);
      sessionUser.setGender(gender);
      sessionUser.setMobile(mobile);
      sessionUser.setPostcode(postcode);
      sessionUser.setRoadAddress(roadAddress);
      sessionUser.setJibunAddress(jibunAddress);
      sessionUser.setDetailAddress(detailAddress);
      sessionUser.setAgree(agree);
    }
    
    return new ResponseEntity<>(Map.of("modifyResult", modifyResult), HttpStatus.OK);
    
  }
  
  
  @Override
  public void modifyPw(HttpServletRequest request, HttpServletResponse response) {
    
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    
    UserDto user = UserDto.builder()
                    .pw(pw)
                    .userNo(userNo)
                    .build();
    
    int modifyPwResult = userMapper.updateUserPw(user);
    
    try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(modifyPwResult == 1) {
        HttpSession session = request.getSession();
        UserDto sessionUser = (UserDto)session.getAttribute("user");
        sessionUser.setPw(pw);
        out.println("alert('비밀번호가 수정되었습니다.')");
        out.println("location.href='" + request.getContextPath() + "/user/mypage.form'");
      } else {
        out.println("alert('비밀번호가 수정되지 않았습니다.')");
        out.println("history.back()");
      }
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  @Override
  public void leave(HttpServletRequest request, HttpServletResponse response) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("userNo"));   // 만약 세션이 만료된 상태로 탈퇴 버튼을 누르면 userNo가 오지 않는다.
    int userNo = Integer.parseInt(opt.orElse("0"));
    
    UserDto user = userMapper.getUser(Map.of("userNo", userNo));
    
    if(user == null) {    // 만약 로그인이 풀려서 user가 없으면, 다른 응답을 만든다.
      try {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("alert('회원 탈퇴를 수행할 수 없습니다.')");
        out.println("location.href='" + request.getContextPath() + "/main.do'");
        out.println("</script>");
        out.flush();
        out.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    
    int insertLeaveUserResult = userMapper.insertLeaveUser(user);
    int deleteUserResult = userMapper.deleteUser(user);
    
    try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(insertLeaveUserResult == 1 && deleteUserResult == 1) {       // 탈퇴 성공
        HttpSession session = request.getSession();
        session.invalidate();   // session 초기화. 이걸 안 하면 탈퇴했는데 로그인이 되어있음. 탈퇴 시 필수 !
        out.println("alert('회원 탈퇴되었습니다. 그 동안 이용해 주셔서 감사합니다.')");
        out.println("location.href='" + request.getContextPath() + "/main.do'");
      } else {
        out.println("alert('회원 탈퇴되지 않았습니다.')");
        out.println("history.back()");
      }
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  
  @Override
  public void inactiveUserBatch() {
    userMapper.insertInactiveUser();      // insert 먼저
    userMapper.deleteUserForInactive();
  }
  
  @Override
  public void active(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
    
    // 복구해줄 이메일
    InactiveUserDto inactiveUser = (InactiveUserDto)session.getAttribute("inactiveUser");   // 위에 login메소드에서 넣어둠
    String email = inactiveUser.getEmail();
    
    int insertActiveUserResult =  userMapper.insertActiveUser(email);
    int deleteInactiveUserResult = userMapper.deleteInactiveUser(email);
    
    try {
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(insertActiveUserResult == 1 && deleteInactiveUserResult == 1) {
        out.println("alert('휴면계정이 복구되었습니다. 계정 활성화를 위해서 곧바로 로그인 해주세요.')");
        out.println("location.href='" + request.getContextPath() + "/main.do'");    // 로그인 페이지로 보내면 로그인 후 다시 휴면 계정 복구 페이지로 돌아오므로 main으로 이동한다.
      } else {
        out.println("alert('휴면계정 복구를 실패했습니다. 다시 시도하세요.')");
        out.println("history.back()");
      }
      out.println("</script>");
      out.flush();
      out.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  
}
