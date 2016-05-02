package AbstractException;

public class AbstractException extends Exception {
    private String friendlyMessage;

    public AbstractException() {
        super();
    }

    public AbstractException(String message) {
        super(message);
        this.friendlyMessage = message;
    }

    public AbstractException(String message, String friendlyMessage) {
        super(message);
        this.friendlyMessage = friendlyMessage;
    }

    public String getFriendlyMessage() {
        return this.friendlyMessage;
    }
}
