package annotation.exception;

/**
 * Created by Administrator on 2016/8/30.
 */
public class LoginException extends RuntimeException {
    public LoginException(String message){
        super(message);
    }
}
