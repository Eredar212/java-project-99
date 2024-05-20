package hexlet.code.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Сущность пользователя")
public class UserDTO {
    @Schema(description = "Идентификатор", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate createdAt;

}
