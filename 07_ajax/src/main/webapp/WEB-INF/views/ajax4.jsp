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
	  fnImage();
  })
  
  function fnImage(){
	  $('#btn_image').click(function(){
		  var path = encodeURIComponent('C:\\GDJ69\\assets\\image');  // encodeURIComponent : 자바스크립트 인코딩 필요할 때 사용. // 자바로 보낼 때 문제가 될 수 있어서 \를 2개 사용
		  var filename = $('#image').val();
		  $('#display').empty();    // display를 지우는 게 아니라, display에 추가한 image src를 지운다.
		  $('#display').append('<img src="${contextPath}/ajax4/display.do?path=' + path + '&filename=' + filename + '" width="192px">'); // src에 그림주시오 요청을 한다.
	  })
  }
	  

</script>

</head>
<body>

  <%-- HDD에 저장된 이미지를 표시하기 --%>

  <div>
    <select id="image">
      <c:forEach var="n" begin="1" end="10" step="1">
        <option>animal${n}.jpg</option>
      </c:forEach>
    </select>
    <button id="btn_image">이미지 가져오기</button>
  </div>
  
  <hr>
  
  <div id="display"></div>

</body>
</html>