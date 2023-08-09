package kr.spring.style.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.item.vo.ItemVO;
import kr.spring.style.vo.StyleCommentVO;
import kr.spring.style.vo.StyleFavVO;
import kr.spring.style.vo.StyleVO;

@Mapper
public interface StyleMapper {
	//부모글
	//목록
	public List<StyleVO> selectList(Map<String,Object> map);
	public int selectRowCount(Map<String,Object> map);
	//작성
	public void insertStyle(StyleVO style);
	//태그할 상품 검색
	@Select("SELECT item_num, item_name, item_photo1, item_photo1name FROM item WHERE item_name LIKE '%' || #{item_name} || '%'")
	public List<ItemVO> selectSearchItem(String item_name);
	//상세
	public StyleVO selectStyle(Integer st_num);
	//수정
	public void updateStyle(StyleVO style);
	//삭제
	@Delete("DELETE FROM style WHERE st_num=#{st_num}")
	public void deleteStyle(Integer st_num);
	//좋아요
	@Select("SELECT * FROM style_fav WHERE st_num=#{st_num} AND mem_num=#{mem_num}")
	public StyleFavVO selectFav(StyleFavVO fav);
	@Select("SELECT COUNT(*) FROM style_fav WHERE st_num=#{st_num}")
	public int selectFavCount(Integer st_num);
	@Insert("INSERT INTO style_fav (stfav_num,mem_num,st_num) VALUES (style_fav_seq.nextval,#{mem_num},#{st_num})")
	public void insertFav(StyleFavVO fav);
	@Delete("DELETE FROM style_fav WHERE stfav_num=#{stfav_num}")
	public void deleteFav(Integer stfav_num);
	@Delete("DELETE FROM style_fav WHERE st_num=#{st_num}")
	public void deleteFavByStNum(Integer st_num);
	//댓글
	public List<StyleCommentVO> selectListComment(Map<String,Object> map);
	@Select("SELECT COUNT(*) FROM style_comment WHERE st_num=#{st_num}")
	public int selectRowCountComment(Map<String,Object> map);
	@Select("SELECT * FROM style_comment c JOIN member_detail d ON c.mem_num = d.mem_num WHERE com_num=#{com_num}")
	public StyleCommentVO selectComment(Integer com_num);
	public void insertComment(StyleCommentVO styleComment);
	@Update("UPDATE style_comment SET com_comment=#{com_comment}, com_mdate=SYSDATE WHERE com_num=#{com_num}")
	public void updateComment(StyleCommentVO styleComment);
	@Delete("DELETE FROM style_comment WHERE com_num=#{com_num}")
	public void deleteComment(Integer com_num);
	//부모글 삭제시 댓글이 존재하면 부모글 삭제전 댓글 삭제
	@Delete("DELETE FROM style_comment WHERE st_num=#{st_num}")
	public void deleteReplyByStyleNum(Integer st_num);
}
