package mujina.idp;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class InvalidAuthenticationException extends AuthenticationException {

    private final String errorMessage;

    public InvalidAuthenticationException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

}
