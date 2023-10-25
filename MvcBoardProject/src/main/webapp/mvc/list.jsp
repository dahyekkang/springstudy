<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<a href="/MvcBoardProject/write.do">새글작성</a>
	
	<hr>
	
	<table border="1">
		<thead>
			<tr>
				<td>글번호</td>
				<td>제목</td>
				<td>작성자</td>
				<td>작성일</td>
				<td>조회수</td>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="dto" items="${list}">
				<tr>
					<td>${dto.no}</td>
					<td><a href="/MvcBoardProject/updateHit.do?no=${dto.no}">${dto.title}</a></td>
					<td>${dto.author}</td>
					<td>${dto.postdate}</td>
					<td>${dto.hit}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
</body>
</html>