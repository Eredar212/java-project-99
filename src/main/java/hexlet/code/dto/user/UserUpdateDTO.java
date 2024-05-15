package hexlet.code.dto.user;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {

    private String email;

    private String password;
}
