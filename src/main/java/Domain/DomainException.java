package Domain;

import AbstractException.AbstractException;

/**
 * @author Gerard
 */
public class DomainException extends AbstractException {
    public DomainException() {
        super();
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, String friendlyMessage) {
        super(message, friendlyMessage);
    }
}
