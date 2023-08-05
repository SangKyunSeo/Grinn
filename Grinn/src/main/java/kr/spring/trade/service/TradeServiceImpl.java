package kr.spring.trade.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.itemsize.vo.ItemSizeVO;
import kr.spring.pbid.vo.PurchaseBidVO;
import kr.spring.pbid.vo.PurchaseSizePriceVO;
import kr.spring.sbid.vo.SaleBidVO;
import kr.spring.sbid.vo.SaleSizePriceVO;
import kr.spring.trade.dao.TradeMapper;
import kr.spring.trade.vo.TradeVO;

@Service
@Transactional
public class TradeServiceImpl implements TradeService{

	@Autowired
	TradeMapper tradeMapper;
	
	@Override
	public List<ItemSizeVO> selectGetItemSize(Integer item_num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PurchaseBidVO> selectGetPurchaseBidByItem(Integer item_num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SaleBidVO> selectGetSaleBidByItem(Integer item_num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int selectMinSaleBid(Integer item_num,Integer item_sizenum) {
		return tradeMapper.selectMinSaleBid(item_num, item_sizenum);
	}

	@Override
	public int selectMaxPurchaseBid(Integer item_num,Integer item_sizenum) {
		return tradeMapper.selectMaxPurchaseBid(item_num, item_sizenum);
	}

	@Override
	public List<PurchaseSizePriceVO> selectPurchaseSizePrice(Integer item_num) {
		return tradeMapper.selectPurchaseSizePrice(item_num);
	}

	@Override
	public List<SaleSizePriceVO> selectSaleSizePrice(Integer item_num) {
		return tradeMapper.selectSaleSizePrice(item_num);
	}

	@Override
	public void insertPurchaseBid(PurchaseBidVO purchaseBidVO) {
		tradeMapper.insertPurchaseBid(purchaseBidVO);
	}

	@Override
	public void insertSaleBid(SaleBidVO saleBidVO) {
		tradeMapper.insertSaleBid(saleBidVO);
		
	}

	@Override
	public void deletePurchaseBid(Integer purchase_num) {
		tradeMapper.deletePurchaseBid(purchase_num);
	}

	@Override
	public void deleteSaleBid(Integer sale_num) {
		tradeMapper.deleteSaleBid(sale_num);
	}

	@Override
	public int selectGetPurchaseBidByItemAndSize(Integer item_num, Integer item_sizenum) {
		return tradeMapper.selectGetPurchaseBidByItemAndSize(item_num, item_sizenum);
	}

	@Override
	public int selectGetSaleBidByItemAndSize(Integer item_num, Integer item_sizenum) {
		return tradeMapper.selectGetSaleBidByItemAndSize(item_num, item_sizenum);
	}

	@Override
	public PurchaseBidVO selectPurchaseBidByUserNum(Integer mem_num,Integer item_num) {
		return tradeMapper.selectPurchaseBidByUserNum(mem_num,item_num);
	}

	@Override
	public SaleBidVO selectSaleBidByUserNum(Integer mem_num,Integer item_num) {
		return tradeMapper.selectSaleBidByUserNum(mem_num,item_num);
	}

	@Override
	public int selectSellerNum(Integer item_num, Integer item_sizenum, Integer sale_price) {
		return tradeMapper.selectSellerNum(item_num, item_sizenum, sale_price);
	}

	@Override
	public int selectBuyerNum(Integer item_num, Integer item_sizenum, Integer purchase_price) {
		return tradeMapper.selectBuyerNum(item_num, item_sizenum, purchase_price);
	}

	@Override
	public int selectTradeNum() {
		return tradeMapper.selectTradeNum();
	}

	@Override
	public void insertTrade(TradeVO trade) {
		tradeMapper.insertTrade(trade);
		tradeMapper.insertTradeDetail(trade);
		
	}

	@Override
	public int selectPurchaseBidNumber(Integer mem_num, Integer item_num, Integer purchase_price) {
		return tradeMapper.selectPurchaseBidNumber(mem_num, item_num, purchase_price);
	}

	@Override
	public int selectSaleBidNumber(Integer mem_num, Integer item_num, Integer sale_price) {
		return tradeMapper.selectSaleBidNumber(mem_num, item_num, sale_price);
	}

	@Override
	public List<TradeVO> selectTradePurchaseInfo(Map<String,Object> map) {
		return tradeMapper.selectTradePurchaseInfo(map);
	}
	
	@Override
	public List<TradeVO> selectTradeSaleInfo(Map<String,Object> map) {
		return null;
	}

	@Override
	public List<PurchaseBidVO> selectPurchaseBidInfo(Map<String, Object> map) {
		return tradeMapper.selectPurchaseBidInfo(map);
	}

	@Override
	public List<SaleBidVO> selectSaleBidInfo(Map<String, Object> map) {
		return null;
	}

	@Override
	public int selectTradePurchaseCount(Integer mem_num) {
		return tradeMapper.selectTradePurchaseCount(mem_num);
	}
	
	@Override
	public int selectTradeSaleCount(Integer mem_num) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int selectPurchaseBidCount(Integer mem_num) {
		return tradeMapper.selectPurchaseBidCount(mem_num);
	}

	@Override
	public int selectSaleBidCount(Integer mem_num) {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
