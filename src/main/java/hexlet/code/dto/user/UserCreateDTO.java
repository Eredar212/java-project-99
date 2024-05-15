package hexlet.code.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserCreateDTO {
    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String password;
}
