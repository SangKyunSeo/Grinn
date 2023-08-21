$(function(){
	/*$('#to').datepicker({
	   dateFormat: 'yy-mm-dd' //Input Display Format 변경
       ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
       ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
       ,changeYear: true //콤보박스에서 년 선택 가능
       ,changeMonth: true //콤보박스에서 월 선택 가능                
       ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
       ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
       ,buttonImageOnly: true //기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
       ,buttonText: "선택" //버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
       ,yearSuffix: "년" //달력의 년도 부분 뒤에 붙는 텍스트
       ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
       ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
       ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
       ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트
       ,minDate:0 //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
       ,maxDate: "+1M" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후) 
	});
	*/
	
	
    // 초기값을 오늘 날짜로 설정
   // $('#from-to').datepicker('setDate', 'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, -1M:한달후, -1Y:일년후)
	
   	
	
	// ===종료 날짜 먼저 선택 시 시작 날짜의 값이 없으면 알림===
	$('#from-to2').on('keyup mouseup', function(){
		if($('#from-to').val() == ''){
			alert('시작일을 먼저 입력하시기 바랍니다.');
			$('#from-to').focus();
			return;
		}
	});
	
	
	$('#get_count').on('keyup mouseup click', function(){
		if($('#get_count').val() == ''){
			$('#booth_total_txt').text('총 금액 0원');
		}
		
		if($('#get_count').val() < 0){
			$('#get_count').val('');
			return;
		}
		
		if($('#get_count').val() >1){
			alert('최대 1개 예약 가능합니다.')
			$('#get_count').val('');
			return;
		}
				
		let total = Number($('#get_count').val()) * $('#booth_fee').val();
		$('#booth_total_txt').text('총 금액 ' + total.toLocaleString() + '원');
		
	});
	         
});