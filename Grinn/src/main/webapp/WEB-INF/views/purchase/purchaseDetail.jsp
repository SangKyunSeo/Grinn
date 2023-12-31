<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<script type="text/javascript" src="${pageContext.request.contextPath}/js/trade.js"></script>    
<script type="text/javascript">
	$(function(){
		$('#btn_purchase_direct').show();
		$('#btn_purchase_bid').hide();
		$('#print_deadline').attr('data-deadline','1');
		$('.btn-direct input').css('background-color','#fa7070')
		$('.btn-direct input').css('color','white');
		$('#modalWrap').css('display','none');
		
		if(${saleBidCount == 0}){
			$('#print_deadline').attr('data-deadline','');
			$('#purchase_method').text('').text('구매 입찰 하기');
			$('#direct_price').css('display','none');
			$('#bid_price').css('display','flex');
			$('#input_deadline').css('display','block');
			$('#btn_purchase').attr('value','구매 입찰 계속');
			$('#btn_purchase').attr('data-type',"2");
			$('#input_bid').val('');
			$('.btn-bid input').css('background-color','#fa7070')
			$('.btn-bid input').css('color','white');
			$('.btn-direct input').css('background-color','#ebebeb')
			$('.btn-direct input').css('color','black');
		}
		
		$('#purchase_bid').click(function(){
			$('#print_deadline').attr('data-deadline','');
			$('#purchase_method').text('').text('구매 입찰 하기');
			$('#direct_price').css('display','none');
			$('#bid_price').css('display','flex');
			$('#input_deadline').css('display','block');
			$('#btn_purchase').attr('value','구매 입찰 계속');
			$('#btn_purchase').attr('data-type',"2");
			$('#input_bid').val('');
			$('.btn-bid input').css('background-color','#fa7070')
			$('.btn-bid input').css('color','white');
			$('.btn-direct input').css('background-color','#ebebeb')
			$('.btn-direct input').css('color','black');
		});
		$('#purchase_direct').click(function(){
			$('#input_bid').val(${minSaleBid});
			$('#purchase_method').text('').text('즉시 구매 하기');
			$('#direct_price').css('display','flex');
			$('#bid_price').css('display','none');
			$('#input_deadline').css('display','none');
			$('#btn_purchase').attr('value','즉시 구매 계속');
			$('#btn_purchase').attr('data-type',"1");
			$('.btn-direct input').css('background-color','#fa7070')
			$('.btn-direct input').css('color','white');
			$('.btn-bid input').css('background-color','#ebebeb')
			$('.btn-bid input').css('color','black');
		});
		
		$('#input_bid').keyup(function(){
			let val = $(this).val();
			val = Number(val.replaceAll(',',''));
			if(isNaN(val)){
				$('#input_warn').text('숫자를 입력하세요').css('color','red');
				$(this).val('').focus();
				return;
			}else{
				val = val.toLocaleString('ko-KR');
				$(this).val(val);
				$('#input_warn').text('');
			}
		});
		
		$('#input_bid').change(function(){
			let val = $(this).val();
			val = Number(val.replaceAll(',',''));
			if(val.trim==''){
				$('#input_warn').text('희망가를 입력하세요').css('color','red');
				$(this).val('').focus();
				return;
			}
			
			if(val < 20000){
				$('#input_warn').text('2만원 이상을 입력하세요.').css('color','red');
				$(this).val('').focus();
				return;
			}
			
			if(val.toString().length > 9){
				$('#input_warn').text('숫자는 9자리까지 입력 가능합니다.').css('color','red');
				$(this).val('').focus();
				return;
			}
			
			if(val >= ${minSaleBid} && ${minSaleBid}!=0){
				$('#print_deadline').attr('data-deadline','1');
				$('#purchase_method').text('').text('즉시 구매 하기');
				$('#direct_price').css('display','flex');
				$('#bid_price').css('display','none');
				$('#input_deadline').css('display','none');
				$('#btn_purchase').attr('data-type','1');
				$('#btn_purchase').attr('value','즉시 구매 계속');
				$('#input_bid').val(${minSaleBid});
				$('.btn-direct input').css('background-color','#fa7070')
				$('.btn-direct input').css('color','white');
				$('.btn-bid input').css('background-color','#ebebeb')
				$('.btn-bid input').css('color','black');
				return;
			}
			
			if(val < ${minSaleBid} && val >= 20000){
				$('#input_warn').text('');
				return;
			}
		});
		
		$('.purchase-deadline').each(function(){
			let deadline = $(this).attr('data-deadline');
			$(this).click(function(){
				initBtn();
				$('#print_deadline').text(deadline+'일').attr('data-deadline',deadline);
				let currentDate = new Date();
				currentDate.setDate(currentDate.getDate() + Number(deadline));
				$('#dateDeadline').attr('data-dateDeadline',currentDate);
				let year = currentDate.getFullYear();
				let month = (currentDate.getMonth() + 1);
				let day = currentDate.getDate();
				let formDate = year + '/' + month + '/' + day;
				$('#print_deadline').append(' ('+formDate+') 마감');
				$(this).css('background-color','#fa7070');
				$(this).css('color','white');
			});
		});
		
		function initBtn(){
			$('.purchase-deadline').each(function(){
				$(this).css('background-color','#fff');
				$(this).css('color','rgba(34,34,34,.8)');
			});
		};
		
		$('#address2').focusin(function(){
			$('#address2').css('border-bottom','1px solid black');
		});
		$('#address2').focusout(function(){
			$('#address2').css('border','0');
		});
	});
</script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/ssk/purchaseDetail.css">
<h1 id="purchase_method" class="align-center">즉시 구매 하기</h1>
<div class="page-size-main">
	<div class="content">
		<div class="purchase-inner">
			<div class="item">
				<div class="item-profile">
					<img src="${pageContext.request.contextPath}/item/viewProfile.do?item_num=${item.item_num}" width="130" height="130">
				</div>
				<div class="item-info">
					<p class="a">${item.item_brand}</p>
					<p class="b">${item.item_name}</p>
					<p class="c">${item.item_detail}</p>
					<c:if test="${empty item_size }">
						<p class="d">ONS SIZE</p>
					</c:if>
					<c:if test="${!empty item_size }">
						<p class="d">${item_size}</p>
					</c:if>
				</div>
			</div>
			<div class="purchase-detail">
				<div class="price-list">
					<input type="hidden" value="${minSaleBid}" name="minSaleBid" id="minSaleBid">
					<input type="hidden" value="${item_num}" name="item_num" id="item_num">
					<input type="hidden" value="${item_sizenum}" name="item_sizenum" id="item_sizenum">
					<input type="hidden" value="${item_size}" name="item_size" id="item_size">
					<input type="hidden" name="dateDeadline" id="dateDeadline">
					<ul>
						<li>
							<p>즉시 구매가</p>
							<c:if test="${minSaleBid == 0}">
								<span>-</span>
							</c:if>
							<c:if test="${minSaleBid != 0}">
								<span><fmt:formatNumber value="${minSaleBid}"/>원</span>		
							</c:if>
						</li>
						<li class="second">
							<p>즉시 판매가</p>
							<c:if test="${maxPurchaseBid == 0}">
								<span>-</span>
							</c:if>
							<c:if test="${maxPurchaseBid != 0}">
								<span><fmt:formatNumber value="${maxPurchaseBid}"/>원</span>		
							</c:if>
						</li>
					</ul>
				</div>
				<div class="purchase-groups">
					<div class="purchase-buttons">
						<ul class="tab-list">
							<li class="btn-bid">
								<input type="button" value="구매 입찰" id="purchase_bid">
							</li>
							<li class="btn-direct">
								<c:if test="${saleBidCount != 0}">
									<input type="button" value="즉시 구매" id="purchase_direct">
								</c:if>
								<c:if test="${saleBidCount == 0}">
									<input type="button" value="즉시 구매" id="purchase_direct" disabled>
								</c:if>
							</li>
						</ul>
					</div>
					<div id="price_now">
						<dl id="direct_price">
							<dt>즉시구매가</dt>
							<dd>
								<span><fmt:formatNumber value="${minSaleBid}"/>원</span>
							</dd>
						</dl>
						<dl id="bid_price" style="display:none;" class="bid-input">
							<dt>구매희망가<span id="input_warn"></span></dt>
							<dd>
								<input type="text" placeholder="희망가 입력" id="input_bid" value="${minSaleBid}">
								<span>원</span>
							</dd>
						</dl>
						<div class="purchase-note">
							<p>
								총 결제금액은 다음 화면에서 계산됩니다.
							</p>
						</div>
					</div>
				</div>
				<div id="input_deadline" style="display:none;">
					<div class="deadline-title">	
						입찰 마감기한
					</div>
					<div class="deadline-print">
						<span id="print_deadline"></span>
						<div class="deadline-select">
						<a class="purchase-deadline" data-deadline="1">1일</a>
						<a class="purchase-deadline" data-deadline="3">3일</a>
						<a class="purchase-deadline" data-deadline="5">5일</a>
						<a class="purchase-deadline" data-deadline="7">7일</a>
						<a class="purchase-deadline" data-deadline="10">10일</a>
					</div>
					</div>
				</div>
				<div class="total-confirm">
					<div class="total-price">
						<dl>
							<dt><b>총 결제금액</b></dt>
							<dd>다음 화면에서 확인</dd>
						</dl>
					</div>
					<div id="btn_confirm">
						<input type="button" data-type="1" value="즉시 구매 계속" id="btn_purchase">
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 모달창 -->
<div id="modalWrap">
  <div id="modalContent">
    <div id="modalBody">
      <div class="modal-content">
      	<div class="modal-title">
      		<c:if test="${empty trade.trade_zipcode}">
      			<h2>배송지 입력</h2>
      		</c:if>
      		<c:if test="${!empty trade.trade_zipcode}">
      			<h2>배송지 변경</h2>
      		</c:if>
      	</div>
     		<div class="modal-detail">
      		<div class="delivery-input">
      			<div class="input-box">
      				<h4>우편번호</h4>
      				<div class="input-item">
      					<input type="text" id="zipcode" placeholder="우편 번호를 검색하세요." readonly="readonly">
      					<input type="button" onclick="execDaumPostcode()" value="우편번호 찾기" class="zipcode-btn" >
      				</div>
      				<p class="input-error"></p>
      			</div>
      		</div>
      		<div class="delivery-input">
      			<div class="input-box">
      				<h4>주소</h4>
      				<div class="input-item">
      					<input type="text" id="address1" placeholder="우편 번호 검색후, 자동입력 됩니다." readonly="readonly">
      				</div>
      				<p class="input-error"></p>
      			</div>
      		</div>
      		<div class="delivery-input">
      			<div class="input-box">
      				<h4>상세 주소</h4>
      				<div class="input-item">
      					<input type="text" id="address2" placeholder="건물, 아파트, 동/호수 입력">
      				</div>
      				<p class="input-error"></p>
      			</div>
      		</div>
      	</div>
      	<div class="modal-close">
      		<input type="button" value="저장하기" id="modal_save">
      		<input type="button" value="취소" id="modal_close">
      	</div>
     	</div>
    </div>
  </div>
</div>
<!-- 우편번호 검색 시작 -->
<!-- iOS에서는 position:fixed 버그가 있음, 적용하는 사이트에 맞게 position:absolute 등을 이용하여 top,left값 조정 필요 -->
<div id="layer" style="display:none;position:fixed;overflow:hidden;z-index:5000;-webkit-overflow-scrolling:touch;">
<img src="//t1.daumcdn.net/postcode/resource/images/close.png" id="btnCloseLayer" style="cursor:pointer;position:absolute;right:-3px;top:-3px;z-index:1" onclick="closeDaumPostcode()" alt="닫기 버튼">
</div>

<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    // 우편번호 찾기 화면을 넣을 element
    var element_layer = document.getElementById('layer');

    function closeDaumPostcode() {
        // iframe을 넣은 element를 안보이게 한다.
        element_layer.style.display = 'none';
    }

    function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    //(주의)address1에 참고항목이 보여지도록 수정
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    //(수정) document.getElementById("address2").value = extraAddr;
                
                } 
                //(수정) else {
                //(수정)    document.getElementById("address2").value = '';
                //(수정) }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zipcode').value = data.zonecode;
                //(수정) + extraAddr를 추가해서 address1에 참고항목이 보여지도록 수정
                document.getElementById("address1").value = addr + extraAddr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("address2").focus();

                // iframe을 넣은 element를 안보이게 한다.
                // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
                element_layer.style.display = 'none';
            },
            width : '100%',
            height : '100%',
            maxSuggestItems : 5
        }).embed(element_layer);

        // iframe을 넣은 element를 보이게 한다.
        element_layer.style.display = 'block';

        // iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.
        initLayerPosition();
    }

    // 브라우저의 크기 변경에 따라 레이어를 가운데로 이동시키고자 하실때에는
    // resize이벤트나, orientationchange이벤트를 이용하여 값이 변경될때마다 아래 함수를 실행 시켜 주시거나,
    // 직접 element_layer의 top,left값을 수정해 주시면 됩니다.
    function initLayerPosition(){
        var width = 300; //우편번호서비스가 들어갈 element의 width
        var height = 400; //우편번호서비스가 들어갈 element의 height
        var borderWidth = 5; //샘플에서 사용하는 border의 두께
		
        // 위에서 선언한 값들을 실제 element에 넣는다.
        element_layer.style.width = width + 'px';
        element_layer.style.height = height + 'px';
        element_layer.style.border = borderWidth + 'px solid';
        // 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
        element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width)/2 - borderWidth) + 'px';
        element_layer.style.top = (((window.innerHeight || document.documentElement.clientHeight) - height)/2 - borderWidth) + 'px';
    }
</script>
<!-- 우편번호 검색 끝 -->
