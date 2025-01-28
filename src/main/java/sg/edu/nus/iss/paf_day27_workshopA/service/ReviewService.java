package sg.edu.nus.iss.paf_day27_workshopA.service;

import static sg.edu.nus.iss.paf_day27_workshopA.util.GameConstants.*;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
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

}
