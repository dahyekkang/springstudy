<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  <%-- blogNo를 보고 싶으면 el을 사용하여 속성값 그대로 적어주기! requestScope.blogNo과 같다. --%>
  ${blogNo}
  <br>
  ${requestScope.blogNo}
  <br>
  ${blogDto.blogNo}
  <br>
  ${blogDto.getBlogNo()}
  <br>
  ${dto.blogNo}
</body>
</html>