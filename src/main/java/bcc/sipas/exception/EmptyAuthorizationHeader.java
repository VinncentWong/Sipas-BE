package bcc.sipas.exception;

public class EmptyAuthorizationHeader extends RuntimeException{
    public EmptyAuthorizationHeader(String message) {
        super(message);
    }
}
