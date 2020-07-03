package app.mct.service;

public class MobileNumberAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MobileNumberAlreadyUsedException() {
        super("Mobile number is already in use!");
    }

}
