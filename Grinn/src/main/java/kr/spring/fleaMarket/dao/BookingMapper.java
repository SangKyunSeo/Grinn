package kr.spring.fleaMarket.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import kr.spring.fleaMarket.vo.BookingVO;

@Mapper
public interface BookingMapper {
	// 예약 등록
	public void insertBooking(BookingVO book);
	// 회원 번호별 예약 액수
	public int selectTotalByMem_num(Map<String, Object> map); 
	// 예약 목록
	public List<BookingVO> selectListBooking(Map<String, Object> map);
	// 예약 상세
	public BookingVO selectBooking(BookingVO book);
	// 예약 삭제
	public void deleteBooking(Integer book_num);
	// 예약 삭제에 따른 booth_count 증가
	public void updateBooth(Integer market_num);
}