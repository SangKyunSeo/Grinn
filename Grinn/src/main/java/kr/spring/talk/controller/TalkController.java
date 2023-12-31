package kr.spring.talk.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.spring.member.controller.MemberController;
import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import kr.spring.report.vo.StyleReportVO;
import kr.spring.style.vo.StyleCommentVO;
import kr.spring.style.vo.StyleVO;
import kr.spring.talk.service.TalkService;
import kr.spring.talk.vo.TalkMemberVO;
import kr.spring.talk.vo.TalkRoomVO;
import kr.spring.talk.vo.TalkVO;
import kr.spring.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class TalkController {
	@Autowired
	private TalkService talkService;
	@Autowired
	private MemberService memberService;

	/* ====================== 채팅방 생성 ====================== */
	//폼 호출
	@GetMapping("/talk/talkRoomWrite.do")
	public String talkRoomWrite() {
		return "talkRoomWrite";
	}
	//전송된 데이터 처리
	@PostMapping("/talk/talkRoomWrite.do")
	public String talkRoomSubmit(TalkRoomVO vo, HttpSession session) {
		log.debug("<<채팅방 만들기>> : " + vo);
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		//채팅 멤버 초대 문구 설정 시작
		vo.setTalkVO(new TalkVO());
		vo.getTalkVO().setMem_num(user.getMem_num());
		vo.getTalkVO().setMessage(user.getMem_id() + "님이 "
							+ findMemberId(vo, user) + "님을 초대했습니다.@{member}@");
		//채팅 멤버 초대 문구 설정 끝
		
		talkService.insertTalkRoom(vo);
		
		return "redirect:/talk/talkList.do";
	}
	//초대한 회원의 id 구하기
	private String findMemberId(TalkRoomVO vo, MemberVO user) {
		String member_id = "";
		int[] members = vo.getMembers();
		for(int i=0; i<members.length; i++) {
			String temp_id = memberService.selectMember(members[i]).getMem_id();
			//초대한 회원의 아이디는 제외
			if(!user.getMem_id().equals(temp_id)) {
				member_id += temp_id;
				if(i < members.length-1) member_id += ", ";
			}
		}
		return member_id;
	}
	
	/* ====================== 채팅 회원 검색 ====================== */
	@RequestMapping("/talk/memberSearchAjax.do")
	@ResponseBody
	public Map<String,Object> memberSearchAjax(@RequestParam String mem_id, HttpSession session){
		Map<String,Object> mapJson = new HashMap<String,Object>();
		
		MemberVO memberVO = (MemberVO)session.getAttribute("user");
		if(memberVO == null) {//로그인이 되지 않은 경우
			mapJson.put("result", "logout");
		}else {//로그인 된 경우
			List<MemberVO> member = memberService.selectSearchMember(mem_id);
			mapJson.put("result", "success");
			mapJson.put("member", member);
		}
		
		return mapJson;
	}
	
	/* ====================== 채팅방 목록 ====================== */
	@RequestMapping("/talk/talkList.do")
	public String chatList(@RequestParam(value="talkroom_num",defaultValue="0")int talkroom_num,@RequestParam(value="pageNum",defaultValue="1") int currentPage,
										String keyword, HttpSession session, Model model) {
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("keyword", keyword);
		map.put("mem_num", user.getMem_num());
		
		int count = talkService.selectRowCount(map);
		
		//페이지 처리
		PagingUtil page = new PagingUtil(null,keyword,currentPage,count,30,10,"talkList.do");
		
		List<TalkRoomVO> list = null;
		if(count > 0) {
			map.put("start", page.getStartRow());
			map.put("end", page.getEndRow());
			list = talkService.selectTalkRoomList(map);
		}
		model.addAttribute("count", count);
		model.addAttribute("list", list);
		model.addAttribute("page", page.getPage());
		model.addAttribute("talkroom_num",talkroom_num);
		/* 여기부터 아래에 있는 톡디테일을 끌어다 쓴 내용 */
		String chatMember = ""; //채팅 멤버
		String room_name = ""; //채팅방 이름
		
		List<TalkMemberVO> list2 = talkService.selectTalkMember(talkroom_num);
		for(int i=0; i<list2.size(); i++) {
			TalkMemberVO vo = list2.get(i);
			if(user.getMem_num()==vo.getMem_num()) {
				//로그인한 회원의 채팅방 이름 셋팅
				room_name = vo.getRoom_name();
			}
			//채팅 멤버 저장
			if(i > 0) chatMember += ",";
			chatMember += list2.get(i).getMem_id();
		}
		
		//채팅 멤버 id
		model.addAttribute("chatMember", chatMember);
		//채팅 멤버수
		model.addAttribute("chatCount", list2.size());
		//로그인한 회원의 채팅방 이름
		model.addAttribute("room_name", room_name);		
		
		return "talkList";
	}
	
	/* ====================== 채팅 메시지 처리 ====================== */
	//채팅 메시지 페이지 호출
	@RequestMapping("/talk/talkDetail.do")
	public String chatDetail(@RequestParam int talkroom_num, Model model, HttpSession session) {
		String chatMember = ""; //채팅 멤버
		String room_name = ""; //채팅방 이름
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		List<TalkMemberVO> list = talkService.selectTalkMember(talkroom_num);
		for(int i=0; i<list.size(); i++) {
			TalkMemberVO vo = list.get(i);
			if(user.getMem_num()==vo.getMem_num()) {
				//로그인한 회원의 채팅방 이름 셋팅
				room_name = vo.getRoom_name();
			}
			//채팅 멤버 저장
			if(i > 0) chatMember += ",";
			chatMember += list.get(i).getMem_id();
		}
		
		//채팅 멤버 id
		model.addAttribute("chatMember", chatMember);
		//채팅 멤버수
		model.addAttribute("chatCount", list.size());
		//로그인한 회원의 채팅방 이름
		model.addAttribute("room_name", room_name);			
		
		return "talkList";
	}
	//채팅 메시지 전송
	@RequestMapping("/talk/writeTalk.do")
	@ResponseBody
	public Map<String,Object> writeTalkAjax(TalkVO vo, HttpSession session){
		Map<String,Object> mapJson = new HashMap<String,Object>();
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		if(user==null) {//로그인이 되지 않은 경우
			mapJson.put("result", "logout");
		}else {//로그인 된 경우
			vo.setMem_num(user.getMem_num());//발신자
			
			log.debug("<<채팅 메시지 전송>> : " + vo);
			
			talkService.insertTalk(vo);
			
			mapJson.put("result", "success");
		}
		
		return mapJson;
	}
	//채팅 메시지 	읽기
	@RequestMapping("/talk/talkDetailAjax.do")
	@ResponseBody
	public Map<String,Object> talkDetailAjax(@RequestParam int talkroom_num, HttpSession session){
		Map<String,Object> mapJson = new HashMap<String,Object>();
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		if(user == null) {//로그인이 되지않은 경우
			mapJson.put("result", "logout");
		}else {//로그인 된 경우
			Map<String,Integer> map = new HashMap<String,Integer>();
			map.put("talkroom_num", talkroom_num);
			map.put("mem_num", user.getMem_num());
			
			List<TalkVO> list = talkService.selectTalkDetail(map);
			
			mapJson.put("result", "success");
			mapJson.put("list", list);
			mapJson.put("user_num", user.getMem_num());
		}
		
		return mapJson;
	}
	
	/* ====================== 채팅방 이름 변경 ====================== */
	@RequestMapping("/talk/changeName.do")
	@ResponseBody
	public Map<String,String> changeName(TalkMemberVO vo, HttpSession session){
		Map<String,String> mapJson = new HashMap<String,String>();
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		if(user==null) {//로그인이 되지 않은 경우
			mapJson.put("result", "logout");
		}else {//로그인 된 경우
			vo.setMem_num(user.getMem_num());
			talkService.changeRoomName(vo);
			
			mapJson.put("result", "success");
		}
		
		return mapJson;
	}
	
	/* ====================== 채팅 멤버 추가 ====================== */
	@RequestMapping("/talk/newMemberAjax.do")
	@ResponseBody
	public Map<String,Object> addNewMember(TalkRoomVO vo, HttpSession session){
		Map<String,Object> mapJson = new HashMap<String,Object>();
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		if(user==null) {//로그인이 되지 않은 경우
			mapJson.put("result", "logout");
		}else {//로그인 된 경우
			//입장 메시지 처리
			vo.setTalkVO(new TalkVO());
			vo.getTalkVO().setTalkroom_num(vo.getTalkroom_num());//채팅방 번호 셋팅
			vo.getTalkVO().setMem_num(user.getMem_num());
			vo.getTalkVO().setMessage(user.getMem_id()+"님이 " 
			    + findMemberId(vo, user) + "님을 초대했습니다.@{member}@");
			
			//채팅방 이름 셋팅
			TalkRoomVO db_vo = talkService.selectTalkRoom(vo.getTalkroom_num());
			vo.setBasic_name(db_vo.getBasic_name());
			talkService.insertNewMember(vo);
			
			mapJson.put("result", "success");
		}
		
		return mapJson;
	}
	
	/* ====================== 채팅방 나가기 ====================== */
	@RequestMapping("/talk/deleteTalkRoomMemberAjax.do")
	@ResponseBody
	public Map<String,String> memberDeleteAjax(TalkVO talkVO, HttpSession session){
		Map<String,String> mapJson = new HashMap<String,String>();
		
		MemberVO user = (MemberVO)session.getAttribute("user");
		
		if(user==null) {//로그인이 되지 않은 경우
			mapJson.put("result", "logout");
		}else {//로그인 된 경우
			//퇴장 메시지 생성
			talkVO.setMem_num(user.getMem_num());
			talkVO.setMessage(user.getMem_id()+"님이 나갔습니다.@{member}@");
			talkService.deleteTalkRoomMember(talkVO);
			
			mapJson.put("result", "success");
		}
		
		return mapJson;
	}
	
}
















