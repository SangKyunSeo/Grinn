package kr.spring.fleaMarket.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.fleaMarket.vo.BookingVO;

public interface BookingService {
	// 플리마켓 날짜 추출
	public int selectMarketDate(Integer market_num);
	// 예약 등록
	public void insertBooking(BookingVO bookingVO);
	// 회원 번호별 예약 액수
	public int selectTotalByMem_num(Map<String, Object> map); 
	// 사용자 - 예약 목록
	public int selectBookingCountByMem_num(Map<String, Object> map);
	public List<BookingVO> selectListBookingByMem_num(Map<String, Object> map);
	// 관리자 - 예약 목록
	public List<BookingVO> selectListBooking(Map<String, Object> map);
	public int selectCountBooking(Map<String, Object> map);
	// 예약 상세
	public BookingVO selectBooking(Integer book_num);
	// 예약 완료로 인한 booth_count 변동
	public void bookBooth_count(Integer market_num);
	// 예약 삭제
	public void deleteBooking(Integer book_num);
}
