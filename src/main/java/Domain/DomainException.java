package Domain;

import AbstractException.AbstractException;

/**
 * @author Gerard
 */
public class DomainException extends AbstractException {

    /**
     * Constructora.
     */
    public DomainException() {
        super();
    }

    /**
     * Constructora amb missatge. El missatge friendly pren el mateix valor.
     * @param message missatge de l'excepcio
     */
    public DomainException(String message) {
        super(message);
    }

    /**
     * Constructora un missatge tecnic i un altre friendly.
     * @param message missatge tecnic de l'excepcio
     * @param friendlyMessage missatge friendly
     */
    public DomainException(String message, String friendlyMessage) {
        super(message, friendlyMessage);
    }
}
