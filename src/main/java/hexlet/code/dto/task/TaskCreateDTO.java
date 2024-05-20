package hexlet.code.dto.task;

import hexlet.code.model.Label;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskCreateDTO {
    private int index;
    private Long assigneeId;
    private String title;
    private String content;
    private String status;
    private List<Label> labels;
}
