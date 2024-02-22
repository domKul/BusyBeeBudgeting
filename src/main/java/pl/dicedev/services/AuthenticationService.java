package pl.dicedev.services;

import pl.dicedev.excetpions.BudgetInvalidUsernameOrPasswordException;
import pl.dicedev.services.dtos.AuthenticationJwtToken;
import pl.dicedev.services.dtos.UserDetailsDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserDetailsService userDetailsService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserDetailsServiceImpl userDetailsService,
                                 JWTService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationJwtToken createAuthenticationToken(UserDetailsDto userDetailsDto) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userDetailsDto.getUsername(), userDetailsDto.getPassword()
            ));
        } catch (BadCredentialsException | InternalAuthenticationServiceException ex) {
            throw new BudgetInvalidUsernameOrPasswordException();
        }

        var userDetails = userDetailsService.loadUserByUsername(userDetailsDto.getUsername());
        var jwtToken = jwtService.generateJWTToken(userDetails);

        return new AuthenticationJwtToken(jwtToken);
    }
}
