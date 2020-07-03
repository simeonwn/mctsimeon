package app.mct.web.rest.errors;

public class MobileNumberAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public MobileNumberAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Mobile number is already in use!", "userManagement", "mobilenumberexists");
    }
}
