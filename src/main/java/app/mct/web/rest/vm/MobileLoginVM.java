package app.mct.web.rest.vm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * View Model object for storing a user's credentials.
 */
public class MobileLoginVM {

    @NotNull
    @Size(min = 1, max = 50)
    private String mobileNumber;

    @NotNull
    @Size(min = 4, max = 100)
    private String email;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "MobileLoginVM{" +
            "mobileNumber='" + mobileNumber + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}
