package pl.dicedev.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.dicedev.excetpions.BudgetUserAlreadyExistsInDatabaseException;
import pl.dicedev.excetpions.BudgetUserNotFoundException;
import pl.dicedev.mappers.UserMapper;
import pl.dicedev.repositories.UserRepository;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.UserDetailsDto;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class.getName());

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserLogInfoService userLogInfoService;
    private final AssetsService assetsService;

    public UserDetailsServiceImpl(UserRepository userRepository, UserMapper userMapper, UserLogInfoService userLogInfoService, AssetsService assetsService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userLogInfoService = userLogInfoService;
        this.assetsService = assetsService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Searching user = " + username);
        var entity = userRepository
                .findByUsername(username)
                .orElseThrow(BudgetUserNotFoundException::new);

        return new User(entity.getUsername(), entity.getPassword(), Collections.emptyList());
    }

    public UUID saveUser(UserDetailsDto userDetailsDto) {
        validateIfUserExists(userDetailsDto);
        var entity = userMapper.fromDtoToEntity(userDetailsDto);
        var savedEntity = userRepository.save(entity);
        LOGGER.info("User saved = " + savedEntity);

        return savedEntity.getId();
    }

    @Transactional
    public void deleteUser() {
        UserEntity userEntity = userLogInfoService.getLoggedUserEntity();
        assetsService.deleteAssetByUser(userEntity);
        userRepository.delete(userEntity);
    }

    private void validateIfUserExists(UserDetailsDto userDetailsDto) {
        var entity = userRepository.findByUsername(userDetailsDto.getUsername());

        if (entity.isPresent()) {
            throw new BudgetUserAlreadyExistsInDatabaseException();
        }
    }
}
