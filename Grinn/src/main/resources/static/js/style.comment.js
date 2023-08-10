$(function(){
	let rowCount = 10;
	let currentPage;
	let count;
	
	//댓글목록
	function selectList(pageNum){
		currentPage = pageNum;

		//서버와 통신
		$.ajax({
			url:'listComment.do',
			type:'post',
			data:{pageNum:pageNum,rowCount:rowCount,st_num:$('#st_num').val()},
			dataType:'json',
			success:function(param){
				count = param.count;
				
				if(pageNum == 1){
					//처음 호출시는 해당 ID의 div의 내부 내용물을 제거
					$('#output').empty();
				}
				
				//댓글수 읽어오기(이후 추가)
				displayCommentCount(param);
				
				//댓글목록 처리
				$(param.list).each(function(index, item){
					let output = '<div class="item">';
					output += '<ul class="detail-info">';
					output += '<li>';
					output += '<img src="../style/viewProfileByCom_num.do?com_num=' + item.com_num + '" width="40" height="40" class="my-photo">';
					output += '</li>';
					output += '<li>';
					output += item.mem_id + '<br>';
					
					
					if(item.com_mdate!=null){
						output += '<span class="modify-date">' + item.com_mdate + '</span>';
					}else{
						output += '<span class="modify-date">' + item.com_regdate + '</span>';
					}
					
					output += '</li>';
					output += '</ul>';
					output += '<div class="sub-item">';
					output += '<p>' + item.com_comment.replace(/\r\n/g, '<br>') + '</p>';
					
					if(param.user_num == item.mem_num){
						//로그인한 회원번호와 댓글작성자 회원번호가 같을 때
						output += '<div class="com_btn">';
						output += '<input type="button" data-num="' + item.com_num + '" value="수정" class="modify-btn">';
						output += ' <input type="button" data-num="' + item.com_num + '" value="삭제" class="delete-btn">';
						output += '</div>';
					}else{
						output += '<div class="com_btn">';
						output += ' <a style="color:red;">신고</a>';
						output += '</div>';
					}
					output += '<hr size="1" color="ebebeb">';
					output += '</div>';
					output += '</div>';
					
					//문서 객체 추가
					$('#output').append(output);
				});
				
				//paging button 처리
				if(currentPage >= Math.ceil(count/rowCount)){
					//다음페이지 없음
					$('.paging-button').hide();
				}else{
					//다음 페이지 존재
					$('.paging-button').show();
				}
			},
		});		
	}
	//다음 댓글 보기 버튼 클릭시 데이터 추가
	$('.paging-button input').click(function(){
		selectList(currentPage + 1);
	});	
	//댓글 등록
	$('#com_form').submit(function(event){
		if($('#com_comment').val().trim() == ''){
			alert('내용을 입력하세요!');
			$('#com_comment').val('').focus();
			return false;
		}
		
		//입력한 정보 읽기
		let form_data = $(this).serialize();
		
		//서버와 통신
		$.ajax({
			url:'writeComment.do',
			type:'post',
			data:form_data,
			dataType:'json',
			success:function(param){
				if(param.result == 'logout'){
					alert('로그인해야 작성할 수 있습니다.');
				}else if(param.result == 'success'){
					//폼 초기화
					initForm();
					//댓글 작성이 성공하면 새로 삽입한 글을 초함해서 첫번째 페이지의 게시글을 다시 호출함
					selectList(1);
				}else{
					alert('댓글 등록 오류 발생');
				}
			},
			error:function(){
				alert('네트워크 오류 발생');
			}
		});
		
		//기본 이벤트 제거
		event.preventDefault();
	});
	
	//댓글 작성 폼 초기화
	function initForm(){
		$('textarea').val('');
	}
	
	//댓글 수정폼 호출
	$(document).on('click', '.modify-btn', function(){
		//댓글번호
		let com_num = $(this).attr('data-num');
		//댓글내용
		let content = $(this).parent().parent().find('p').html().replace(/<br>/gi, '\r\n');
		
		//댓글 수정폼 UI
		let modifyUI = '<form id="mre_form">';
		modifyUI += '<input type="hidden" name="com_num" id="mre_num" value="' + com_num + '">';
		modifyUI += '<textarea row="1" cols="28" name="com_comment" id="mre_content" class="rep-content">' + content + '</textarea>';
		modifyUI += '<div id="mre_second" class="com_btn">';
		modifyUI += ' <input type="submit" value="수정">';
		modifyUI += ' <input type="button" value="취소" class="re-reset">';
		modifyUI += '</div>';
		modifyUI += '<hr size="1" noshade width="96%" color="#f4f4f4">';
		modifyUI += '</form>';
		
		//이전에 이미 수정하는 댓글이 있을 경우 수정버튼을 클릭하면 숨길 sub-item을 환원시키고 수정폼을 초기화함
		initModifyForm();
		
		//지금 클릭해서 수정하고자 하는 데이터는 감추기
		//수정버튼을 감싸고 있는 div
		$(this).parent().hide();
		
		//수정폼을 수정하고 하는 데이터가 있는 div에 노출
		$(this).parents('.item').append(modifyUI);
		
		//입력한 글자수 세팅
		let inputLength = $('#mre_content').val().length;
		let remain = 300 - inputLength;
		remain += '/300';
		
		//문서 객체에 반영
		$('#mre_first .letter-count').text(remain);
	});
	
	//수정폼에서 취소 버튼 클릭시 수정폼 초기화
	$(document).on('click', '.re-reset', function(){
		initModifyForm();
	});
	
	//댓글수정폼 초기화
	function initModifyForm(){
		$('.sub-item').show();
		$('.com_btn').show();
		$('#mre_form').remove();
	};
	
	//댓글수정
	$(document).on('submit', '#mre_form', function(event){
		if($('#mre_content').val().trim() == ''){
			alert('내용을 입력하세요!');
			$('#mre_content').val('').focus();
			return false;
		}
		//폼에 입력한 데이터 반환
		let form_data = $(this).serialize();
		
		//서버와 통신
		$.ajax({
			url:'updateComment.do',
			type:'post',
			data:form_data,
			dataType:'json',
			success:function(param){
				if(param.result == 'logout'){
					alert('로그인 후 이용가능합니다.');
				}else if(param.result == 'success'){
					//수정한 내용처리
					$('#mre_form').parent().find('p').html($('#mre_content').val().replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/\r\n/g, '<br>').replace(/\r/g, '<br>').replace(/\n/g, '<br>'));
					//최근 수정일 처리
					$('#mre_form').find('.modify-date').text('최근 수정일 : 5초미만');
					//수정폼 초기화
					initModifyForm();
				}else if(param.result == 'wrongAccess'){
					alert('타인의 글은 수정할 수 없습니다.');
				}else{
					alert('댓글 수정 오류 발생');
				}
			},
			error:function(){
				alert('네트워크 오류 발생');
			}
		});
		
		//기본이벤트 제거
		event.preventDefault();
		
	});
	
	//댓글삭제
	$(document).on('click', '.delete-btn', function(){
		//댓글번호
		let com_num = $(this).attr('data-num');
		//서버와 통신
		$.ajax({
			url:'deleteComment.do',
			type:'post',
			data:{com_num:com_num},
			dataType:'json',
			success:function(param){
				if(param.result == 'logout'){
					alert('로그인 후 삭제 가능합니다.');
				}else if(param.result == 'success'){
					alert('삭제되었습니다.');
					selectList(1);
				}else if(param.result == 'wrongAccess'){
					alert('타인의 글을 삭제할 수 없습니다.');
				}else{
					alert('댓글 삭제 오류 발생');
				}
			},
			error:function(){
				alert('네트워크 오류 발생');
			}
		});
	});
	
	//댓글수 표시
	function displayCommentCount(data){
		let count = data.count;
		let output;
		if(count == 0){
			output = '댓글 수 (0)';
		}else{
			output = '댓글수 (' + count + ')';
		}
		//문서 객체에 추가
		$('#output_rcount').text(output);
	}
	
	//초기데이터(목록) 호출
	selectList(1);
});