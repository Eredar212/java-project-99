package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import hexlet.code.utils.JWTUtils;
import hexlet.code.utils.UserUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private AuthController authController;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    private User testUser;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
    }

    @Test
    public void testIndex() throws Exception {
        userRepository.save(testUser);
        var result = mockMvc.perform(get("/api/users").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        userRepository.save(testUser);

        var request = get("/api/users/{id}", testUser.getId()).with(jwt());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("firstName").isEqualTo(testUser.getFirstName()),
                v -> v.node("email").isEqualTo(testUser.getEmail())
        );
    }

    @Test
    public void testCreate() throws Exception {

        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(testUser));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var user = userRepository.findByEmail(testUser.getEmail()).get();

        assertThat(user).isNotNull();
        assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(user.getPassword()).isNotEqualTo(testUser.getPassword());
    }

    @Test
    public void testIndexWithoutAuth() throws Exception {
        userRepository.save(testUser);
        var result = mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testShowWithoutAuth() throws Exception {

        userRepository.save(testUser);

        var request = get("/api/users/{id}", testUser.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUDeleteOtherUser() throws Exception {
        userRepository.save(testUser);
        token = jwt().jwt(builder -> builder.subject(userUtils.getTestUser().getEmail()));
        var request = delete("/api/users/{id}", testUser.getId()).with(token);

        mockMvc.perform(request)
                .andExpect(status().isForbidden());
    }
}
