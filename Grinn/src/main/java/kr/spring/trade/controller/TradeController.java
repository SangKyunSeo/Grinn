package kr.spring.trade.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kr.spring.member.vo.MemberVO;
import kr.spring.sbid.vo.SaleSizePriceVO;
import kr.spring.trade.service.TradeService;
import kr.spring.trade.vo.TradeVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class TradeController {
	@Autowired
	TradeService tradeService;
	
	/**
	 * ======================================================================================================================
	 * 												자바 빈 초기화
	 * ======================================================================================================================
	 **/
	@ModelAttribute
	public TradeVO initCommand() {
		return new TradeVO();
	}
	
	/**
	 * ======================================================================================================================
	 * 								구매하기 버튼 클릭 시 상품에 따른 사이즈 정보 제공
	 * ======================================================================================================================
	 **/
	@GetMapping("/purchase/selectSize.do")
	public ModelAndView getItemAndSize(@RequestParam int item_num) {
		
		// Login인터셉터로 로그인 되어있는지 확인필요 ***********
		
		
		// 사이즈 별 판매 입찰 정보를 뿌리기
		log.debug("<< item_num >> : " + item_num);
		List<SaleSizePriceVO> sspList = tradeService.selectSaleSizePrice(item_num);
		log.debug("<<sspList 크기 >> : " + sspList.size());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("selectSize");
		mav.addObject("list",sspList);
		
		// 아이템 번호로 아이템 정보 구하기 ***********
		
		return mav;
	}
	
	/**
	 * ======================================================================================================================
	 * 								사이즈 버튼 클릭 시 구매 동의 화면으로 이동
	 * ======================================================================================================================
	 **/
	@GetMapping("/purchase/check.do")
	public String getPurchaseAgree(@RequestParam int item_num, @RequestParam int item_sizenum,@RequestParam String item_size, Model model, HttpSession session) {
		
		// Login인터셉터로 로그인 되어있는지 확인필요 ***********
		
		// 아이템 번호로 아이템 정보 구해서 넘기기 *********
		
		model.addAttribute("item_num",item_num); // item 정보를 구하게 된다면 vo 객체를 넘겨줄거임
		model.addAttribute("item_sizenum",item_sizenum);
		model.addAttribute("item_size",item_size);
		
		return "check";
	}
	
	
}
