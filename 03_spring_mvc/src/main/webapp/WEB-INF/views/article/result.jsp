<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="#{pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

  <!-- model에 저장된 title (${title}, ${requestScope.title}) -->
  ${title}
  <br>
  ${requestScope.title}
  <br>
  <!-- session에 저장된 title -->
  ${sessionScope.title}
  <br>
  
  
  
  <a href="${contextPath}/article/main.do">세션 초기화하고 main 화면으로 이동</a>

  <br>
  
  <a href="${contextPath}/article/confirm.do">세션 확인하기</a>

</body>
</html>