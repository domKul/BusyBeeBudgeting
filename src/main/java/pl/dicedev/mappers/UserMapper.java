package pl.dicedev.mappers;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.UserDetailsDto;

@Component
public class UserMapper {
    public UserEntity fromDtoToEntity(UserDetailsDto userDetailsDto) {
        var entity = new UserEntity();

        entity.setUsername(userDetailsDto.getUsername());
        entity.setPassword(encodePassword(userDetailsDto.getPassword()));

        return entity;
    }

    private String encodePassword(String password) {
        var salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }
}
