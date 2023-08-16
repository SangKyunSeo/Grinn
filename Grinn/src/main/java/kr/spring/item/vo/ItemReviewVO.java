package kr.spring.item.vo;

import java.io.IOException;
import java.sql.Date;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import kr.spring.util.DurationFromNow;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemReviewVO {
	private int review_num;
	@NotEmpty
	private String review_content;
	private byte[] review_photo;
	private String review_photoname;
	private String review_date;
	private String review_modifydate;
	@Range(min=1,max=5)
	private int review_star;
	private int review_status;
	private int item_num;
	private int mem_num;
	
	private String mem_id;
	private String mem_nickname;
	
	public void setReview_date(Date review_date) {
		this.review_date = DurationFromNow.getTimeDiffLabel(review_date);
	}
	public void setReview_modifydate(Date review_modifydate) {
		this.review_modifydate = DurationFromNow.getTimeDiffLabel(review_modifydate);
	}
	//이미지를 byte[] 배열로 변환(blob처리)
	//(주의) 폼에서 파일 업로드 파라미터 네임은 반드시 upload로 지정해야함
	public void setUpload(MultipartFile upload)throws IOException{
		//MultipartFile -> byte[]
		setReview_photo(upload.getBytes());
		//파일이름
		setReview_photoname(upload.getOriginalFilename());
	}
	
}

