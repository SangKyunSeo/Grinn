<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- 상품 목록 시작 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/item.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/itemWrite.js"></script>
<script type="text/javascript">
	$(function() {
		//검색 유효성 체크
		$('#search_form').submit(function() {
			if ($('#keyword').val().trim() == '') {
				alert('검색어를 입력하세요!');
				$('#keyword').val('').focus();
				return false;
			}
		});
	});
</script>

<div class="page-main">
	<div id="page_nav1">
		<div class="side-bar1">
			<a href="itemList.do" class="tab-link ${activeTab == 0 ? 'active' : ''}" data-tab="0">전체</a>
    		<a href="itemList.do?tab=1" class="tab-link ${activeTab == 1 ? 'active' : ''}" data-tab="1">신발</a>
    		<a href="itemList.do?tab=2" class="tab-link ${activeTab == 2 ? 'active' : ''}" data-tab="2">상의</a>
    		<a href="itemList.do?tab=3" class="tab-link ${activeTab == 3 ? 'active' : ''}" data-tab="3">하의</a>
    		<a href="itemList.do?tab=4" class="tab-link ${activeTab == 4 ? 'active' : ''}" data-tab="4">패션잡화</a>
    	</div>
    </div>
	<form action="itemList.do" id="search_form" method="get">
		<ul class="search">
			<li>
				<select name="keyfield" id="keyfield">
					<option value="1" <c:if test="${param.keyfield == 1}">selected</c:if>>브랜드</option>
					<option value="2" <c:if test="${param.keyfield == 2}">selected</c:if>>상품명</option>
				</select>
			</li>
			<li>
				<input type="search" name="keyword" id="keyword" value="${param.keyword}">
			</li>
			<li>
				<input type="submit" value="찾기"> 
				<input type="button" value="목록" onclick="location.href='itemList.do'">
			</li>
		</ul>
		<div class="align-right">
			<select id="order" name="order">
				<option value="1" <c:if test="${param.order == 1}">selected</c:if>>최신</option>
				<option value="2" <c:if test="${param.order == 2}">selected</c:if>>관심상품순</option>
				<option value="3" <c:if test="${param.order == 3}">selected</c:if>>리뷰순</option>
			</select>
			<!-- <script type="text/javascript">
				$(function() {
					$('#order').change(
							function() {
								location.href = 'itemList.do?keyfield='
										+ $('#keyfield').val() + '&keyword='
										+ $('#keyword').val() + '&order='
										+ $('#order').val();
							});
				});
			</script> -->
		</div>
	</form>
	<c:if test="${count == 0}">
		<div class="result-display">표시할 게시물이 없습니다.</div>
	</c:if>
	<c:if test="${count > 0}">
		<div class="list01">
			<c:forEach var="item" items="${list}">
				<div class="list02">
					<a href="itemDetail.do?item_num=${item.item_num}"> 	
						<img src="${pageContext.request.contextPath}/item/photoView.do?item_num=${item.item_num}">
							<div class="list03">
								<p class="listBrand">${item.item_brand}</p>
								<div>
									<p class="itemName">${item.item_name}</p>
									<p class="itemDetail">${item.item_detail}</p>
								</div>
							</div>
							<div class="sale">
							<!-- 즉시구매가 -->
								<c:if test="${item.sale==0}">
									<span>- 원</span>
									<p>즉시구매가</p>
								</c:if>
								<c:if test="${item.sale!=0}">
									<span><fmt:formatNumber value="${item.sale}"/>원</span>
									<p>즉시구매가</p>
								</c:if>
							</div>
						</a>
						<div>
						<!-- 상품보관버튼 -->
						<span class="wishReview"> 
							<img class="output_itemfav" data-num="${item.item_num}"
								src="${pageContext.request.contextPath}/images/save_blank.png" width="15" height="15"> 
								<span class="output_itemfcount"  data-num="${item.item_num}"></span>
						</span> 
						<span class="wishReview"> 
							<img src="${pageContext.request.contextPath}/images/review.png"
								width="15" height="15"> <span>${item.re_cnt}</span>
						</span>
					</div>
					</div>
				</c:forEach>
			</div>
		<%-- <table class="striped-table">
			<tr>
				<td>사진</td>
				<td>제품명</td>
				<td>제품상세</td>
				<td>가격</td>
				<td>좋아요수</td>
				<td>댓글수</td>
			</tr>
			<c:forEach var="item" items="${list}">
				<tr>
					<td>
						<img src="${pageContext.request.contextPath}/item/photoView.do?item_num=${item.item_num}" width="200" height="200">
					</td>
					<td width="400">
						<a href="detail.do?item_num=${item.item_num}">${item.item_name}</a>
					</td>
					<td class="align-center">${item.item_detail}</td>
					<td class="align-center">${item.item_price}</td>
					<td class="align-center">${item.fav_cnt}</td>
					<td class="align-center">${item.re_cnt}</td>
				</tr>
			</c:forEach>
		</table> --%>
		<div class="align-center">${page}</div>
	</c:if>
</div>
<!-- 상품 목록 끝 -->