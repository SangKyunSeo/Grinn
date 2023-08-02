<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 플리마켓 리스트 목록 시작 -->
<script type="text/javascript">
	$(function(){
		// 검색 유효성 체크
		$('#search_form').submit(function(){
			if($('#keyword').val().trim() == ''){
				alert('검색어를 입력하세요');
				$('#keyword').val('').focus();
				return false;
			}
		});
	});
</script>
<div class="page-main">
	<h2>플리마켓 목록</h2>
	<!-- 검색창 시작 -->
	<form action="marketList.do" id="search_form" method="get">
		<ul class="search">
			<li>
				<select name="keyfield" id="keyfield">
					<option value="1" <c:if test="${param.keyfield == 1}">selected</c:if>>제목+내용</option>
					<option value="2" <c:if test="${param.keyfield == 2}">selected</c:if>>제목</option>
					<option value="3" <c:if test="${param.keyfield == 3}">selected</c:if>>내용</option>
				</select>
			</li>
			<li>
				<input type="search" name="keyword" id="keyword" value="${param.keyword}">
			</li>
			<li>
				<input type="submit" value="찾기">
				<input type="button" value="목록" onclick="location.href='marketList.do'">
			</li>
		</ul>
	</form>
	<!-- 검색창 끝 -->
	<c:if test="${count == 0}">
	<div class="result-display">표시할 게시물이 없습니다.</div>
	</c:if>
	<c:if test="${count > 0}">
	<table class="striped-table">
		<c:forEach var="market" items="${marketList}">
		<tr>
			<td class="align-center" width="400">${market.market_thumbNail}</td>
			<td width="200">${market.market_title}</td>
			<td>${market.market_startDate} ${market.market_endDate}</td>
			<td>예약 일시</td>
			<td>
				<input type="button" value="상세정보" onclick="location.href='marketDetail.do?market_num=${market.market_num}'" class="detail-btn">
				<%-- <c:if test="${booth.market_startDate}"> --%>
				<input type="button" value="예약 예정" class="before-btn">
				<%-- </c:if> --%>
				<%-- <c:if test="${booth.market_startDate}">
				<input type="button" value="예약 중" class="booking-btn">
				</c:if> --%>
				<%-- <c:if test="${booth.market_endDate}"> 
				<input type="button" value="예약 종료" class="after-btn">
				-- </c:if> --%>
			</td>
		</tr>
		</c:forEach>
	</table>
	<div class="align-center">${page}</div>
	</c:if>
</div>
<!-- 플리마켓 리스트 목록 끝 -->