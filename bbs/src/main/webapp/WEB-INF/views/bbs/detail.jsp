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

  <h1>상세보기</h1>
  
  <!-- INSERT, UPDATE, DELETE -> POST -->
  <form id="frm_detail" method="post">
    <div>게시글 번호 : ${bbs.bbsNo}</div>
    <div>작성자 : ${bbs.editor}</div>
    <div>제목 : <input type="text" name="title" value="${bbs.title}" required></div>
    <div>내용 : <input type="text" name="contents" value="${bbs.contents}"></div>
    <div>작성일 : ${bbs.createdAt}</div>
    <div>수정일 : ${bbs.modifiedAt == null ? '없음' : bbs.modifiedAt}</div>
    <input type="hidden" name="bbsNo" value="${bbs.bbsNo}">
    <button type="button" id="btn_modify">수정</button>
    <button type="button" id="btn_remove">삭제</button>
    <button type="button" id="btn_list">목록</button>
  </form>

  <script>
    
    var frmDetail = $('#frm_detail');
  
    // 수정
    $('#btn_modify').click(() => {
    	frmDetail.attr('action', '${contextPath}/modify.do');
    	frmDetail.submit();
    })
    
    // 삭제
    $('#btn_remove').click(() => {
    	if(confirm('게시글을 삭제할까요?')){
    		frmDetail.attr('action', '${contextPath}/bbs/remove.jsp');
    		frmDetail.submit();
    	}
    })
    
    // 목록
    $('#btn_list').click(() => {
    	location.href="${contextPath}/list.do";
    })
    
    const modifyResult = '${modifyResult}';   // '', '1', '0'
    if(modifyResult !== '') {
      if(modifyResult === '1'){
        alert('수정 성공');
      } else {
        alert('수정 실패');
      }
    }
  
  </script>  
  
</body>
</html>