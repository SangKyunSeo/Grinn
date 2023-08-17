<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<!-- 판매내역 시작 -->
<script type="text/javascript">
	$(function(){
		$('#dialog').dialog({
			width: '700px',
			height: 'auto',
			autoOpen: false,
			modal: true
		});
		
		$('.head-list').click(function(){
			$('#dialog').dialog('open');
		});
		
		$('#btn_status1').click(function(){
			$('#status').attr('value','전체').attr('data-status','1');	
			$('#dialog').dialog('close');
			if(${way} == '1'){
				location.href='${pageContext.request.contextPath}/user/selling.do?way=1&status=1';
			}else if(${way} == '2'){
				location.href='${pageContext.request.contextPath}/user/selling.do?way=2&status=1';
			}
		});
		$('#btn_status2').click(function(){
			if(${way} == '1'){
				$('#status').attr('value','입찰중').attr('data-status','2');	
				
				location.href='${pageContext.request.contextPath}/user/selling.do?way=1&status=2';
			}else if(${way} == '2'){
				$('#status').attr('value','검수준비중').attr('data-status','2');	
				$('#dialog').dialog('close');
				location.href='${pageContext.request.contextPath}/user/selling.do?way=2&status=2';
			}
		});
		$('#btn_status3').click(function(){
			if(${way} == '1'){
				$('#status').attr('value','기한만료').attr('data-status','3');	
				$('#dialog').dialog('close');
				location.href='${pageContext.request.contextPath}/user/selling.do?way=1&status=3';
			}else if(${way} == '2'){
				$('#status').attr('value','검수중').attr('data-status','3');	
				$('#dialog').dialog('close');
				location.href='${pageContext.request.contextPath}/user/selling.do?way=2&status=3';
			}
		});
		
		$('#btn_status4').click(function(){
			$('#status').attr('value','배송중').attr('data-status','4');	
			$('#dialog').dialog('close');
			location.href='${pageContext.request.contextPath}/user/selling.do?way=2&status=4';
		});
		
		$('#btn_status5').click(function(){
			$('#status').attr('value','거래실패').attr('data-status','6');	
			$('#dialog').dialog('close');
			location.href='${pageContext.request.contextPath}/user/selling.do?way=2&status=6';
		});
		
		if(${way} == '1'){
			if($('#status').attr('data-status') == '3'){
				$('.list-text').css('color','red');
			}	
		}
		
		let currentDate = new Date();
		$('.send-item').each(function(){
			let date = $(this).attr('data-regDate');
			date = new Date(date);
			let diff = currentDate.getDate() - date.getDate();
			let trade_num = $(this).attr('data-trade_num');
			$(this).click(function(){
				if(diff > 5){
    				alert('5일이 지났습니다. 거래는 실패했습니다.');	
    				return;
    			}else{
    				location.href='${pageContext.request.contextPath}/sale/sendItem.do?trade_num=' + trade_num;
    			}
			});
		});
		
		
	});
</script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ssk/selling.css">   
<jsp:useBean id="now" class="java.util.Date" /> 
<fmt:formatDate value="${now}" pattern="yyyy/MM/dd" var="nowDate" /> 
<div class="page-main">
	<div class="mySale">
		<div class="mySale-title">
			<div class="mySale-title-content">
				<h2>판매 내역</h2>
			</div>
		</div>
		<div class="mySale-notice">
			<span>검수준비중인 상품을 거래일 기준 5일 이내로 보내주세요.</span>
		</div>
		<div class="mySale-tab">
			<div class="tab-item">
				<a href="${pageConetext.request.contextPath}/user/selling.do?way=1" <c:if test="${way == 1}">style="border-bottom:1px solid #222"</c:if>>
					<dl class="tab-box">
						<dt>판매입찰</dt>
						<dd <c:if test="${way == 1}">style="color:#41B979;"</c:if>>${bidCount}</dd>
					</dl>
				</a>
			</div>
			<div class="tab-item">
				<a href="${pageConetext.request.contextPath}/user/selling.do?way=2" <c:if test="${way == 2}">style="border-bottom:1px solid #222"</c:if>>
					<dl class="tab-box">
						<dt>진행중</dt>
						<dd <c:if test="${way == 2}">style="color:#41B979;"</c:if>>${tradeCount}</dd>
					</dl>
				</a>
			</div>
			<div class="tab-item">
				<a href="${pageConetext.request.contextPath}/user/selling.do?way=3" <c:if test="${way == 3}">style="border-bottom:1px solid #222"</c:if>>
					<dl class="tab-box">
						<dt>종료</dt>
						<dd <c:if test="${way == 3}">style="color:#41B979;"</c:if>>${quitCount}</dd>
					</dl>
				</a>
			</div>
		</div>
		<div class="mySale-detail">
			<div class="mySale-head">
				<div class="head-list">
					<c:if test="${way == 1}">
						<c:if test="${status == 1}">
							<input type="button" data-status="1" value="전체" id="status">
						</c:if>
						<c:if test="${status == 2}">
							<input type="button" data-status="2" value="입찰중" id="status">
						</c:if>
						<c:if test="${status == 3}">
							<input type="button" data-status="3" value="기한만료" id="status">
						</c:if>
					</c:if>
					<c:if test="${way == 2}">
						<c:if test="${status == 1}">
							<input type="button" data-status="1" value="전체" id="status">
						</c:if>
						<c:if test="${status == 2}">
							<input type="button" data-status="2" value="검수준비중" id="status">
						</c:if>
						<c:if test="${status == 3}">
							<input type="button" data-status="3" value="검수중" id="status">
						</c:if>
						<c:if test="${status == 4}">
							<input type="button" data-status="4" value="배송중" id="status">
						</c:if>
						<c:if test="${status == 6}">
							<input type="button" data-status="6" value="거래실패" id="status">
						</c:if>
					</c:if>
					<c:if test="${way == 3}">
						<input type="button" data-status="5" value="배송완료" id="status">
					</c:if>
				</div>
			</div>
			<div class="mySale-main-content">
				<c:if test="${count == 0}">
					<c:if test="${way == 1}">
						<div class="mySale-empty">판매 입찰 내역이 없습니다.</div>
					</c:if>
					<c:if test="${way == 2}">
						<div class="mySale-empty">판매 거래 내역이 없습니다.</div>
					</c:if>
					<c:if test="${way == 3}">
						<div class="mySale-empty">종료된 판매 내역이 없습니다.</div>
					</c:if>
				</c:if>
				<c:if test="${count > 0}">
					<table>
					  <thead>
					    <tr>
					      <th>사진</th>
					      <th width="200">이름</th>
					      <th width="50">사이즈</th>
					      <c:if test="${way == 1}">
					      <th>희망 가격</th>
					      <th>등록일</th>
					      <th>마감일</th>
					      <c:if test="${status == 3}">
					      	<th>삭제</th>
					      </c:if>
					      </c:if>
					      <c:if test="${way >= 2}">
					      <th>거래 가격</th>
					      <th>거래 날짜</th>
					      <th>구매자</th>
					      <th>거래 상태</th>
					      <c:if test="${status == 2}">
					      <th>보내기</th>
					      </c:if>
					      </c:if>
					    </tr>
					  </thead>
					  <tbody>
					    <c:forEach items="${list}" var="list">
					      <tr class="list-text" <c:if test="${way == 1 and now > list.sale_deadline}">style="color:red;"</c:if>>
					        <td><img src="${pageContext.request.contextPath}/item/viewProfile.do?item_num=${list.item_num}" width="50" height="50"></td>
					        <td>${list.item_name}</td>
					       <c:if test="${empty list.item_size}">
					        	<td>ONE SIZE</td>
					        </c:if>
					        <c:if test="${!empty list.item_size}">
					        	<td>${list.item_size}</td>
					        </c:if>
					        <c:if test="${way == 1}">
					        <td><fmt:formatNumber value="${list.sale_price}"/></td>
					        <td class="list-date">${list.sale_regDate}</td>
					        <td class="list-date">${list.sale_deadline}</td>
					        <c:if test="${status==3}">
					        <td>
					        	<input type="button" class="btn-deleteBid" onclick="location.href='${pageContext.request.contextPath}/sale/deleteBid.do?sale_num=${list.sale_num}'">
					        </td>
					        </c:if>
					        </c:if>
					        <c:if test="${way == 2}">
					        <td><fmt:formatNumber value="${list.trade_price}"/></td>
					        <td class="list-date">${list.trade_regDate}</td>
					        <td>${list.mem_name}</td>
					        <c:if test="${list.trade_state==2}">
					        <td>검수준비중</td>
					        </c:if>
					        <c:if test="${list.trade_state==3}">
					        <td>검수중</td>
					        </c:if>
					        <c:if test="${list.trade_state==4}">
					        <td>배송중</td>
					        </c:if>
					        <c:if test="${list.trade_state==6}">
					        <td>거래실패</td>
					        </c:if>
					        <c:if test="${list.trade_state == 2}">
					        	<td class="btn-send-item"><input type="button" class="send-item" data-regDate="${list.trade_regDate}" data-trade_num="${list.trade_num}"></td>
					        </c:if>
					        </c:if>
					        <c:if test="${way == 3}">
					        <td><fmt:formatNumber value="${list.trade_price}"/></td>
					        <td class="list-date">${list.trade_regDate}</td>
					        <td>${list.mem_name}</td>
					        <c:if test="${list.trade_state==5}">
					        <td>배송완료</td>
					        </c:if>
					        </c:if>
					      </tr>
					    </c:forEach>
					  </tbody>
					</table>
				</c:if>
			</div>
			<div class="align-center">${page}</div>
		</div>
	</div>
	

	
<c:if test="${count > 0}">
	
</c:if>

<c:if test="${way == 1}">
	<div id="dialog">
		<ul>
			<li>
				<input type="button" value="전체" id="btn_status1">
			</li>
			<li>
				<input type="button" value="입찰중" id="btn_status2">
			</li>
			<li>
				<input type="button" value="기한만료" id="btn_status3">
			</li>
		</ul>
	</div>
</c:if>
<c:if test="${way == 2}">
	<div id="dialog">
		<ul>
			<li>
				<input type="button" value="전체" id="btn_status1">
			</li>
			<li>
				<input type="button" value="검수준비중" id="btn_status2">
			</li>
			<li>
				<input type="button" value="검수중" id="btn_status3">
			</li>
			<li>
				<input type="button" value="배송중" id="btn_status4">
			</li>
			<li>
				<input type="button" value="거래실패" id="btn_status5">
			</li>
		</ul>
	</div>
</c:if>
</div>
<!-- 판매내역 끝 -->
