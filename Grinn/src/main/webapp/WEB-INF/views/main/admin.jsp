<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/sys/admin.css">
<div class="page-main">
	<div class = "page_body">
		<h2 id = "ha">회원목록</h2>
		<div class = "table">
		<table class="striped-table">
			<tr>
				<th>아이디</th>
				<th>이름</th>
				<th>이메일</th>
				<th>전화번호</th>
				<th>가입일</th>
				<th>권한</th>
			</tr>
			<c:forEach var="member" items="${memberList}">
			<tr>
				<td>
					<c:if test="${member.mem_auth == 0}">${member.mem_id}</c:if>
					<c:if test="${member.mem_auth > 0}"><a href="${pageContext.request.contextPath}/member/admin_update.do?mem_num=${member.mem_num}">${member.mem_id}</a></c:if>
				</td>
				<td>${member.mem_name}</td>
				<td>${member.mem_email}</td>
				<td>${member.mem_phone}</td>
				<td>${member.mem_date}</td>
				<td>
					<c:if test="${member.mem_auth == 0}">탈퇴</c:if>
					<c:if test="${member.mem_auth == 1}">정지</c:if>
					<c:if test="${member.mem_auth == 2}">일반</c:if>
					<c:if test="${member.mem_auth == 9}">관리</c:if>
				</td>
			</tr>
			</c:forEach>
		</table>
		<div class="align-center">${page}</div>
		</div>
	</div>
</div>