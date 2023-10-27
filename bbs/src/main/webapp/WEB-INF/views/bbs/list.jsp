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
<style>
  .bbs {
    width: 300px;
    border: 1px solid gray;
    cursor: pointer;
  }
</style>
</head>
<body>

  <h1>작성화면</h1>
  
  <div>
    <form id="frm_add" method="post" action="${contextPath}/add.do">
      <div><input type="text" name="editor" id="editor" placeholder="작성자"></div>
      <div><input type="text" name="title" id="title" placeholder="제목"></div>
      <div><input type="text" name="contents" id="contents" placeholder="내용"></div>
      <div><button type="submit">등록하기</button></div>
    </form>
  </div>
  
  <hr>
  
  <h1>목록보기</h1>
  <h3>전체개수 : ${total}</h3>
  <c:forEach items="${bbsList}" var="bbs"> 
    <div class="bbs" data-bbs_no="${bbs.bbsNo}">
      <div>${bbs.bbsNo}</div>
      <div>${bbs.title}</div>
    </div>  
  </c:forEach>
  
  <div class="paging">${paging}</div>
  
  <script>
    /*
    $('.bbs').click(function(){
    	// 클릭한 대상 : 이벤트 대상 (this)
    	let bbsNo = $(this).data('bbs_no');
    	alert(bbsNo);
    })
    */
    $('.bbs').click((ev) => {
    	// 클릭한 대상 : 이벤트 대상 (이벤트객체의 target 속성)
    	let bbsNo = $(ev.target).parent().data('bbs_no');
    	location.href = '${contextPath}/detail.do?bbsNo=' + bbsNo;
    })
    
    $('#frm_add').submit((ev) => {
    	let title = $('#title');
    	if(title.val() === ''){
    		alert('제목은 필수입니다.');
    		title.focus();
    		ev.preventDefault();
    		return;
    	}
    })
    
    // const addResult = ${addResult};   // 이렇게 해도 되는데 전달될 땐 문제가 없는데 전달 안되면 문제가 생김(컴파일오류) 그래서 따옴표로 묶는다!!!!!!!!!
    const addResult = '${addResult}';   // '', '1', '0'
    if(addResult !== '') {
    	if(addResult === '1'){
    		alert('추가 성공');
    	} else {
    		alert('추가 실패');
    	}
    }
    
    const removeResult = '${removeResult}';   // '', '1', '0'
    if(removeResult !== '') {
      if(removeResult === '1'){
        alert('삭제 성공');
      } else {
        alert('삭제 실패');
      }
    }
    
  </script>
  
</body>
</html>