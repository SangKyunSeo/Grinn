<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2 class="align-center">
	<a href = "${pageContext.request.contextPath}/main/main.do">Grinn</a>
</h2>
<div class="align-right">
	<a href="${pageContext.request.contextPath}/notice/noticeList.do">고객센터</a>

	<c:if test="${!empty user && user.mem_auth == 2}">
		<a href="${pageContext.request.contextPath}/member/myPage.do">MY페이지</a>
		<a href="${pageContext.request.contextPath}/item/fav.do">관심상품</a>
		<a href="${pageContext.request.contextPath}/alert/alert.do">알림</a>
	</c:if>
	
	<c:if test="${!empty user}">
		<img src="${pageContext.request.contextPath}/member/photoView.do" 
		                        width="25" height="25" class="my-photo">
	</c:if>
	<c:if test="${!empty user && !empty user.mem_nickname}">
		[<span class="user_name">${user.mem_nickname}</span>]
	</c:if>
	<c:if test="${!empty user && empty user.mem_nickname}">
		[<span class="user_name">${user.mem_id}</span>]
	</c:if>
	
	<c:if test="${!empty user}">
		<a href="${pageContext.request.contextPath}/member/logout.do">로그아웃</a>
	</c:if>
	
	<c:if test="${empty user}">
	<a href="${pageContext.request.contextPath}/member/registerUser.do">회원가입</a>
	<a href="${pageContext.request.contextPath}/member/login.do">로그인</a>
	</c:if>
	<c:if test="${!empty user && user.mem_auth == 9}">
	<a href="${pageContext.request.contextPath}/main/admin.do">관리자메인</a>
	</c:if>
</div>
<div class = "align-right">
	<a href = "${pageContext.request.contextPath}/main/main.do"><b>HOME</b></a>
	<a href = "${pageContext.request.contextPath}/style/list.do"><b>STYLE</b></a>
	<a href = "${pageContext.request.contextPath}/item/itemList.do"><b>SHOP</b></a>
	<a href = "${pageContext.request.contextPath}/fleamarket/marketSelect.do"><b>FLEA MARKET</b></a>
</div>