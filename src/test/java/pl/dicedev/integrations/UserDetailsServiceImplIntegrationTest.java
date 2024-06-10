package pl.dicedev.integrations;

import org.junit.jupiter.api.Test;
import pl.dicedev.enums.AuthenticationMessageEnum;
import pl.dicedev.excetpions.BudgetUserAlreadyExistsInDatabaseException;
import pl.dicedev.excetpions.BudgetUserNotFoundException;
import pl.dicedev.services.dtos.UserDetailsDto;

import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UserDetailsServiceImplIntegrationTest extends InitIntegrationTestData {

    @Test
    void shouldReturnUserWithUserNameAndPasswordFromDatabase() {
        // given
        initDatabaseByUser();

        // when
        var result = userDetailsService.loadUserByUsername(USER_NAME);

        // then
        assertThat(result.getUsername()).isEqualTo(USER_NAME);
        assertThat(result.getPassword()).isEqualTo(USER_PASSWORD);
    }

    @Test
    void shouldSaveUserInToDatabase() {
        // given
        UserDetailsDto dto = new UserDetailsDto();
        dto.setUsername(USER_NAME);
        dto.setPassword(USER_PASSWORD);
        var bCryptPrefix = "$2a$10$";
        var bCryptRegex = "^[$]2[abxy]?[$](?:0[4-9]|[12][0-9]|3[01])[$][./0-9a-zA-Z]{53}$";

        // when
        var userId = userDetailsService.saveUser(dto);

        // then
        assertThat(userId).isNotNull();
        var userEntityOptional = userRepository.findById(userId);
        var userEntity = userEntityOptional.get();
        assertAll(
                () -> assertThat(userEntity.getUsername()).isEqualTo(USER_NAME),
                () -> assertThat(userEntity.getPassword()).contains(bCryptPrefix),
                () -> assertThat(userEntity.getPassword()).matches(Pattern.compile(bCryptRegex))
        );
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotFoundInDatabase() {
        // given
        initDatabaseByUser();

        // when
        var result = assertThrows(BudgetUserNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("fakeUser"));

        // then
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.USER_NOT_FOUND.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExistsInDatabase() {
        // given
        initDatabaseByUser();
        UserDetailsDto dto = new UserDetailsDto();
        dto.setPassword(USER_PASSWORD);
        dto.setUsername(USER_NAME);

        // when
        var result = assertThrows(BudgetUserAlreadyExistsInDatabaseException.class,
                () -> userDetailsService.saveUser(dto));

        // then
        assertThat(result.getMessage()).isEqualTo(AuthenticationMessageEnum.USER_ALREADY_EXISTS.getMessage());

    }

    @Test
    void shouldRemoveUserWhichDoNotHaveAnyAssetsInDatabase() {
        // given
        initDatabaseByUser();

        var userInDatabase = userRepository.findAll();
        assertThat(userInDatabase).hasSize(1);

        // when
        userDetailsService.deleteUser();
        
        // then
        var userInDatabaseAfterRemove = userRepository.findAll();
        assertThat(userInDatabaseAfterRemove).hasSize(0);

    }

    @Test
    void shouldRemoveUserWhichHasOneAssetInDatabase() {
        // given
        initDatabaseByUser();
        var userEntity = userRepository.findByUsername(USER_NAME).get();
        initDatabaseByAssetsForUser(userEntity);

        var userInDatabase = userRepository.findAll();
        assertThat(userInDatabase).hasSize(1);
        var assetsInDatabase = assetsRepository.findAll();
        assertThat(assetsInDatabase).hasSize(1);
        assertThat(assetsInDatabase.get(0).getUser()).isEqualTo(userEntity);

        // when
        userDetailsService.deleteUser();

        // then
        var userInDatabaseAfterDelete = userRepository.findAll();
        assertThat(userInDatabaseAfterDelete).hasSize(0);
        var assetsInDatabaseAfterDelete = assetsRepository.findAll();
        assertThat(assetsInDatabaseAfterDelete).hasSize(0);
    }

}