package generator.java.src.main.java.com.thed.zephyr.cloud.rest.exception;

/**
 * Created by Masud on 11/19/18.
 */
public class JobProgressException  extends Exception{
    public JobProgressException() {
    }

    public JobProgressException(String message) {
        super(message);
    }

    public JobProgressException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobProgressException(Throwable cause) {
        super(cause);
    }

    public JobProgressException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
