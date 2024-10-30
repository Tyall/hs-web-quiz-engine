package engine.users.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class RegistrationRequest {

    @Email(regexp = ".+@.+\\..+")
    private String email;

    @Size(min = 5)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
