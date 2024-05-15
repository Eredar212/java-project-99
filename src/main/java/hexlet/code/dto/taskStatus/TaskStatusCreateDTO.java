package hexlet.code.dto.taskStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusCreateDTO {
    private String name;
    private String slug;
}
