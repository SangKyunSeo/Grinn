<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/sys/admin.css">
<script type="text/javascript">
	$(function(){
		//검색 유효성 체크
		$('#search_form').submit(function(){
			if($('#keyword').val().trim() == ''){
				alert('검색어를 입력하세요');
				$('#keyword').val('').focus();
				return false;
			}
		});
	});
</script>
<style>
	h2{
		text-align: center;
	}
</style>
<div class="page-main">
	<h2>회원목록(관리자)</h2>
	<form action="admin_list.do" id="search_form" method="get">
	<div class = "memberList">
		<div class="search">
			<div>
				<select name="keyfield" id = "keyfield">
					<option value="1" <c:if test="${param.keyfield == 1}">selected</c:if>>ID</option>
					<option value="2" <c:if test="${param.keyfield == 2}">selected</c:if>>이름</option>
					<option value="3" <c:if test="${param.keyfield == 3}">selected</c:if>>이메일</option>
					<option value="4" <c:if test="${param.keyfield == 4}">selected</c:if>>전체</option>
				</select>
			</div>
			<div class = "memberSearch">
				<input type="search" name="keyword" id="keyword" value="${param.keyword}">
			</div>
			<div>
				<input type="image" src="../images/item_search.png" name="submit"> 
			</div>
		</div>
	</div>
	</form>
	<c:if test="${count == 0}">
	<div class="result-display">표시할 회원정보가 없습니다.</div>
	</c:if>
	<c:if test="${count > 0}">
	<table class="striped-table">
		<tr>
			<th>아이디</th>
			<th>이름</th>
			<th>전화번호</th>
			<th>이메일</th>
			<th>가입일</th>
			<th>권한</th>
		</tr>
		<c:forEach var="member" items="${list}">
		<tr>
			<td>
				<c:if test="${member.mem_auth==0}">${member.mem_id}</c:if>
				<c:if test="${member.mem_auth > 0}"><a href="admin_update.do?mem_num=${member.mem_num}">${member.mem_id}</a></c:if>
			</td>
			<td>${member.mem_name}</td>
			<td>${member.mem_phone}</td>
			<td>${member.mem_email}</td>
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
	</c:if>	
</div>










