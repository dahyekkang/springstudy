<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>MvcBoard 작성화면</h1>
	<form action="/MvcBoardProject/insert.do" method="post">
		<p>작성자</p>
		<div><input type="text" name="author" autofocus></div>
		<p>제목</p>
		<div><input type="text" name="title" required></div>
		<p>내용</p>
		<div><textarea name="content" rows="5" cols="23"></textarea></div>
		<button>저장하기</button>
		<input type="reset" value="다시작성">
		<input type="button" value="목록보기" onclick="location.href='/MvcBoardProject/list.do'">
	</form>
</body>
</html>