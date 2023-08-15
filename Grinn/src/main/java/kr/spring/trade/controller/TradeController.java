package kr.spring.trade.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import kr.spring.item.service.ItemService;
import kr.spring.item.vo.ItemVO;
import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import kr.spring.pbid.vo.PurchaseBidVO;
import kr.spring.pbid.vo.PurchaseSizePriceVO;
import kr.spring.sbid.vo.SaleBidVO;
import kr.spring.sbid.vo.SaleSizePriceVO;
import kr.spring.trade.service.TradeService;
import kr.spring.trade.vo.TradeVO;
import kr.spring.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class TradeController {
	@Autowired
	TradeService tradeService;
	@Autowired
	MemberService memberService;
	@Autowired
	ItemService itemService;
	/**
	 * ======================================================================================================================
	 * 자바 빈 초기화
	 * ======================================================================================================================
	 **/
	@ModelAttribute
	public TradeVO initCommand() {
		return new TradeVO();
	}

	/**
	 * ======================================================================================================================
	 * 구매하기 버튼 클릭 시 상품에 따른 사이즈 정보 제공
	 * ======================================================================================================================
	 **/
	@GetMapping("/purchase/selectSize.do")
	public ModelAndView getPurchaseItemAndSize(@RequestParam int item_num) {

		// Login인터셉터로 로그인 되어있는지 확인필요 ***********

		// 사이즈 별 판매 입찰 정보를 뿌리기
		log.debug("<< item_num >> : " + item_num);
		List<SaleSizePriceVO> sspList = tradeService.selectSaleSizePrice(item_num);
		log.debug("<<sspList 크기 >> : " + sspList.size());
		log.debug("<<test>> : " + sspList.get(0));
		ModelAndView mav = new ModelAndView();
		mav.setViewName("selectSize");
		mav.addObject("list", sspList);

		// 아이템 번호로 아이템 정보 구하기 ***********
		ItemVO item = itemService.selectItem(item_num);
		mav.addObject("item",item);
		return mav;
	}

	/**
	 * ======================================================================================================================
	 * 사이즈 버튼 클릭 시 구매 동의 화면으로 이동
	 * ======================================================================================================================
	 **/
	@GetMapping("/purchase/check.do")
	public String getPurchaseAgree(@RequestParam int item_num, @RequestParam int item_sizenum,
			@RequestParam String item_size, Model model, HttpSession session) {

		// Login인터셉터로 로그인 되어있는지 확인필요 ***********

		// 아이템 번호로 아이템 정보 구해서 넘기기 *********
		ItemVO item = itemService.selectItem(item_num);

		model.addAttribute("item_num", item_num); // item 정보를 구하게 된다면 vo 객체를 넘겨줄거임
		model.addAttribute("item_sizenum", item_sizenum);
		model.addAttribute("item_size", item_size);
		model.addAttribute("item",item);
		return "check";
	}

	/**
	 * ======================================================================================================================
	 * 구매 : 구매 계속 버튼 클릭 시 구매 관련 상세 정보 처리
	 * ======================================================================================================================
	 **/
	// 구매 기본 화면 구성
	@GetMapping("/purchase/purchaseDetail.do")
	public String getPurchaseDetail(@RequestParam int item_num, @RequestParam int item_sizenum,
			@RequestParam String item_size, Model model, HttpSession session) {

		// Login인터셉터로 로그인 되어있는지 확인 필요 ****************

		// 판매입찰 정보가 등록되어 있는지 확인
		int saleBidCount = tradeService.selectGetSaleBidByItemAndSize(item_num, item_sizenum);
		// 구매입찰 정보가 등록되어 있는지 확인
		int purchaseBidCount = tradeService.selectGetPurchaseBidByItemAndSize(item_num, item_sizenum);
		// 즉시 구매가 정보 넘기기
		int minSaleBid = 0;
		if (saleBidCount > 0) {
			minSaleBid = tradeService.selectMinSaleBid(item_num, item_sizenum);
		} else {
			minSaleBid = 0; // 입찰 정보가 없기 때문에 아이템 정가가 들어가야함 *********************************
		}

		// 즉시 판매가 정보 넘기기
		int maxPurchaseBid = 0;
		if (purchaseBidCount > 0) {
			maxPurchaseBid = tradeService.selectMaxPurchaseBid(item_num, item_sizenum);
		} else {
			maxPurchaseBid = 0; // 입찰 정보가 없기 때문에 아이템 정가가 들어가야함 *********************************
		}

		model.addAttribute("minSaleBid", minSaleBid); // 즉시 구매가
		model.addAttribute("maxPurchaseBid", maxPurchaseBid); // 즉시 판매가
		model.addAttribute("item_num", item_num);
		model.addAttribute("item_size", item_size);
		model.addAttribute("item_sizenum", item_sizenum);
		model.addAttribute("saleBidCount", saleBidCount);
		// 아이템 번호로 아이템 정보 구해서 넘기기 *********
		ItemVO item = itemService.selectItem(item_num);
		model.addAttribute("item",item);
		// 구매자 정보 넘기기 **************
		return "purchaseDetail";
	}

	// 구매 입찰 버튼 클릭 시 통신
	@RequestMapping("/purchase/paymentPurchaseBid.do")
	@ResponseBody
	public Map<String, Object> getPaymentPurchaseBid(@RequestParam int minSaleBid, @RequestParam int item_num,
			@RequestParam int item_sizenum, @RequestParam String item_size, @RequestParam String deadline,
			@RequestParam int purchase_price, @RequestParam String dateDeadline, @RequestParam String type,
			HttpSession session) {
		Map<String, Object> mapJson = new HashMap<String, Object>();
		// 로그인 되어있는지 체크
		MemberVO user = (MemberVO) session.getAttribute("user");

		// 구매 입찰을 등록했는지 체크
		PurchaseBidVO pbVO = null;

		if (user == null) {
			mapJson.put("result", "logout");
		} else {
			pbVO = tradeService.selectPurchaseBidByUserNum(user.getMem_num(), item_num);
			MemberVO member = memberService.selectMember(user.getMem_num());
			if (pbVO == null || type.equals("1")) {
				log.debug("<<dateDeadline >> : " + dateDeadline);
				log.debug("<<purchase_price >> : " + purchase_price);
				log.debug("<<minSaleBid >> : " + minSaleBid);
				mapJson.put("minSaleBid", minSaleBid);
				mapJson.put("item_num", item_num);
				mapJson.put("item_sizenum", item_sizenum);
				mapJson.put("deadline", Integer.parseInt(deadline));
				mapJson.put("purchase_price", purchase_price);
				mapJson.put("member", member);
				mapJson.put("dateDeadline", dateDeadline);
				mapJson.put("result", "success");
				mapJson.put("type", type);
			} else {
				mapJson.put("result", "duplicated");
			}
		}
		return mapJson;
	}

	// 구매입찰 - 결제하기 버튼 클릭 시
	@PostMapping("/purchase/purchasePaymentBid.do")
	public String paymentBidFinish(PurchaseBidVO pbVO, @RequestParam int total, Model model, HttpSession session) {
		MemberVO user = (MemberVO) session.getAttribute("user");

		if (user == null) {
			model.addAttribute("message", "로그인이 필요합니다!");
			model.addAttribute("url", "../member/login.do");
		} else {
			pbVO.setMem_num(user.getMem_num());
			// total로 결제 금액 결제 진행 하기 **************************************************************
			MemberVO member = memberService.selectMember(user.getMem_num());
			
			if(member.getMem_point() - total < 0) {
				model.addAttribute("message","잔액이 부족합니다. 금액을 충전해주세요.");
				model.addAttribute("url","../item/itemList.do");
				return "common/resultView";
			}else {
				tradeService.executePayment(total); // 관리자에게 입금
				// 포인트 차감 진행.
				tradeService.spendPoint(user.getMem_num(), total);
				tradeService.insertPurchaseBid(pbVO);
				
			}
			model.addAttribute("message", "[구매입찰] 결제가 완료되었습니다.");
			model.addAttribute("url", "../item/itemList.do");
		}

		return "common/resultView";
	}

	// 즉시구매 - 결제하기 버튼 클릭 시
	@PostMapping("/purchase/purchasePaymentDirect.do")
	public String paymentDirectFinish(TradeVO tradeVO, @RequestParam int total, Model model, HttpSession session) {
		MemberVO user = (MemberVO) session.getAttribute("user");

		if (user == null) {
			model.addAttribute("message", "로그인이 필요합니다!");
			model.addAttribute("url", "../member/login.do");
		}else{
			int seller_num = tradeService.selectSellerNum(tradeVO.getItem_num(), tradeVO.getItem_sizenum(), tradeVO.getTrade_price());
			if(seller_num == user.getMem_num()) {
				model.addAttribute("message","본인이 등록한 입찰 정보로 즉시 구매 할 수 없습니다.");
				model.addAttribute("url","../item/itemList.do");
				return "common/resultView";
			}else {
				MemberVO member = memberService.selectMember(user.getMem_num());
				
				if(total > member.getMem_point()) {
					model.addAttribute("message","잔액이 부족합니다. 금액을 충전해주세요.");
					model.addAttribute("url","../item/itemList.do");
					return "common/resultView";
				}
				// 거래 정보 저장을 위한 거래 번호 생성
				int trade_num = tradeService.selectTradeNum();
				int mem_num = seller_num;
				int sale_num = tradeService.selectSaleBidNumber(mem_num, tradeVO.getItem_num(), tradeVO.getTrade_price());
				tradeVO.setTrade_num(trade_num);
				tradeVO.setBuyer_num(user.getMem_num());
				tradeVO.setSeller_num(seller_num);
				
				// 관리자에게 입금
				tradeService.executePayment(total);
				tradeService.spendPoint(user.getMem_num(), total);
				
				// 구매입찰을 등록했는데 즉시구매를 했을 경우
				PurchaseBidVO pbVO = tradeService.selectPurchaseBidForDirectBuy(user.getMem_num(), tradeVO.getItem_num(), tradeVO.getItem_sizenum());
				if(pbVO.getMem_num() == user.getMem_num()) {
					// 입금했던 거래가 다시 반환
					int deposit = 0;
					if(pbVO.getPurchase_price() < 50000) {
						deposit += 3000;
					}
					deposit += (pbVO.getPurchase_price() / 10);
					deposit += pbVO.getPurchase_price();
					// 사용자 포인트 반환
					tradeService.sendPointToBuyer(user.getMem_num(), deposit);
					// 관리자 포인트 차감
					tradeService.cancelExecutePayment(deposit);
					
					// 구매 입찰 정보 삭제
					tradeService.deletePurchaseBid(pbVO.getPurchase_num());
				}

				tradeService.insertTrade(tradeVO);
				// 판매입찰 정보 삭제
				tradeService.deleteSaleBid(sale_num);
				model.addAttribute("message", "[즉시구매] 결제를 완료했습니다.");
				model.addAttribute("url", "../item/itemList.do");
			}
		}
		return "common/resultView";
	}

	/**
	 * ======================================================================================================================
	 * 판매하기 버튼 클릭 시 상품에 따른 사이즈 정보 제공
	 * ======================================================================================================================
	 **/
	@GetMapping("/sale/selectSize.do")
	public ModelAndView getSaleItemAndSize(@RequestParam int item_num) {

		// Login인터셉터로 로그인 되어있는지 확인필요 ***********

		// 사이즈 별 구매 입찰 정보를 뿌리기
		log.debug("<< item_num >> : " + item_num);
		List<PurchaseSizePriceVO> pspList = tradeService.selectPurchaseSizePrice(item_num);
		log.debug("<<sspList 크기 >> : " + pspList.size());
		log.debug("<<test>> : " + pspList.get(0));
		ModelAndView mav = new ModelAndView();
		mav.setViewName("selectSaleSize");
		mav.addObject("list", pspList);

		// 아이템 번호로 아이템 정보 구하기 ***********
		ItemVO item = itemService.selectItem(item_num);
		mav.addObject("item",item);
		return mav;
	}

	/**
	 * ======================================================================================================================
	 * 사이즈 버튼 클릭 시 판매 동의 화면으로 이동
	 * ======================================================================================================================
	 **/
	@GetMapping("/sale/checkSale.do")
	public String getSaleAgree(@RequestParam int item_num, @RequestParam int item_sizenum,
			@RequestParam String item_size, Model model, HttpSession session) {

		// Login인터셉터로 로그인 되어있는지 확인필요 ***********

		// 아이템 번호로 아이템 정보 구해서 넘기기 *********
		ItemVO item = itemService.selectItem(item_num);

		model.addAttribute("item_num", item_num); // item 정보를 구하게 된다면 vo 객체를 넘겨줄거임
		model.addAttribute("item_sizenum", item_sizenum);
		model.addAttribute("item_size", item_size);
		model.addAttribute("item",item);
		return "checkSale";
	}

	/**
	 * ======================================================================================================================
	 * 판매 : 판매 계속 버튼 클릭 시 판매 관련 상세 정보 처리
	 * ======================================================================================================================
	 **/
	// 판매 기본 화면 구성
	@GetMapping("/sale/saleDetail.do")
	public String getSaleDetail(@RequestParam int item_num, @RequestParam int item_sizenum,
			@RequestParam String item_size, Model model, HttpSession session) {

		// Login인터셉터로 로그인 되어있는지 확인 필요 ****************

		// 판매입찰 정보가 등록되어 있는지 확인
		int saleBidCount = tradeService.selectGetSaleBidByItemAndSize(item_num, item_sizenum);
		// 구매입찰 정보가 등록되어 있는지 확인
		int purchaseBidCount = tradeService.selectGetPurchaseBidByItemAndSize(item_num, item_sizenum);
		// 즉시 구매가 정보 넘기기
		int minSaleBid = 0;
		if (saleBidCount > 0) {
			minSaleBid = tradeService.selectMinSaleBid(item_num, item_sizenum);
		} else {
			minSaleBid = 0; // 입찰 정보가 없기 때문에 아이템 정가가 들어가야함 *********************************
		}

		// 즉시 판매가 정보 넘기기
		int maxPurchaseBid = 0;
		if (purchaseBidCount > 0) {
			maxPurchaseBid = tradeService.selectMaxPurchaseBid(item_num, item_sizenum);
		} else {
			maxPurchaseBid = 0; // 입찰 정보가 없기 때문에 아이템 정가가 들어가야함 *********************************
		}

		model.addAttribute("minSaleBid", minSaleBid); // 즉시 구매가
		model.addAttribute("maxPurchaseBid", maxPurchaseBid); // 즉시 판매가
		model.addAttribute("item_num", item_num);
		model.addAttribute("item_size", item_size);
		model.addAttribute("item_sizenum", item_sizenum);
		model.addAttribute("purchaseBidCount", purchaseBidCount);
		// 아이템 번호로 아이템 정보 구해서 넘기기 *********
		ItemVO item = itemService.selectItem(item_num);
		model.addAttribute("item",item);
		// 구매자 정보 넘기기 **************
		return "saleDetail";
	}

	// 판매 입찰 버튼 클릭 시 통신
	@RequestMapping("/sale/paymentSaleBid.do")
	@ResponseBody
	public Map<String, Object> getPaymentSaleBid(@RequestParam int maxPurchaseBid, 
												 @RequestParam int item_num,
												 @RequestParam int item_sizenum, 
												 @RequestParam String item_size, 
												 @RequestParam String deadline,
												 @RequestParam int sale_price, 
												 @RequestParam String dateDeadline, 
												 @RequestParam String type,
												 HttpSession session) {
		
		Map<String, Object> mapJson = new HashMap<String, Object>();
		// 로그인 되어있는지 체크
		MemberVO user = (MemberVO) session.getAttribute("user");

		// 판메 입찰을 등록했는지 체크
		SaleBidVO sbVO = null;

		if (user == null) {
			mapJson.put("result", "logout");
		} else {
			sbVO = tradeService.selectSaleBidByUserNum(user.getMem_num(), item_num);
			MemberVO member = memberService.selectMember(user.getMem_num());
			if (sbVO == null || type.equals("1")) {
				log.debug("<<dateDeadline >> : " + dateDeadline);
				log.debug("<<sale_price >> : " + sale_price);
				log.debug("<<maxPurchaseBid >> : " + maxPurchaseBid);
				mapJson.put("maxPurchaseBid", maxPurchaseBid);
				mapJson.put("item_num", item_num);
				mapJson.put("item_sizenum", item_sizenum);
				mapJson.put("deadline", Integer.parseInt(deadline));
				mapJson.put("sale_price", sale_price);
				mapJson.put("member", member);
				mapJson.put("dateDeadline", dateDeadline);
				mapJson.put("result", "success");
				mapJson.put("type", type);
			} else {
				mapJson.put("result", "duplicated");
			}
		}
		return mapJson;
	}
	// 판매입찰 - 판매입찰하기 버튼 클릭 시
	@PostMapping("/sale/salePaymentBid.do")
	public String saleBidFinish(SaleBidVO sbVO, @RequestParam int total, Model model, HttpSession session) {
		MemberVO user = (MemberVO) session.getAttribute("user");

		if (user == null) {
			model.addAttribute("message", "로그인이 필요합니다!");
			model.addAttribute("url", "../member/login.do");
		} else {
			sbVO.setMem_num(user.getMem_num());
			tradeService.insertSaleBid(sbVO);

			model.addAttribute("message", "[판매입찰] 완료되었습니다.");
			model.addAttribute("url", "../item/itemList.do");
		}

		return "common/resultView";
	}

	// 즉시판매 - 즉시판매하기 버튼 클릭 시
	@PostMapping("/sale/salePaymentDirect.do")
	public String saleDirectFinish(TradeVO tradeVO, @RequestParam int purchase_price, Model model, HttpSession session) {
		MemberVO user = (MemberVO) session.getAttribute("user");

		if (user == null) {
			model.addAttribute("message", "로그인이 필요합니다!");
			model.addAttribute("url", "../member/login.do");
		} else {
			int buyer_num = tradeService.selectBuyerNum(tradeVO.getItem_num(), tradeVO.getItem_sizenum(), purchase_price);
			SaleBidVO sbVO = tradeService.selectSaleBidForDirectSell(user.getMem_num(), tradeVO.getItem_num(), tradeVO.getItem_sizenum());
			if(sbVO.getMem_num() == user.getMem_num()) {
				model.addAttribute("message","이미 등록된 입찰 정보가 있어, 즉시 판매를 할 수 없습니다.");
				model.addAttribute("url","../item/itemList.do");
				return "common/resultView";
			}
			if(buyer_num == user.getMem_num()) {
				model.addAttribute("message","본인이 등록한 입찰 정보로 즉시 판매할 수 없습니다.");
				model.addAttribute("url","../item/itemList.do");
				return "common/resultView";
			}else {
				// 거래 정보 저장을 위한 거래 번호 생성
				int trade_num = tradeService.selectTradeNum();
				int mem_num = buyer_num;
				int purchase_num = tradeService.selectPurchaseBidNumber(mem_num, tradeVO.getItem_num(), purchase_price);
				tradeVO.setTrade_num(trade_num);
				tradeVO.setBuyer_num(buyer_num);
				tradeVO.setSeller_num(user.getMem_num());
				tradeVO.setTrade_price(purchase_price);
				
				tradeService.insertTrade(tradeVO);
				
				// 구매 입찰 정보 삭제
				tradeService.deletePurchaseBid(purchase_num);
				model.addAttribute("message", "[즉시판매] 완료했습니다.");
				model.addAttribute("url", "../item/itemList.do");
			}
		}
		return "common/resultView";
	}
	
	/**
	 * ======================================================================================================================
	 * 마이페이지 : 구매, 판매 내역
	 * ======================================================================================================================
	 **/
	// 마이페이지 - 구매 내역
	@RequestMapping("/user/buying.do")
	public ModelAndView myPurchaseBidInfO(
									  @RequestParam(value="way",defaultValue="1") int way,
									  @RequestParam(value="status",defaultValue="1") int status,
									  @RequestParam(value="pageNum",defaultValue="1") int currentPage,
									  HttpSession session) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		int bidCount = tradeService.selectPurchaseBidCount(user.getMem_num());
		int tradeCount = tradeService.selectTradePurchaseCount(user.getMem_num());
		int quitCount = tradeService.selectTradePurchaseQuitCount(user.getMem_num());
		PagingUtil page = null;
		List<PurchaseBidVO> purchaseBidList = null;
		List<TradeVO> purchaseTradeList = null;
		
		if(way == 1) {
			count = tradeService.selectPurchaseBidCount(user.getMem_num());
			bidCount = tradeService.selectPurchaseBidCount(user.getMem_num());
			page = new PagingUtil(currentPage,count,10,5,"/user/buying.do");
			
			map.put("status", status);
			map.put("mem_num", user.getMem_num());
			
			if(count > 0 ) {
				map.put("start", page.getStartRow());
				map.put("end",page.getEndRow());
				purchaseBidList = tradeService.selectPurchaseBidInfo(map);
			}
			
			mav.addObject("list",purchaseBidList);
		}else if(way == 2) {
			count = tradeService.selectTradePurchaseCount(user.getMem_num());
			
			page = new PagingUtil(currentPage,count,10,5,"/user/buying.do");
			
			map.put("status", status);
			map.put("mem_num", user.getMem_num());
			
			if(count > 0 ) {
				map.put("start", page.getStartRow());
				map.put("end",page.getEndRow());
				purchaseTradeList = tradeService.selectTradePurchaseInfo(map);
			}
			log.debug("<< count >> : " + count);
			mav.addObject("list",purchaseTradeList);
		}else if(way == 3) {
			status = 5;
			count = tradeService.selectTradePurchaseQuitCount(user.getMem_num());
			
			page = new PagingUtil(currentPage,count,10,5,"/user/buying.do");
			
			map.put("status", status);
			map.put("mem_num", user.getMem_num());
			
			if(count > 0) {
				map.put("start", page.getStartRow());
				map.put("end",page.getEndRow());
				purchaseTradeList = tradeService.selectTradePurchaseInfo(map);
			}
			mav.addObject("list",purchaseTradeList);
		}
		
		
		mav.addObject("page",page.getPage());
		mav.addObject("count",count);
		mav.addObject("bidCount",bidCount);
		mav.addObject("tradeCount",tradeCount);
		mav.addObject("quitCount",quitCount);
		mav.addObject("status",status);
		mav.addObject("way",way);
		mav.setViewName("buying");
		return mav;
	}
	
	// 마이페이지 - 판매내역
	@RequestMapping("/user/selling.do")
	public ModelAndView mySaleInfo(
									@RequestParam(value="way",defaultValue="1") int way,
									@RequestParam(value="status",defaultValue="1") int status,
									@RequestParam(value="pageNum",defaultValue="1") int currentPage,
									HttpSession session) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		int count = 0;
		int bidCount = tradeService.selectSaleBidCount(user.getMem_num());
		int tradeCount = tradeService.selectTradeSaleCount(user.getMem_num());
		int quitCount = tradeService.selectTradeSaleQuitCount(user.getMem_num());
		
		PagingUtil page = null;
		List<SaleBidVO> saleBidList = null;
		List<TradeVO> saleTradeList = null;
		
		if(way == 1) { // 판매 입찰 내역
			count = tradeService.selectSaleBidCount(user.getMem_num());
			bidCount = tradeService.selectSaleBidCount(user.getMem_num());
			page = new PagingUtil(currentPage,count,10,5,"/user/buying.do");
			
			map.put("status", status);
			map.put("mem_num", user.getMem_num());
			
			if(count > 0 ) {
				map.put("start", page.getStartRow());
				map.put("end",page.getEndRow());
				saleBidList = tradeService.selectSaleBidInfo(map);
			}
			
			mav.addObject("list",saleBidList);
		}else if(way == 2) { // 판매 거래 정보 내역
			count = tradeService.selectTradeSaleCount(user.getMem_num());
			
			page = new PagingUtil(currentPage,count,10,5,"/user/buying.do");
			
			map.put("status", status);
			map.put("mem_num", user.getMem_num());
			
			if(count > 0 ) {
				map.put("start", page.getStartRow());
				map.put("end",page.getEndRow());
				saleTradeList = tradeService.selectTradeSaleInfo(map);
			}
			log.debug("<< count >> : " + count);
			mav.addObject("list",saleTradeList);
		}else if(way == 3) { // 종료된 판매 거래 정보 내역
			status = 5;
			count = tradeService.selectTradeSaleQuitCount(user.getMem_num());
			
			page = new PagingUtil(currentPage,count,10,5,"/user/buying.do");
			
			map.put("status", status);
			map.put("mem_num", user.getMem_num());
			
			if(count > 0) {
				map.put("start", page.getStartRow());
				map.put("end",page.getEndRow());
				saleTradeList = tradeService.selectTradeSaleInfo(map);
				log.debug("<< 종료된 판매 거래 >> : " + saleTradeList.size());
			}
			mav.addObject("list",saleTradeList);
		}
		
		
		mav.addObject("page",page.getPage());
		mav.addObject("count",count);
		mav.addObject("bidCount",bidCount);
		mav.addObject("tradeCount",tradeCount);
		mav.addObject("quitCount",quitCount);
		mav.addObject("status",status);
		mav.addObject("way",way);
		mav.setViewName("selling");
		return mav;
	}
	
	// 마이페이지 - 구매입찰 내역 삭제
	@RequestMapping("/purchase/deleteBid.do")
	public String deletePurchaseBid(@RequestParam int purchase_num,HttpSession session,Model model) {
		MemberVO user = (MemberVO) session.getAttribute("user");
		
		if(user == null) {
			model.addAttribute("message","로그인이 필요합니다!!!");
			model.addAttribute("url","../member/login.do");
		}else {
			// 구매 입찰 내역 삭제
			tradeService.deletePurchaseBid(purchase_num);
			model.addAttribute("message","구매입찰 내역이 성공적으로 삭제되었습니다.");
			model.addAttribute("url","../user/buying.do");
		}
		
		return "common/resultView";
	}
	
	// 마이페이지 - 판매입찰 내역 삭제
	@RequestMapping("/sale/deleteBid.do")
	public String deleteSaleBid(@RequestParam int sale_num,HttpSession session, Model model) {
		MemberVO user = (MemberVO) session.getAttribute("user");
		
		if(user == null) {
			model.addAttribute("message","로그인이 필요합니다.");
			model.addAttribute("url","../member/login.do");
		}else {
			tradeService.deleteSaleBid(sale_num);
			model.addAttribute("message","판매입찰 내역이 성공적으로 삭제되었습니다.");
			model.addAttribute("url","../user/selling.do");
		}
		
		return "common/resultView";
	}
	
	// 마이페이지 - 판매 거래 상태 변경 ( 검수준비중 -> 검수중 )
	@RequestMapping("/sale/sendItem.do")
	public String updateTradeStateToSend(@RequestParam int trade_num, HttpSession session, Model model) {
		MemberVO user = (MemberVO) session.getAttribute("user");
		
		if(user == null) {
			model.addAttribute("message","로그인이 필요합니다.");
			model.addAttribute("url","../member/login.do");
		}else {
			tradeService.updateTradeStateToSend(trade_num);
			model.addAttribute("message","상품을 보냅니다.");
			model.addAttribute("url","../user/selling.do");
		}
		
		return "common/resultView";
	}
	
	/**
	 * ======================================================================================================================
	 * 관리자 관련 정보
	 * ======================================================================================================================
	 **/
	// 거래 목록 조회
	@RequestMapping("/trade/admin_list.do")
	public ModelAndView getTradeList(@RequestParam(value="pageNum", defaultValue="1")int currentPage,
									 @RequestParam(value="status", defaultValue="1")int status,
									 HttpSession session) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		String message = "";
		String url = "";
		PagingUtil page = null;
		int count = 0;
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		List<TradeVO> list = null;
		if(user == null) {
			message = "로그인이 필요합니다.";
			url = "../member/login.do";
			mav.addObject("message",message);
			mav.addObject("url",url);
			mav.setViewName("common/resultView");
			return mav;
		}else if(user != null && user.getMem_auth() != 9) {
			message = "잘못된 접근입니다.";
			url = "../main/main.do";
			mav.addObject("message",message);
			mav.addObject("url",url);
			mav.setViewName("common/resultView");
			return mav;
		}else {
			count = tradeService.getTradeListCount();
			page = new PagingUtil(currentPage,count,10,5,"/trade/admin_list.do");
			
			if(count > 0) {
				map.put("start",page.getStartRow());
				map.put("end",page.getEndRow());
				map.put("status", status);
				list = tradeService.getTradeList(map);
			}
			
			mav.addObject("list",list);
			mav.addObject("page",page.getPage());
			mav.addObject("count",count);
			mav.setViewName("admin_list");
			mav.addObject("status",status);
			return mav;
		}
		
	}
	
	// 거래 상세 페이지
	@RequestMapping("/trade/admin_detail.do")
	public String getTradeDetail(@RequestParam int trade_num, HttpSession session, Model model) {
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		TradeVO trade = null;
		if(user == null) {
			model.addAttribute("message","로그인이 필요합니다.");
			model.addAttribute("url","../member/login.do");
			return "common/resultView";
		}else {
			if(user.getMem_auth() != 9) {
				model.addAttribute("message","잘못된 접근입니다.");
				model.addAttribute("url","../main/main.do");
				return "common/resultView";
			}else {
				trade = tradeService.getTradeDetail(trade_num);
				model.addAttribute("trade",trade);
			}
		}
		
		return "admin_detail";
	}
	
	// 거래 상태 수정
	@RequestMapping("/trade/adminUpdateTradeState.do")
	public String updateTradeState(HttpSession session, Model model, @RequestParam int trade_num, @RequestParam int trade_state){
		MemberVO user = (MemberVO) session.getAttribute("user");
		
		if(user == null) {
			model.addAttribute("message","로그인이 필요하니다.");
			model.addAttribute("url","../member/login.do");
			return "common/resultView";
		}else {
			if(user.getMem_auth() != 9 || user.getMem_num()!=29) {
				model.addAttribute("message","잘못된 접근입니다.");
				model.addAttribute("url","../main/main.do");
				return "common/resultView";
			}else {
				if(trade_state == 4) {
					// 검수 성공 - 판매자에게 (입찰가 - 수수료) 입금하기
					TradeVO tradeVO = tradeService.getTradeDetailForDeposit(trade_num);
					int mem_num = tradeVO.getSeller_num();
					int price = tradeVO.getTrade_price();
					int fee = price / 10;
					int total = price - fee;
					
					// 거래가 판매자에게 입금
					tradeService.sendPointToSeller(mem_num, total); 
					// 거래가 관리자에서 출금
					tradeService.adminWithdraw(total, user.getMem_num());
				}
				tradeService.updateTradeState(trade_num, trade_state);
				model.addAttribute("message","거래 상태를 성공정으로 수정했습니다.");
				model.addAttribute("url","../trade/admin_list.do");
				return "common/resultView";
			}
		}
	}
	
	// 거래 실패로 인한 패널티 부여 페이지
	@RequestMapping("/trade/adminGivePenalty.do")
	public ModelAndView getAdminGivePenaltyForm(@RequestParam int seller_num, @RequestParam int trade_num, HttpSession session) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		ModelAndView mav = new ModelAndView();
		
		if(user == null) {
			mav.addObject("message","로그인이 필요합니다.");
			mav.addObject("url","../member/login.do");
			mav.setViewName("common/resultView");
			return mav;
		}else {
			if(user.getMem_auth() != 9) {
				mav.addObject("message","잘못된 접근입니다.");
				mav.addObject("url","../main/main.do");
				mav.setViewName("common/resultView");
				return mav;
			}else {
				mav.addObject("trade",tradeService.getTradeDetail(trade_num));
				mav.setViewName("admin_penalty");
				return mav;
			}
		}
	}
	
	// 관리자 - 포인트 정보
	@RequestMapping("/admin/point.do")
	public String adminGetPoint(HttpSession session, Model model) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		if(user == null) {
			model.addAttribute("message","로그인이 필요합니다.");
			model.addAttribute("url","../member/login.do");
			return "common/resultView";
		}else {
			if(user.getMem_auth() != 9 || user.getMem_num() != 29) {
				model.addAttribute("message","잘못된 접근입니다.");
				model.addAttribute("url","../main/main.do");
				return "common/resultView";
			}else {
				model.addAttribute("point",tradeService.adminGetPoint(user.getMem_num()));
				return "admin_point";
			}
		}
	}
	
}
