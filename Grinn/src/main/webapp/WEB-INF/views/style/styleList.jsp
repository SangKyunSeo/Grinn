<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/style.fav.js"></script>
    <style>
        /* Flexbox를 사용하여 가로 정렬 */
        .style_list {
            display: flex;
            flex-wrap: wrap; /* 아이템들을 여러 줄에 걸쳐 표시할 수 있도록 설정 */
            margin-left: 50px;
            
        }

        .style-item {
            margin: 10px;
            text-align: left;
            max-width: 200px;
        }

        .style-item img {
            border-radius: 5px;
            max-width: 100%;
            height: auto;
        }

        .user-profile {
            display: flex;
            align-items: center;
            margin-bottom: 5px;
            float:left;
        }

        .profile-photo img{
              border-radius: 50%; /* 50%로 설정하여 사진을 둥글게 만듭니다 */
			  width: 30px; /* 필요한 크기로 조정하세요 */
			  height: 30px; /* 필요한 크기로 조정하세요 */
			  overflow: hidden; /* 둥글게 잘린 부분을 숨김 처리합니다 */
        }
        
        .mem-id{
        	font-size: 17px;
        }
        
        .like-button{
        	width: 20px;
            height: 20px;
            float:right;
            margin-bottom: 10px;
        }
        .phrase{
        	clear:both;
        }
        .output-fav{
        	cursor: pointer;
        }
        .align-center{
        	margin:0 auto;
        	text-align:center;
        }
    </style>
<script type="text/javascript">
$(document).ready(function() {
    // 좋아요 읽기
    // 좋아요 선택 여부와 선택한 총개수 표시
    function selectFav(st_num) {
        $.ajax({
            url: 'getFav.do',
            type: 'post',
            data: { st_num: st_num },
            dataType: 'json',
            success: function(param) {
                displayFav(param, st_num);
            },
            error: function() {
                alert('네트워크 오류 발생');
            }
        });
    }

    // 좋아요 등록/삭제
    $(document).on('click', '.output-fav', function() {
        let st_num = $(this).data('num');
        $.ajax({
            url: 'writeFav.do',
            type: 'post',
            data: { st_num: st_num },
            dataType: 'json',
            success: function(param) {
                if (param.result == 'logout') {
                    alert('로그인 후 이용 가능합니다.');
                    location.href = '/member/login.do';
                } else if (param.result == 'success') {
                    displayFav(param, st_num);
                } else {
                    alert('등록시 오류 발생');
                }
            },
            error: function() {
                alert('네트워크 오류 발생');
            }
        });
    });

    // 좋아요 표시 공통 함수
    function displayFav(param, st_num) {
        let output;
        if (param.status == 'yesFav') {
            output = '../images/yes_like.png';
        } else if (param.status == 'noFav') {
            output = '../images/no_like.png';
        } else {
            alert('좋아요 표시 오류 발생');
        }
        // 문서 객체에 추가
        $('.output-fav[data-num="' + st_num + '"]').attr('src', output);
    }

    // 초기 데이터 표시
    $('.output-fav').each(function() {
        selectFav($(this).data('num'));
    });
});

//스타일 검색
$(function(){
	$('input[type="search"]').attr('placeholder','ID, 상품태그, 내용 검색');
	
	function searchData(){
		let data = $('input[type="search"]').val();
		if(data == ''){
			alert('검색어를 입력하세요.');
			$('input[type="search"]').val('').focus();
			return false;
		}
		location.href="list.do?keyword="+data;
	};
	
	$('input[type="search"]').keypress(function(){
		if(event.keyCode==13){
			searchData();	
		}
		
	});
});
</script>
    <div class="page-main">
    	<div class="align-right">
    		<a href="write.do"><span style="font-size:30pt;">&#x1F4F7</span></a>
    	</div>
    	<c:if test="${count > 0}">
    	<div class="align-right">
    		<a href="${pageContext.request.contextPath}/style/list.do?order=1">
    			<c:if test="${order == 1}"><b>최신순</b></c:if>
    			<c:if test="${order != 1}">최신순</c:if>
    		</a> |
    		<a href="${pageContext.request.contextPath}/style/list.do?order=2">
    			<c:if test="${order == 2}"><b>인기순</b></c:if>
    			<c:if test="${order != 2}">인기순</c:if>
    		</a>
    	</div>
    	</c:if>
    	<div class="style_list">
    		<c:if test="${count == 0}">
				<div class="align-center">검색 결과가 없습니다.</div>
			</c:if>
    		<c:forEach var="style" items="${list}">
            	<div class="style-item">
                	<a href="detail.do?st_num=${style.st_num}">
                    	<img src="${pageContext.request.contextPath}/style/viewPhoto1.do?st_num=${style.st_num}" width="300" height="300">
                	</a>
                	<div class="user-profile">
                	<a href="/user/userStyle.do?mem_num=${style.mem_num}">
                	<span class="profile-photo">
                		<img src="${pageContext.request.contextPath}/style/viewProfile.do?st_num=${style.st_num}">
                	</span>
    				<span class="mem-id">${style.mem_id}</span>
    				</a>
					</div>
                    <div class="like-button">
                    	<img class="output-fav" data-num="${style.st_num}" src="${pageContext.request.contextPath}/images/no_like.png">
                    </div>
                    <br>
                    <div class="phrase">
                    	<p>${style.st_phrase}</p>
                    </div>
            	</div>
        	</c:forEach>
    	</div>
    	<c:if test="${count > 0}">
    	<div class="align-center">${page}</div>
    	</c:if>
    </div>