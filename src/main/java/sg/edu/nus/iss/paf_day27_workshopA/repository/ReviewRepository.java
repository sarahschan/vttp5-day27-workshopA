package sg.edu.nus.iss.paf_day27_workshopA.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import static sg.edu.nus.iss.paf_day27_workshopA.util.ReviewConstants.*;

import java.time.LocalDateTime;

@Repository
public class ReviewRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public void insertReview(String user, int rating, String comment, int gameId, String gameName) {

        Document toInsert = new Document();
            toInsert.put(COMMENT_FIELD_USER, user);
            toInsert.put(COMMENT_FIELD_RATING, rating);
            toInsert.put(COMMENT_FIELD_COMMENT, comment);
            toInsert.put(COMMENT_FIELD_GAMEID, gameId);
            toInsert.put(COMMENT_FIELD_POSTED, LocalDateTime.now().toString());
            toInsert.put(COMMENT_FIELD_GAMENAME, gameName);
        
        mongoTemplate.insert(toInsert, COMMENT_COLLECTION);
        
    }


    public Document getReviewById(String reviewId) {
        
        Criteria criteria = Criteria.where(COMMENT_FIELD_COMMENTID).is(reviewId);

        Query query = Query.query(criteria);

        return mongoTemplate.findOne(query, Document.class, COMMENT_COLLECTION);
    }


    public void updateReview(String reviewId, int rating, String comment) {

        Criteria criteria = Criteria.where(COMMENT_FIELD_COMMENTID).is(reviewId);

        Query query = Query.query(criteria);

        Document editDoc = new Document();
            editDoc.append(COMMENT_FIELD_COMMENT, comment);
            editDoc.append(COMMENT_FIELD_RATING, rating);
            editDoc.append(COMMENT_FIELD_POSTED, LocalDateTime.now().toString());

        Update updateOps = new Update()
            .push("edited", editDoc);

        UpdateResult result = mongoTemplate.updateFirst(query, updateOps, Document.class, COMMENT_COLLECTION);

        System.out.println(result);

    }


}
