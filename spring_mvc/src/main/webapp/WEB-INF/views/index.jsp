<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

  <div><a href="${contextPath}/add1.do?boardNo=1&title=공지사항">링크</a></div>
  
  <form action="${contextPath}/add2.do">
    <div><input type="text" name="boardNo"></div>
    <div><input type="text" name="title"></div>
    <div><button type="submit">전송</button></div>
  </form>
  
  <div>
    <button type="button" id="btn">버튼</button>
  </div>
  
  <script>
    document.getElementById('btn').onclick = function(){
    	location.href='${contextPath}/add3.do?boardNo=1&title=공지사항';    	
    }
  </script>

</body>
</html>