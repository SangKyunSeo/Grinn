package kr.spring.trade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.itemsize.vo.ItemSizeVO;
import kr.spring.pbid.vo.PurchaseBidVO;
import kr.spring.pbid.vo.PurchaseSizePriceVO;
import kr.spring.sbid.vo.SaleBidVO;
import kr.spring.sbid.vo.SaleSizePriceVO;
import kr.spring.trade.dao.TradeMapper;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertSaleBid(SaleBidVO saleBidVO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePurchaseBid(Integer purchase_num) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteSaleBid(Integer sale_num) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int selectGetPurchaseBidByItemAndSize(Integer item_num, Integer item_sizenum) {
		return tradeMapper.selectGetPurchaseBidByItemAndSize(item_num, item_sizenum);
	}

	@Override
	public int selectGetSaleBidByItemAndSize(Integer item_num, Integer item_sizenum) {
		return tradeMapper.selectGetSaleBidByItemAndSize(item_num, item_sizenum);
	}

}
