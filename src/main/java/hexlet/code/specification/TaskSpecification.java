package hexlet.code.specification;

import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {
    public Specification<Task> build(TaskParamsDTO params) {
        return  withAssigneeId(params.getAssigneeId())
                .and(withLabel(params.getLabelId()))
                .and(withStatus(params.getStatus()))
                .and(withTitleCont(params.getTitleCont()));
    }

    public Specification<Task> withTitleCont(String title) {
        return ((root, query, criteriaBuilder) -> title == null
        ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("name"), title));
    }

    public Specification<Task> withAssigneeId(Long id) {
        return ((root, query, criteriaBuilder) -> id == null
        ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("assignee").get("id"), id));
    }

    public Specification<Task> withStatus(String status) {
        return ((root, query, criteriaBuilder) -> status == null
                ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("taskStatus").get("slug"), status));
    }

    public Specification<Task> withLabel(Long id) {
        return ((root, query, criteriaBuilder) -> id == null
                ? criteriaBuilder.conjunction()
                : criteriaBuilder.equal(root.joinSet("labels").get("id"), id));
    }

}
