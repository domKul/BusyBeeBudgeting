package pl.dicedev.integrations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dicedev.enums.AuthenticationMessageEnum;
import pl.dicedev.excetpions.BudgetInvalidUsernameOrPasswordException;
import pl.dicedev.services.AuthenticationService;
import pl.dicedev.services.dtos.UserDetailsDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AuthenticationServiceIntegrationTest extends InitIntegrationTestData{

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setup() {
        authenticationService = new AuthenticationService(
                userDetailsService,
                jwtService,
                authenticationManager
        );
    }

    @Test
    void shouldThrowAnBudgetInvalidUsernameOrPasswordExceptionWhenUsernameIsIncorrect() {
        // given
        initDatabaseByUser();

        UserDetailsDto dto = new UserDetailsDto();
        dto.setUsername("incorrectUserName");
        dto.setPassword("user123");

        // when
        var result = assertThrows(BudgetInvalidUsernameOrPasswordException.class,
                () -> authenticationService.createAuthenticationToken(dto));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.INVALID_USERNAME_OR_PASSWORD.getMessage());
    }

    @Test
    void shouldThrowAnBudgetInvalidUsernameOrPasswordExceptionWhenPasswordIsIncorrect() {
        // given
        initDatabaseByUser();

        UserDetailsDto dto = new UserDetailsDto();
        dto.setUsername("user123");
        dto.setPassword("IncorrectPassword");

        // when
        var result = assertThrows(BudgetInvalidUsernameOrPasswordException.class,
                () -> authenticationService.createAuthenticationToken(dto));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.INVALID_USERNAME_OR_PASSWORD.getMessage());
    }

}
