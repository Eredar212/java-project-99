package hexlet.code.controller;

import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusService;
import hexlet.code.util.ModelGenerator;
import hexlet.code.utils.UserUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private ModelGenerator modelGenerator;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    private TaskStatus testTask;

    @BeforeEach
    public void setUp() {
        testTask = Instancio.of(modelGenerator.getTaskModel()).create();
        token = jwt().jwt(builder -> builder.subject(userUtils.getTestUser().getEmail()));
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/task_statuses"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        taskStatusRepository.save(testTask);

        var request = get("/api/task_statuses/{id}", testTask.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testTask.getName()),
                v -> v.node("slug").isEqualTo(testTask.getSlug())
        );
    }

    @Test
    public void testUDeleteWithoutAuth() throws Exception {
        taskStatusRepository.save(testTask);
        var request = delete("/api/task_statuses/{id}", testTask.getId());

        mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUDeleteWithAuth() throws Exception {
        taskStatusRepository.save(testTask);
        var request = delete("/api/task_statuses/{id}", testTask.getId()).with(token);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }
}
