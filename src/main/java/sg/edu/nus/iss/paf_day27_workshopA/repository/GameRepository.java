package sg.edu.nus.iss.paf_day27_workshopA.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static sg.edu.nus.iss.paf_day27_workshopA.util.GameConstants.*;

@Repository
public class GameRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;


    public Document getGameById(int gameId) {
        
        Criteria criteria = Criteria.where(GAME_FIELD_ID).is(gameId);

        Query query = Query.query(criteria);
            query.fields()
                .include(GAME_FIELD_NAME);

        return mongoTemplate.findOne(query, Document.class, COLLECTION_GAMES);
    }



}
