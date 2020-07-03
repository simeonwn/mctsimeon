package app.mct.security;

import org.springframework.security.core.AuthenticationException;

public class MobileNumberNotFoundException extends AuthenticationException {
    public MobileNumberNotFoundException(String msg) {
        super(msg);
    }

    public MobileNumberNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
