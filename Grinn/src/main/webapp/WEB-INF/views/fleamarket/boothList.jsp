<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 부스 리스트 목록 시작 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/market.css">
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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/market.css">
<div class="page-main">
	<h2>플리마켓 목록</h2>
	<!-- 검색창 시작 -->
	<form action="boothList.do" id="search_form" method="get">
		<div class="itemListTop">
		<div class="search">
			<div>
				<select name="keyfield" id="keyfield">
					<option value="1" <c:if test="${param.keyfield == 1}">selected</c:if>>제목+내용</option>
					<option value="2" <c:if test="${param.keyfield == 2}">selected</c:if>>제목</option>
					<option value="3" <c:if test="${param.keyfield == 3}">selected</c:if>>내용</option>
				</select>
			</div>
			<div class="itemSearch">
				<input type="search" name="keyword" id="keyword" value="${param.keyword}">
			</div>
			<div>
				<input type="image" src="../images/item_search.png" name="submit"> 
			</div>
		</div>
		</div>
	</form>
	<!-- 검색창 끝 -->
	<c:if test="${count == 0}">
	<div class="result-display">표시할 플리마켓이 없습니다.</div>
	</c:if>
	<c:if test="${count > 0}">
	<table class="striped-table">
		<c:forEach var="booth" items="${boothList}">
		<tr class="market-list">
			<td class="align-center tuumbNail">
				<a href="${pageContext.request.contextPath}/fleamarket/detail.do?market_num=${booth.market_num}">
					<img src="${pageContext.request.contextPath}/fleamarket/imageView.do?market_num=${booth.market_num}&photo_type=2" width="120" height="160">
				</a>
			</td>
			<td class="market-title"><b>${booth.market_title}</b></td>
			<td class="market-date">${booth.market_startDate} ~ ${booth.market_endDate}</td>
			<td class="align-center market-place">${booth.place_name}</td>
			<td class="align-center">
				<input type="button" value="상세정보" onclick="location.href='detail.do?market_num=${booth.market_num}'" class="detail-btn">
				<c:if test="${booth.market_type == 2}">
				<input type="button" value="예약" class="booking-btn" onclick="location.href='booking.do?market_num=${booth.market_num}'" class="detail-btn">
				</c:if>
				<c:if test="${booth.market_type == 1}">
				<input type="button" value="예약 예정" class="detail-btn-disabled">
				</c:if>
				<c:if test="${booth.market_type == 3}">
				<input type="button" value="예약 종료" class="detail-btn-disabled">
				</c:if>
			</td>
		</tr>
		</c:forEach>
	</table>
	<div class="align-center">${page}</div>
	</c:if>
</div>
<!-- 부스 리스트 목록 끝 -->