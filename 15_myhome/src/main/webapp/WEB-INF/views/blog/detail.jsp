<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="dt" value="<%=System.currentTimeMillis()%>" />

<jsp:include page="../layout/header.jsp">
  <jsp:param value="${blog.blogNo}번 블로그" name="title"/>
</jsp:include>

<style>
  .blind {
    display: none;
  }
  .ico_remove_comment {
    cursor: pointer;
  }
</style>

<div>

  <!-- 블로그 상세보기 -->
  <div>
    <h1>${blog.title}</h1>
    <div>작성자 : ${blog.userDto.name}</div>
    <div>조회수 : ${blog.hit}</div>
    <div>작성IP : ${blog.ip}</div>
    <div>작성일 : ${blog.createdAt}</div>
    <div>수정일 : ${blog.modifiedAt}</div>
    <div>
      <!-- 블로그의 작성자는 편집/삭제를 수행할 수 있다. -->
      <c:if test="${sessionScope.user.userNo == blog.userDto.userNo}">
        <form id="frm_btn" method="post">
          <input type="hidden" name="blogNo" value="${blog.blogNo}">
          <input type="hidden" name="title" value="${blog.title}">
          <input type="hidden" name="contents" value='${blog.contents}'>
          <button type="button" id="btn_edit">편집</button>
          <button type="button" id="btn_remove">삭제</button>
        </form>
      </c:if>
    </div>
    <div>${blog.contents}</div>
   </div>
  <script>
  
    var frmBtn = $('#frm_btn');
  
    const fnEditBlog = () => {
    	$('#btn_edit').click(() => {
    		frmBtn.attr('action', '${contextPath}/blog/edit.form');
    		frmBtn.submit();
    	})
    }
    
    const fnRemoveBlog = () => {
    	$('#btn_remove').click(() => {
    		if(confirm('블로그를 삭제하면 모든 댓글이 함께 삭제됩니다. 삭제할까요?')){
    		  frmBtn.attr('action', '${contextPath}/blog/remove.do');
    		  frmBtn.submit();
    		}
    	})
    }
    
    fnEditBlog();
    fnRemoveBlog();
    
  </script>
  
  <hr>
  
  <!-- 댓글 작성 화면 -->
  <div>
    <form id="frm_comment_add"> 
      <h3> 댓글 </h3>
      <textarea rows="3" cols="50" name="contents" id="contents"  class="form-control" placeholder="댓글을 작성해 주세요"></textarea>
      <input type="hidden" name="userNo" value="${sessionScope.user.userNo}">
      <input type="hidden" name="blogNo" value="${blog.blogNo}">
      <div>
        <button type="button" id="btn_comment_add" class="btn btn-primary">작성완료</button>
      </div>
    </form>
  </div>
  
  <!-- 블로그 댓글 목록 -->
  <div style="width: 100%; border-bottom: 1px solid gray;"></div>
  <div id="comment_list"></div>
  <div id="paging"></div>
  
  <script>
    
    const fnCommentAdd = () => {
      $('#btn_comment_add').click(() => {
        if('${sessionScope.user}' === ''){
          if(confirm('로그인이 필요한 기능입니다. 로그인할까요?')){
            location.href = '${contextPath}/user/login.form';
          } else {
            return;
          }
        }
        // 댓글이 달렸다고 해서 블로그 내용이 바뀌는 것 아님. 댓글만 목록 갱신 -> ajax
        $.ajax({
          // 요청
          type: 'post',   // 삽입할 때 요청은 post
          url: '${contextPath}/blog/addComment.do',
          data: $('#frm_comment_add').serialize(),
          // 응답
          dataType: 'json',
          success: (resData) => {   // {"addCommentResult": 1}
            if(resData.addCommentResult === 1){
              alert('댓글이 등록되었습니다.');
              $('#contents').val('');
              fnCommentList();
            }
          }
        })
      })
    }
    
    // 전역 변수
    var page = 1;
    
    const fnCommentList = () => {
  	  // click 이벤트가 따로 없다.
      $.ajax({
        // 요청
        type: 'get',
        url: '${contextPath}/blog/commentList.do',
        data: 'page=' + page + '&blogNo=${blog.blogNo}',
        // 응답
        dataType: 'json',
        success: (resData) => {    // resData = {"commentList": [], "paging": "<div> ... </div>"}
          $('#comment_list').empty();
          $('#paging').empty();
          if(resData.commentList.length === 0){
            	$('#comment_list').text('첫 번째 댓글의 주인공이 되어 보세요.');
            	$('#paging').text('');
            	return;
            } 
          $.each(resData.commentList, (i, c) => {
      	    let str = '';
      	    if(c.depth === 0){
      	    	str += '<div style="width: 100%; border-bottom: 1px solid gray;">';
      	    } else {
      	    	str += '<div style="width: 100%; border-bottom: 1px solid gray; margin-left: 32px;">';
      	    }
      	    if(c.status === 0){
      	    	str += '<div>삭제된 댓글입니다.</div>';
      	    	// continue;   // 다시 반복문 처음으로 돌아가기
      	    } else {
        	    str += '  <div>' + c.userDto.name + '</div>';
        	    str += '  <div>' + c.contents + '</div>';
        	    str += '  <div style="font-size: 12px;">' + c.createdAt + '</div>';
        	    if(c.depth === 0){
        	      str += '  <div><button type="button" class="btn_open_reply btn btn-primary" id="${c.commentNo}">답글달기</button></div>'
        	    }
        	    /*********************************** 답글 입력 창 ***********************************/
        	    str += '  <div class="blind frm_add_reply_wrap">';
        	    str += '    <form class="frm_add_reply">';
        	    str += '      <div>${sessionScope.user.name}</div>';
        	    str += '      <textarea rows="3" cols="50" name="contents" placeholder="답글을 입력하세요"></textarea>';
        	    str += '      <input type="hidden" name="userNo" value="${sessionScope.user.userNo}">';
        	    str += '      <input type="hidden" name="blogNo" value="${blog.blogNo}">';
        	    str += '      <input type="hidden" name="groupNo" value="' + c.groupNo +'">';
        	    str += '      <button type="button" class="btn btn-primary btn_add_reply">답글작성완료</button>';
        	    str += '    </form>';
        	    str += '  </div>';
        	    /************************************************************************************/
        	    if('${sessionScope.user.userNo}' == c.userDto.userNo){      // javascript는 '1' == 1 : true, '1' === 1 : false
          	    str += '  <div>';
          	    str += '    <input type="hidden" value="' + c.commentNo + '">';
          	    str += '    <i class="fa-regular fa-circle-xmark ico_remove_comment"></i>';    // 버튼 아이콘으로
          	    str += '  </div>';        	    	
        	    }
      	    }
      	    str += '</div>';
      	    $('#comment_list').append(str);
      	  })
      	  $('#paging').append(resData.paging);   // fnAjaxPaging() 함수가 호출되는 곳
        }
      })
    }
    
    const fnAjaxPaging = (p) => {
  	  page = p;
  	  fnCommentList();
    }
    
    const fnBlind = () => {
    	$(document).on('click', '.btn_open_reply', (ev) => {
        if('${sessionScope.user}' === ''){
          if(confirm('로그인이 필요한 기능입니다. 로그인할까요?')){
            location.href = '${contextPath}/user/login.form';
          } else {
            return;
          }
        }
        var blindTarget = $(ev.target).parent().next();
        if(blindTarget.hasClass('blind')){
        	$('.frm_add_reply_wrap').addClass('blind');    // 모든 답글 입력화면 닫기
        	blindTarget.removeClass('blind');              // 답글 입력화면 열기
        	$('.btn_open_reply').removeClass('active');
        	$(ev.target).addClass('active');
        } else {
        	blindTarget.addClass('blind');
        }
    	})
    }
    
    const fnCommentReplyAdd = () => {
  	  $(document).on('click', '.btn_add_reply', (ev) => {
        if('${sessionScope.user}' === ''){
          if(confirm('로그인이 필요한 기능입니다. 로그인할까요?')){
            location.href = '${contextPath}/user/login.form';
          } else {
            return;
          }
        }
  		  var frmAddReply = $(ev.target).closest('.frm_add_reply');
  		  $.ajax({
  			  // 요청
  			  type: 'post',
  			  url: '${contextPath}/blog/addCommentReply.do',
  			  data: frmAddReply.serialize(),
  			  
  			  // 응답
  			  dataType: 'json',
  			  success: (resData) => {    // resData = {"addCommentReplyResult": 1}
  				  if(resData.addCommentReplyResult === 1){
  					  alert('답글이 등록되었습니다.');
  					  fnCommentList();
  					  frmAddReply.find('textarea').val('');
  				  } else {
  					  alert('답글이 등록되지 않았습니다.');
  				  }
  			  }
  		  })
  	  })
    }
    
    const fnCommentRemove = () => {
    	$(document).on('click', '.ico_remove_comment', (ev) => {
    		if(!confirm('해당 댓글을 삭제할까요?')){
    			return;
    		}
    		$.ajax({
    			// 요청
    			type: 'post',
    			url: '${contextPath}/blog/removeComment.do',
    			data: 'commentNo=' + $(ev.target).prev().val(),
    			
    			// 응답
    			dataType: 'json',
    			success: (resData) => {    // resData = {"removeResult": 1}
    				if(resData.removeResult === 1){
    					alert('해당 댓글이 삭제되었습니다.');
    					fnCommentList();
    				} else {
    					alert('댓글이 삭제되지 않았습니다.');
    				}
    			}
    		})
    	})
    }
    
    fnCommentAdd();
    fnBlind();
    fnCommentReplyAdd();
    fnCommentList();
    fnCommentRemove();
    
    /*
    <div style="width: 100%; border-bottom: 1px solid gray;">
    
      // 삭제된 댓글/답글
      <div>삭제된 댓글입니다</div>
    
      // 정상 댓글/답글
      <div>이름</div>
      <div>내용</div>
      <div style="font-size: 12px;">작성일자</div>
      <div><button type="button" class="btn_open_reply">답글달기</button></div>
      <div class="blind frm_add_reply_wrap">
        <form class="frm_add_reply">
          <textarea rows="3" cols="50" name="contents" placeholder="답글을 입력하세요"></textarea>
          <input type="hidden" name="userNo" value="">
          <input type="hidden" name="blogNo" value="">
          <input type="hidden" name="groupNo" value="">
          <button type="button" class="btn_add_reply">답글작성완료</button>
        </form>
      </div>
      <div>
        <input type="hidden" value="commentNo값">
        <button type="button" class="btn_remove_comment">삭제</button>            // 이 부분을 지우고 아래 아이콘버튼으로 바꾸었음
        <i class="fa-regular fa-circle-xmark ico_remove_comment" style="cursor: pointer"></i>';
      </div>  
    </div>
    */
      
  </script>


</div>

<%@ include file="../layout/footer.jsp" %>