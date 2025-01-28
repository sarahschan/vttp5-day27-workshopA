package sg.edu.nus.iss.paf_day27_workshopA.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.paf_day27_workshopA.exception.InvalidParamException;
import sg.edu.nus.iss.paf_day27_workshopA.exception.MissingValueException;

@RestController
@RequestMapping("")
public class CommentController {
    
    @PostMapping(path = "/review", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> submitReview(
        @RequestParam(name = "user", required = false) String user,
        @RequestParam(name = "rating", required = false) String ratingString,
        @RequestParam(name = "comment", required = false) String commentRaw,
        @RequestParam(name = "game_id", required = false) String gameId) {
    
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

        if (gameId == null || gameId.isBlank()) {
            throw new MissingValueException("Missing parameter and/or parameter value: game_id");
        }



        System.out.println(String.format("Received params: %s, %d, %s, %s", user, rating, comment, gameId));

        return ResponseEntity.ok("Review submitted successfully");

    }
}
