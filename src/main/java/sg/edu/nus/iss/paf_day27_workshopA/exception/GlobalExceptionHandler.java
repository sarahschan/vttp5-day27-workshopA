package sg.edu.nus.iss.paf_day27_workshopA.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler({MissingValueException.class, InvalidParamException.class, NoGameFoundException.class, NoCommentFoundException.class})
    public ResponseEntity<Map<String, String>> handleMissingValueException(RuntimeException ex) {
        Map<String, String> errorMsg = new HashMap<>();
        errorMsg.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
    }
}
