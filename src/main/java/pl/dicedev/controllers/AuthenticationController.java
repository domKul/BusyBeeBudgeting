package pl.dicedev.controllers;

import pl.dicedev.services.AuthenticationService;
import pl.dicedev.services.UserDetailsServiceImpl;
import pl.dicedev.services.dtos.AuthenticationJwtToken;
import pl.dicedev.services.dtos.UserDetailsDto;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthenticationController(AuthenticationService authenticationService, UserDetailsServiceImpl userDetailsService) {
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public AuthenticationJwtToken getAuthenticationToken(@RequestBody UserDetailsDto userDetailsDto) {
        return authenticationService.createAuthenticationToken(userDetailsDto);
    }

    @PostMapping
    public UUID setUserDetails(@RequestBody UserDetailsDto userDetailsDto) {
        return userDetailsService.saveUser(userDetailsDto);
    }

    @DeleteMapping
    public void deleteUser() {
        userDetailsService.deleteUser();
    }

}
