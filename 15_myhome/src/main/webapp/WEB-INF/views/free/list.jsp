<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="dt" value="<%=System.currentTimeMillis()%>" />

<jsp:include page="../layout/header.jsp">
  <jsp:param value="자유게시판" name="title" />
</jsp:include>

<style>
  .blind {
    display: none;
  }
</style>

<div>
  
  <div><a href="${contextPath}/free/write.form">새글작성</a></div>
  
  <div>
    <table border="1">
      <thead>
        <tr>
          <td>순번</td>
          <td>작성자</td>
          <td>내용</td>
          <td>작성일자</td>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${freeList}" var="free" varStatus="vs">
          <tr class="list">
            <td>${beginNo - vs.index}</td>
            <td>${free.email}</td>
            <td>
              ${free.contents}
              <!-- 댓글작성버튼 -->
              <button type="button" class="btn_reply">댓글</button>
            </td>
            <td>${free.createdAt}</td>
          </tr>
          <tr class="blind write_tr">
            <td colspan="4">
              <form method="post" action="${contextPath}/free/addReply.do">
              <!-- 작성자/내용/원글 -->
                <div>
                  <label for="email">작성자</label>
                  <input type="text" name="email" id="email" value="${sessionScope.user.email}" readonly>     
                </div>
                <div>
                  <label for="contents">내용</label>
                  <input type="text" name="contents" id="contents">     
                </div>
                <div>
                  <input type="hidden" name="depth" value="${free.depth}">
                  <input type="hidden" name="groupNo" value="${free.groupNo}">
                  <input type="hidden" name="groupOrder" value="${free.groupOrder}">
                  <button type="submit">댓글달기</button>
                </div>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
      <tfoot>
        <tr>
          <td colspan="4">${paging}</td>
        </tr>
      </tfoot>
    </table>
  </div>
  
</div>

<script>

  
  const fnAddResult = () => {
	  let addResult = '${addResult}';
	  if(addResult !== '') {
		  if(addResult === '1'){
			  alert('게시글이 등록되었습니다.');
		  } else {
			  alert('게시글이 등록되지 않았습니다.');
		  }
	  }
  }
  
  const fnBlind = () => {
	  $('.btn_reply').click((ev) => {
		  // 화살표 함수는 this 객체가 지원되지 않기 때문에
		  // 이벤트 대상을 "이벤트 객체"의 "target" 속성으로 처리한다.
		  let writeTr = $(ev.target).closest('.list').next();   // .blind
		  
		  // class = "blind"를 가진 상태 : 숨김 상태이므로 열어 준다.
		  if(writeTr.hasClass('blind')){
			  $('.write_tr').addClass('blind');    // 모든 작성화면 닫기
			  writeTr.removeClass('blind');
		  }
		  // class="blind"가 없는 상태 : 이미 열린 상태이므로 다시 숨긴다.
		  else {
			  writeTr.addClass('blind');
		  }
	  })
  }
  
  const fnaddReplyResult = () => {
	   let addReplyResult = '${addReplyResult}';
	    if(addReplyResult !== '') {
	      if(addReplyResult === '1'){
	        alert('댓글이 등록되었습니다.');
	      } else {
	        alert('댓글이 등록되지 않았습니다.');
	      }
	    }
  }
  
  fnAddResult();
  fnBlind();
  fnaddReplyResult();
    
</script>


<%@ include file="../layout/footer.jsp" %>