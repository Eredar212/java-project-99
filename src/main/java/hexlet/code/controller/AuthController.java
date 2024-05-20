package hexlet.code.controller;

import hexlet.code.dto.AuthDTO;
import hexlet.code.service.CustomUserDetailsService;
import hexlet.code.utils.JWTUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Аутентификация")
public class AuthController {
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    public String login(@RequestBody @Valid AuthDTO authDTO) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(authDTO.getUsername());
            if (!passwordEncoder.matches(authDTO.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Bad credentials");
            }
        } catch (UsernameNotFoundException exception) {
            throw new BadCredentialsException("Bad credentials");
        }
        var authentication = new UsernamePasswordAuthenticationToken(authDTO.getUsername(), authDTO.getPassword());
        authenticationManager.authenticate(authentication);
        return jwtUtils.generateToken(authDTO.getUsername());
    }

}
