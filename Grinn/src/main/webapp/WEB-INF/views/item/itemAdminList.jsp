<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- 상품 목록 시작 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/item.css">
<script type="text/javascript">
	$(function(){
		//검색 유효성 체크
		$('#search_form').submit(function(){
			if($('#keyword').val().trim()==''){
				alert('검색어를 입력하세요!');
				$('#keyword').val('').focus();
				return false;
			}
		});
		
		$('input[type="search"]').attr('placeholder','전체 검색');
		
		function searchData(){
			let data = $('input[type="search"]').val();
			if(data == ''){
				alert('검색어를 입력하세요.');
				$('input[type="search"]').val('').focus();
				return false;
			}
			location.href="../main/search.do?keyfield=1&keyword="+data;
		};
		
		$('input[type="search"]').keypress(function(){
			if(event.keyCode==13){
				searchData();	
			}
			
		});
	});
</script>
<div class="page-main">
	<div class="addItem">
		<h2>상품 목록(관리자)</h2>
		<c:if test="${!empty user}">
			<input type="button" value="상품등록" onclick="location.href='itemWrite.do'" class="addItem">
		</c:if>
	</div>
	<form action="itemAdminList.do" id="search_form" method="get">
		<div class="itemListTop">
		<div class="search">
			<div>
				<select name="keyfield" id="keyfield">
					<option value="2" <c:if test="${param.keyfield == 2}">selected</c:if>>브랜드</option>
					<option value="3" <c:if test="${param.keyfield == 3}">selected</c:if>>상품명</option>
				</select>
			</div>
			<div class="itemSearch">
				<input type="search" name="keyword" id="keyword" value="${param.keyword}">
			</div>
			<div>
				<input type="image" src="../images/item_search.png" name="submit"> 
			</div>
		</div>
		<div class="orderTop">
			<select id="order" name="order">
				<option value="1" <c:if test="${param.order == 1}">selected</c:if>>최신</option>
				<option value="2" <c:if test="${param.order == 2}">selected</c:if>>관심상품순</option>
				<option value="3" <c:if test="${param.order == 3}">selected</c:if>>리뷰순</option>
			</select>
		</div>
		
		</div>
	</form>
	<c:if test="${count == 0}">
	<div class="result-display">표시할 게시물이 없습니다.</div>
	</c:if>
	<c:if test="${count > 0}">
		<table class="striped-table">
			<tr>
				<th class="z">제품<br>번호</th>
				<th></th>
				<th>브랜드</th>
				<th>제품명</th>
				<th class="z">최근<br>거래가</th>
				<th class="z">최근<br>거래일</th>
				<th>수정</th>
				<th>삭제</th>
			</tr>
			<c:forEach var="item" items="${list}">
				<tr class="align-center">
					<td>${item.item_num}</td>
					<td>
					<a href="itemDetail.do?item_num=${item.item_num}">
						<img src="${pageContext.request.contextPath}/item/photoView.do?item_num=${item.item_num}" width="50" height="50">
					</a>
					</td>
					<td class="brand-name-cell">${item.item_brand}</td>
					<td class="item-name-cell"><a href="itemDetail.do?item_num=${item.item_num}">${item.item_name}</a></td>
					<c:if test="${item.trade_price>0}">
						<td><fmt:formatNumber value="${item.trade_price}"/>원</td>
						<td><fmt:formatDate value="${item.trade_regdate}" pattern="yy.MM.dd"/></td>
					</c:if>
					<c:if test="${item.trade_price==0}">
						<td colspan="2">거래정보 없음</td>
					</c:if>
					<td>
						<a href="itemModify.do?item_num=${item.item_num}">수정</a>
					</td>
					<td><a href="itemDelete.do?item_num=${item.item_num}" onclick="return confirm('정말로 상품을 삭제하시겠습니까?')">삭제</a></td>
				</tr>
			</c:forEach>
		</table>
		<div class="align-center">${page}</div>
	</c:if>
</div>
<!-- 상품 목록 끝 -->