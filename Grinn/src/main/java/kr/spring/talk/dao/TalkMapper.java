package kr.spring.talk.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.talk.vo.TalkMemberVO;
import kr.spring.talk.vo.TalkRoomVO;
import kr.spring.talk.vo.TalkVO;

@Mapper
public interface TalkMapper {
	//채팅방 목록
	public List<TalkRoomVO> selectTalkRoomList(Map<String,Object> map);
	public int selectRowCount(Map<String,Object> map);
	
	//채팅방 번호 생성
	@Select("SELECT talkroom_seq.nextval FROM dual")
	public int selectTalkRoomNum();
	//채팅방 생성
	@Insert("INSERT INTO talkroom (talkroom_num,basic_name) VALUES(#{talkroom_num},#{basic_name})")
	public void insertTalkRoom(TalkRoomVO talkRoomVO);
	//채팅방 멤버 등록
	@Insert("INSERT INTO talk_member (talkroom_num,room_name,mem_num) VALUES (#{talkroom_num},#{room_name},#{mem_num})")
	public void insertTalkRoomMember(@Param(value="talkroom_num") Integer talkroom_num,@Param(value="room_name") String room_name,@Param(value="mem_num") Integer mem_num);
	
	//채팅 멤버 읽기
	public List<TalkMemberVO> selectTalkMember(Integer talkroom_num);
	
	//채팅 메시지 번호 생성
	@Select("SELECT talk_seq.nextval FROM dual")
	public int selectTalkNum();
	//채팅 메시지 등록
	public void insertTalk(TalkVO talkVO);
	//읽지 않은 채팅 기록 저장
	@Insert("INSERT INTO talk_read (talkroom_num,talk_num,mem_num) VALUES (#{talkroom_num},#{talk_num},#{mem_num})")
	public void insertTalkRead(@Param(value="talkroom_num") int talkroom_num,
            @Param(value="talk_num") int talk_num,
            @Param(value="mem_num") int mem_num);
	
	//채팅메시지 읽기
	public List<TalkVO> selectTalkDetail(Map<String,Integer> map);
	//읽은 채팅 기록 삭제
	@Delete("DELETE FROM talk_read WHERE talkroom_num=#{talkroom_num} AND mem_num=#{mem_num}")
	public void deleteTalkRead(Map<String,Integer> map);
	
	//채팅방 이름 변경하기
	@Update("UPDATE talk_member SET room_name=#{room_name} WHERE talkroom_num=#{talkroom_num} AND mem_num=#{mem_num}")
	public void changeRoomName(TalkMemberVO vo);
	
	//채팅방 상세
	@Select("SELECT * FROM talkroom WHERE talkroom_num=#{talkroom_num}")
	public TalkRoomVO selectTalkRoom(Integer talkroom_num);
		
	//채팅방 나가기
	@Delete("DELETE FROM talk_member WHERE talkroom_num=#{talkroom_num} AND mem_num=#{mem_num}")
	public void deleteTalkRoomMember(TalkVO talkVO);
	@Delete("DELETE FROM talk WHERE talkroom_num=#{talkroom_num}")
	public void deleteTalk(Integer talkroom_num);
	@Delete("DELETE FROM talkroom WHERE talkroom_num=#{talkroom_num}")
	public void deleteTalkRoom(Integer talkroom_num);
	
}







