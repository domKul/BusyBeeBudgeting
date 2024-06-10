package pl.dicedev.integrations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import pl.dicedev.builders.AssetEntityBuilder;
import pl.dicedev.enums.AssetCategory;
import pl.dicedev.repositories.AssetsRepository;
import pl.dicedev.repositories.ExpensesRepository;
import pl.dicedev.repositories.UserRepository;
import pl.dicedev.repositories.entities.AssetEntity;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.AssetsService;
import pl.dicedev.services.ExpensesService;
import pl.dicedev.services.JWTService;
import pl.dicedev.services.UserDetailsServiceImpl;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Arrays.asList;

@SpringBootTest
@Transactional
@WithMockUser(username = "user123", password = "123user")
public abstract class InitIntegrationTestData {


    @Autowired
    protected ExpensesService expensesService;
    @Autowired
    protected ExpensesRepository expensesRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected UserDetailsServiceImpl userDetailsService;
    @Autowired
    protected JWTService jwtService;
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    protected AssetsRepository assetsRepository;
    @Autowired
    protected AssetsService service;

    protected static final String USER_NAME = "user123";
    protected static final String USER_PASSWORD = "123user";

     void initDatabaseByAssetsForUser(UserEntity userEntity) {
        var assetEntity = new AssetEntityBuilder()
                .withIncomeDate(Instant.now())
                .withUser(userEntity)
                .withAmount(BigDecimal.ONE)
                .withCategory(AssetCategory.BONUS)
                .build();
        assetsRepository.save(assetEntity);
    }

     void initDatabaseByUser() {
        UserEntity entity = new UserEntity();
        entity.setPassword(USER_PASSWORD);
        entity.setUsername(USER_NAME);
        userRepository.save(entity);
    }

     UserEntity initDefaultMockUserInDatabase() {
        var user = new UserEntity();
        user.setUsername("user123");
        user.setPassword("123user");

        return userRepository.save(user);
    }

     void initDataBaseByDefaultMockUserAndHisAssets() {
        var userEntity = initDefaultMockUserInDatabase();
        AssetEntity entity1 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(1))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.OTHER)
                .withUser(userEntity)
                .build();
        AssetEntity entity2 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(3))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.SALARY)
                .withUser(userEntity)
                .build();
        AssetEntity entity3 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(5))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.RENT)
                .withUser(userEntity)
                .build();

        assetsRepository.saveAll(asList(entity1, entity2, entity3));
    }


     void initDataBaseBySecondMockUserAndHisAssets() {
        var userEntity = initSecondMockUserInDatabase();
        AssetEntity entity1 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(1))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.OTHER)
                .withUser(userEntity)
                .build();
        AssetEntity entity2 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(3))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.SALARY)
                .withUser(userEntity)
                .build();
        AssetEntity entity3 = new AssetEntityBuilder()
                .withAmount(new BigDecimal(5))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.RENT)
                .withUser(userEntity)
                .build();
        assetsRepository.saveAll(asList(entity1, entity2, entity3));
    }
     UserEntity initSecondMockUserInDatabase() {
        var user = new UserEntity();
        user.setUsername("secondUser123");
        user.setPassword("123SecondUser");
        return userRepository.save(user);
    }
}
