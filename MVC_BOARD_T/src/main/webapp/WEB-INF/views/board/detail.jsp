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
<script>

  $(function(){
	  fnRemove();
	  fnList();
  })

  function fnList(){
	  $(document).on('click', '.btn_list', function(){
		  location.href = '${contextPath}/board/list.do';
    })
  }
  
  function fnRemove(){
		$(document).on('click', '#btn_remove', function(){
			if(confirm('공지사항을 삭제할까요?')){
				location.href = '${contextPath}/board/remove.do?no=${board.no}';
			}
		})
  }
  
</script>
</head>
<body>

  <div>
    <h1>MvcBoard 게시글 상세보기 화면</h1>
    <div>작성자: ${board.author}</div>
    <div>작성일: ${board.postdate}</div>
    <div>작성IP: ${board.ip}</div>
    <div>조회수: ${board.hit}</div>
    <div>제목: ${board.title}</div>
    <div>내용</div>
    <div>${board.content}</div>
    <div>
      <button type="button" id="btn_remove">삭제하기</button>
      <button type="button" class="btn_list">목록보기</button>
    </div>
  </div>
  
</body>
</html>