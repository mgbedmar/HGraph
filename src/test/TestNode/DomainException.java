package TestNode;


public class DomainException extends Exception {
    private String friendlyMessage;

    public DomainException() {
        super();
    }

    public DomainException(String message) {
        super(message);
        this.friendlyMessage = message;
    }

    public DomainException(String message, String friendlyMessage) {
        super(message);
        this.friendlyMessage = friendlyMessage;
    }

    public String getFriendlyMessage() {
        return this.friendlyMessage;
    }
}
