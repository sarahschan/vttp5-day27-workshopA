package sg.edu.nus.iss.paf_day27_workshopA.service;

import static sg.edu.nus.iss.paf_day27_workshopA.util.GameConstants.*;

import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.paf_day27_workshopA.exception.NoGameFoundException;
import sg.edu.nus.iss.paf_day27_workshopA.repository.CommentRepository;
import sg.edu.nus.iss.paf_day27_workshopA.repository.GameRepository;

@Service
public class CommentService {
    
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CommentRepository commentRepository;

    public Optional<String> getGameName(int gameId) {
        
        Document d = gameRepository.getGameById(gameId);

        if (d == null) {
            return Optional.empty();
        }

        return Optional.of(d.getString(GAME_FIELD_NAME));
    }


    public boolean submitComment(String user, int rating, String comment, int gameId) {

        if (getGameName(gameId).isEmpty()) {
            throw new NoGameFoundException(String.format("Game with gID %d not found", gameId));
        }

        String gameName = getGameName(gameId).get();

        commentRepository.insertReview(user, rating, comment, gameId, gameName);

        return true;
    }


}
