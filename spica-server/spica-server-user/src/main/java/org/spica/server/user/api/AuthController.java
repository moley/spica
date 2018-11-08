package org.spica.server.user.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.server.user.domain.User;
import org.spica.server.user.domain.UserRepository;
import org.spica.server.user.service.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@CrossOrigin
public class AuthController {

  private final static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);


  private final UserRepository userRepository;

  private final TokenProvider tokenProvider;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  public AuthController(PasswordEncoder passwordEncoder, UserRepository userService,
                        TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
    this.userRepository = userService;
    this.tokenProvider = tokenProvider;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }

  @GetMapping("/authenticate")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void authenticate() {
    // we don't have to do anything here
    // this is just a secure endpoint and the JWTFilter
    // validates the token
    // this service is called at startup of the app to check
    // if the jwt token is still valid
  }

  @PostMapping("/login")
  public String authorize(@Valid @RequestBody User loginUser,
                          HttpServletResponse response) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
      loginUser.getUsername(), loginUser.getPassword());

    try {
      this.authenticationManager.authenticate(authenticationToken);
      return this.tokenProvider.createToken(loginUser.getUsername());
    }
    catch (AuthenticationException e) {
      LOGGER.error("Security exception {}", e.getMessage(), e);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return null;
    }
  }

  @PostMapping("/signup")
  public String signup(@RequestBody User signupUser) {
    if (this.userRepository.findByUsername(signupUser.getUsername()) != null) {
      return "EXISTS";
    }

    signupUser.setPassword(this.passwordEncoder.encode(signupUser.getPassword()));
    this.userRepository.save(signupUser);
    return this.tokenProvider.createToken(signupUser.getUsername());
  }
}
