package pl.dicedev.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pl.dicedev.excetpions.BudgetUserNotFoundException;
import pl.dicedev.repositories.UserRepository;
import pl.dicedev.repositories.entities.UserEntity;

@Component
public class UserLogInfoService {

    private final UserRepository userRepository;

    public UserLogInfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getLoggedUserEntity() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var username = ((User) authentication.getPrincipal()).getUsername();

        return userRepository.findByUsername(username)
                .orElseThrow(BudgetUserNotFoundException::new);
    }
}
