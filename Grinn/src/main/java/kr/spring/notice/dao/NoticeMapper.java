package kr.spring.notice.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.notice.vo.NoticeVO;

@Mapper 
public interface NoticeMapper {
	//고객센터-공지사항
	@Select("SELECT * FROM notice WHERE no_status=1")
	public List<NoticeVO> selectList(Map<String, Object> map);
	//고객센터-자주묻는질문
	@Select("SELECT * FROM notice WHERE no_status=2")
	public List<NoticeVO> selectFaqList(Map<String, Object> map);
	//고객센터-검수기준
	@Select("SELECT * FROM notice WHERE no_status=3")
	public List<NoticeVO> selectAuthList(Map<String, Object> map);
	@Select("SELECT * FROM notice WHERE no_status=3 AND no_category=#{no_category}")
	public List<NoticeVO> selectAuthListCategory(Map<String, Object> map);
	public int selectRowCount(Map<String, Object> map);
	public void insertNotice(NoticeVO notice);
	@Select("SELECT * FROM notice")
	public NoticeVO selectNotice(Integer no_num);
	@Update("UPDATE notice SET no_title=#{no_title}, no_content=#{no_content}, "
			+ "no_modifydate=SYSDATE WHERE no_num=#{no_num}")
	public void updateNotice(NoticeVO notice);
	@Delete("DELETE FROM notice WHERE no_num=#{no_num}")
	public void deleteNotice(Integer no_num);

}
