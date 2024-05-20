package hexlet.code.dto.task;

import hexlet.code.model.Label;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskUpdateDTO {
    private String title;
    private String content;

    private List<Label> labels;
}
