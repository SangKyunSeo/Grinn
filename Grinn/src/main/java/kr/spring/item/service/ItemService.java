package kr.spring.item.service;

import java.util.List;
import java.util.Map;

import kr.spring.item.vo.ItemFavVO;
import kr.spring.item.vo.ItemReviewVO;
import kr.spring.item.vo.ItemTradeVO;
import kr.spring.item.vo.ItemVO;
import kr.spring.item.vo.ItemstVO;
import kr.spring.trade.vo.TradeVO;

public interface ItemService {
	
	//부모글
	public List<ItemVO> selectList(Map<String, Object> map);
	public int selectRowCount(Map<String,Object> map);
	public void insertItem(ItemVO item);
	public ItemVO selectItem(Integer item_num);
	public ItemVO sizeListInfo(Integer item_num);
	//public Integer minSale(Integer item_num);
	public Integer maxPurchase(Integer item_num);
	public Integer latelyTrade(Integer item_num);
	public void updateItem(ItemVO item);
	public void deleteItem(Integer item_num);
	
	public List<TradeVO> tradeList(Integer item_num);
	public List<ItemTradeVO> saleList(Integer item_num); 
	public List<ItemTradeVO> purchaseList(Integer item_num); 
	
	//==관심상품==
	public ItemFavVO selectFav(ItemFavVO fav);
	public int selectFavCount(Integer item_num);
	public void insertFav(ItemFavVO fav);
	public void deleteFav(Integer item_favNum);
	
	// 리뷰
	public List<ItemReviewVO> selectListReview(Map<String, Object> map);
	public int selectRowCountReview(Map<String, Object> map);
	public ItemReviewVO selectReview(Integer review_num);
	public void insertReview(ItemReviewVO itemReview);
	public void updateReview(ItemReviewVO itemReview);
	public void deleteReiew(Integer review_num);
	
	//스타일
	public List<ItemstVO> selectListST(Map<String,Object> map);
	public int selectRowCountST(Map<String,Object> map);
	public int stylecount(Integer item_num);
	
	// 투표
	public List<ItemVO> selectSearchItem(String item_name);
}