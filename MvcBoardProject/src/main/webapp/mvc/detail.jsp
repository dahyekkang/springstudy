<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
	function fn_Delete(f) {
		if (confirm('게시글을 삭제할까요?')) {
			f.action = '/MvcBoardProject/delete.do';
			f.submit();
		}
	}
</script>
</head>
<body>
	<h1>MvcBoard 게시글 상세보기화면</h1>
	<form method="post">
		<h3>${dto.no}번 게시글</h3>
		<p>작성자: ${dto.author}</p>
		<p>작성일: ${dto.postdate}</p>
		<p>작성IP: ${dto.ip}</p>
		<p>조회수: ${dto.hit}</p>
		<p>제목: ${dto.title}</p>
		<p>내용</p>
		<pre>${dto.content}</pre>
		<input type="hidden" name="no" value="${dto.no}">
		<input type="button" value="삭제하기" onclick="fn_Delete(this.form)">
		<input type="button" value="목록보기" onclick="location.href='/MvcBoardProject/list.do'">
	</form>
</body>
</html>