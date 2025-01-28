package sg.edu.nus.iss.paf_day27_workshopA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.paf_day27_workshopA.exception.InvalidParamException;
import sg.edu.nus.iss.paf_day27_workshopA.exception.MissingValueException;
import sg.edu.nus.iss.paf_day27_workshopA.service.CommentService;

@RestController
@RequestMapping("")
public class CommentController {
    
    @Autowired
    private CommentService commentService;

    
    @PostMapping(path = "/review", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> submitReview(
        @RequestParam(name = "user", required = false) String user,
        @RequestParam(name = "rating", required = false) String ratingString,
        @RequestParam(name = "comment", required = false) String commentRaw,
        @RequestParam(name = "game_id", required = false) String gameIdRaw) {
    
        if (user == null || user.isBlank()) {
            throw new MissingValueException("Missing parameter and/or parameter value: user");
        }

        if (ratingString == null || ratingString.isBlank()) {
            throw new MissingValueException("Missing parameter and/or parameter value: rating");
        }

        int rating;
        try {
            rating = Integer.parseInt(ratingString);
            if (rating < 0 || rating > 10) {
                throw new InvalidParamException("Rating must be a number from 0 to 10");
            }
        } catch (NumberFormatException ex) {
            throw new InvalidParamException("Rating must be a number from 0 to 10");
        }

        String comment = "-";
        if (commentRaw != null && !commentRaw.isBlank()) {
            comment = commentRaw;
        }

        if (gameIdRaw == null || gameIdRaw.isBlank()) {
            throw new MissingValueException("Missing parameter and/or parameter value: game_id");
        }
        int gameId;
        try {
            gameId = Integer.parseInt(gameIdRaw);
        } catch (NumberFormatException ex) {
            throw new InvalidParamException("Game ID must be an number");
        }


        try {
            commentService.submitComment(user, rating, comment, gameId);
            JsonObject successMsg = Json.createObjectBuilder()
                .add("success", "Comment inserted to MongoDB")
                .build();
            return ResponseEntity.status(HttpStatus.OK).body(successMsg.toString());

        } catch (Exception ex) {
            JsonObject errorMsg = Json.createObjectBuilder()
                .add("error", ex.getMessage())
                .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg.toString());
        }

    }
}
