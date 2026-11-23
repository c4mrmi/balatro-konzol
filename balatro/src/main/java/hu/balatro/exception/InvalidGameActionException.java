package hu.balatro.exception;

public class InvalidGameActionException extends Exception {
    public InvalidGameActionException(String message) {
        super(message);
    }
}