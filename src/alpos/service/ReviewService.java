package alpos.service;

import java.util.List;

import alpos.entity.Hashtag;
import alpos.model.ReviewModel;

public interface ReviewService {
	public void addReview (ReviewModel reviewModel);
	
	public List<Hashtag> getHashtagFromReviewContent (String reviewContent);
}
