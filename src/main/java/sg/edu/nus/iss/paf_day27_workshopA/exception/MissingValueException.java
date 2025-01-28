package sg.edu.nus.iss.paf_day27_workshopA.exception;

public class MissingValueException extends RuntimeException{

    public MissingValueException(String message) {
        super(message);
    }
}
