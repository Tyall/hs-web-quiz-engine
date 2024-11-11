package pl.softyal.webquizengine.users.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

    @Email(regexp = ".+@.+\\..+")
    private String email;

    @Size(min = 5)
    private String password;

}
