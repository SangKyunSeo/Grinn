package kr.spring.fleaMarket.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.fleaMarket.dao.BookingMapper;
import kr.spring.fleaMarket.vo.BookingVO;

@Service
@Transactional
public class BookingServiceImpl implements BookingService{
	@Autowired
	private BookingMapper bookingMapper;
	
	@Override
	public void insertBooking(BookingVO book) {
		bookingMapper.insertBooking(book);
	}

	@Override
	public int selectTotalByMem_num(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<BookingVO> selectListBooking(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BookingVO selectBooking(BookingVO book) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateBooking(BookingVO book) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBooking(Integer book_num) {
		// TODO Auto-generated method stub
		
	}
}