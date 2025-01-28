package sg.edu.nus.iss.paf_day27_workshopA.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import static sg.edu.nus.iss.paf_day27_workshopA.util.CommentsConstants.*;

import java.time.LocalDateTime;

@Repository
public class CommentRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean insertReview(String user, int rating, String comment, int gameId, String gameName) {

        Document toInsert = new Document();
            toInsert.put(COMMENT_FIELD_USER, user);
            toInsert.put(COMMENT_FIELD_RATING, rating);
            toInsert.put(COMMENT_FIELD_COMMENT, comment);
            toInsert.put(COMMENT_FIELD_GAMEID, gameId);
            toInsert.put(COMMENT_FIELD_POSTED, LocalDateTime.now().toString());
            toInsert.put(COMMENT_FIELD_GAMENAME, gameName);
        
        mongoTemplate.insert(toInsert, COMMENT_COLLECTION);

        return true;
        
    }
}
