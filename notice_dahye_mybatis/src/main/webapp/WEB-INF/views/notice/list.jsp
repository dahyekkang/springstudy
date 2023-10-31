<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
</head>
<body>

  <div>
    <h2>고양이 상사에 오신 걸 환영합니다</h2>
    <img src="${contextPath}/resources/image/animal10.jpg" width="360px">
  </div>
  
  <hr>
  
  <div>
    <thead>
      <tr>
        <td>공지번호</td>
        <td>제목</td>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${noticeList} var="notice">
        <tr>
          <td>${notice.noticeNo}</td>
          <td>${notice.title}</td>
        </tr>
      </c:forEach>
    </tbody>
  </div>

</body>
</html>