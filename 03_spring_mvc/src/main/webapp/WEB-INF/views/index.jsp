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

  <%-- MyController01으로 요청 --%>
  <div>
    <a href="${contextPath}/board/list.do">board목록</a>
  </div>

  <hr>
  
  <%-- MyController02으로 요청 --%>
  <div>
    <a href="${contextPath}/notice/list.do">notice목록</a>
    <br>
    <a href="${contextPath}/member/list.do">member목록</a>
  </div>
  
  <hr>
  
  <%-- MyController03으로 요청 (파라미터를 받는 방법) --%>
  <%-- 요청하는 방법1 : <a> 태그 이용 --%>
  <div>
    <a href="${contextPath}/blog/detail.do?blogNo=100">블로그 상세보기</a>
  </div>
  
  <hr>
  
  <%-- MyController04으로 요청 --%>
  <div>
    <a href="${contextPath}/article/add.do?title=이럴수가">기사 등록하기</a>
  </div>
  <%-- ${sessionScope.title} --%>
  
  <hr>

  <%-- MyController05으로 요청 --%>
  <div>
    <form action="${contextPath}/faq/add.do" method="post">
      <div>
        <label for="title">제목</label>
        <input type="text" id="title" name="title">
      </div>
      <div>
        <button type="submit">작성완료</button>
      </div>
    </form>
  </div>

</body>
</html>