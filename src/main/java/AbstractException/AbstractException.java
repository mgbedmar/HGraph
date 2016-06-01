package AbstractException;

/**
 * @author Gerard
 */
public class AbstractException extends Exception {

    /**
     * Missatge friendly per l'usuari.
     */
    private String friendlyMessage;

    /**
     * Constructora.
     */
    public AbstractException() {
        super();
    }

    /**
     * Constructora amb missatge. El missatge friendly pren el mateix valor.
     * @param message missatge de l'excepcio
     */
    public AbstractException(String message) {
        super(message);
        this.friendlyMessage = message;
    }

    /**
     * Constructora un missatge tecnic i un altre friendly.
     * @param message missatge tecnic de l'excepcio
     * @param friendlyMessage missatge friendly
     */
    public AbstractException(String message, String friendlyMessage) {
        super(message);
        this.friendlyMessage = friendlyMessage;
    }

    /**
     * Dona el missatge friendly de l'excepcio
     * @return missatge friendly
     */
    public String getFriendlyMessage() {
        return this.friendlyMessage;
    }
}
