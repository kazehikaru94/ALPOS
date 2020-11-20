package alpos.service.imp;

import alpos.dao.HashtagDAO;
import alpos.dao.ReviewDAO;
import alpos.entity.Hashtag;
import alpos.entity.Review;
import alpos.model.ReviewModel;
import alpos.service.ReviewService;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.*;

@Component
public class ReviewServiceImp implements ReviewService {
	
	@Autowired
	private ReviewDAO reviewDao;
	
	@Autowired
	private HashtagDAO hashtagDao;
	
	public ReviewDAO getReviewDao() {
		return reviewDao;
	}

	public void setReviewDao(ReviewDAO reviewDao) {
		this.reviewDao = reviewDao;
	}
	
	public HashtagDAO getHashtagDAO() {
		return hashtagDao;
	}


	public void setHashtagDao(HashtagDAO hashtagDAO) {
		this.hashtagDao = hashtagDAO;
	}


	@Transactional
	public void addReview (ReviewModel reviewModel) {
		try {
			Review review = new Review();
			review.setUserId(reviewModel.getUserId());
			review.setBookId(reviewModel.getBookId());
			review.setContent(reviewModel.getContent());
			review.setHashtags(reviewModel.getHashtags());
			reviewDao.makePersistent(review);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	
	@Transactional
	public List<Hashtag> getHashtagFromReviewContent (String reviewContent){
		List<String> hashtags = new ArrayList<String>();
		List<Hashtag> hashtagObjects = new ArrayList<Hashtag>();
		
		//Extract hashtag from review content
		Pattern hashtagPattern = Pattern.compile("(#\\w+)\\b"); 
		Matcher matcher = hashtagPattern.matcher(reviewContent);
		while(matcher.find()) {
			hashtags.add(matcher.group().substring(1).toLowerCase());			
		}	
		
		//Convert hashtag string to hashtag Object
		
		try {
			while (hashtags != null) {
				for (String hashtag:hashtags) {
//					Hashtag hashtagObject = hashtagDAO.findByName(hashtag);
					if (hashtagDao.findByName(hashtag) != null) {
						hashtagObjects.add(hashtagDao.findByName(hashtag));
					}
					else {
						Hashtag hashtagObject = new Hashtag();
						hashtagObject.setName(hashtag);
						hashtagObject = hashtagDao.makePersistent(hashtagObject);
						hashtagObjects.add(hashtagObject);
					}
				}
				return hashtagObjects;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return hashtagObjects;
	}
}
