package sg.edu.nus.iss.paf_day27_workshopA.service;

import static sg.edu.nus.iss.paf_day27_workshopA.util.GameConstants.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.paf_day27_workshopA.exception.NoCommentFoundException;
import sg.edu.nus.iss.paf_day27_workshopA.exception.NoGameFoundException;
import sg.edu.nus.iss.paf_day27_workshopA.repository.GameRepository;
import sg.edu.nus.iss.paf_day27_workshopA.repository.ReviewRepository;

@Service
public class ReviewService {
    
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public Optional<String> getGameName(int gameId) {
        
        Document d = gameRepository.getGameById(gameId);

        if (d == null) {
            return Optional.empty();
        }

        return Optional.of(d.getString(GAME_FIELD_NAME));
    }


    public void submitReview(String user, int rating, String comment, int gameId) {

        if (getGameName(gameId).isEmpty()) {
            throw new NoGameFoundException(String.format("Game with gID %d not found", gameId));
        }

        String gameName = getGameName(gameId).get();

        reviewRepository.insertReview(user, rating, comment, gameId, gameName);

    }


    public boolean checkReviewExists(String reviewId) {

        Document d = reviewRepository.getReviewById(reviewId);

        if (d == null) {
            throw new NoCommentFoundException(String.format("Review with _id %s not found, cannot be updated", reviewId));
        }

        return true;
    }


    public void updateReview(String reviewId, int rating, String comment) {

        reviewRepository.updateReview(reviewId, rating, comment);

    }


    public String getReviewHistoryById(String reviewId) {
        
        Document d = reviewRepository.getReviewById(reviewId);
            d.append("timestamp", LocalDateTime.now().toString());

        return d.toJson();

    }


    public String getLatestReview(String reviewId){

        Document d = reviewRepository.getReviewById(reviewId);

        String user = d.getString("user");
        int rating = d.getInteger("rating");
        String comment = d.getString("comment");
        int id = d.getInteger("ID");
        String posted = d.getString("posted");
        String name = d.getString("name");
        Boolean editedBoolean = false;

        if (d.containsKey("edited") && !d.getList("edited", Document.class).isEmpty()){
            List<Document> edits = d.getList("edited", Document.class);
            Document lastEdit = edits.get(edits.size() -1);

            rating = lastEdit.getInteger("rating");
            comment = lastEdit.getString("comment");
            posted = lastEdit.getString("posted");
            editedBoolean = true;

        }

        JsonObject response = Json.createObjectBuilder()
            .add("user", user)
            .add("rating", rating)
            .add("comment", comment)
            .add("ID", id)
            .add("posted", posted)
            .add("name", name)
            .add("edited", editedBoolean)
            .add("timestamp", LocalDateTime.now().toString())
            .build();


        return response.toString();

    }

    // {
    //     user: <name form field>,
    //     ** rating: <latest rating>,
    //     ** comment: <latest comment>,
    //     ID: <game id form field>,
    //     ** posted: <date>,
    //     name: <The board game’s name as per ID>,
    //     ** edited: <true or false depending on edits>,
    //     timestamp: <result timestamp>
    // }
}
