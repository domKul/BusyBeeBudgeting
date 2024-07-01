package pl.dicedev.integrations;

import org.junit.jupiter.api.Test;
import pl.dicedev.builders.AssetDtoBuilder;
import pl.dicedev.enums.AssetCategory;
import pl.dicedev.excetpions.AssetIncompleteException;
import pl.dicedev.repositories.entities.AssetEntity;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.AssetDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AssetServiceIntegrationTest extends InitIntegrationTestData {

    @Test
    void shouldReturnListWithThreeElements() {
        // given
        initDataBaseByDefaultMockUserAndHisAssets();

        // when
        var allAssetsInDB = service.getAllAssets();

        // then
        assertThat(allAssetsInDB).hasSize(3);
    }

    @Test
    void shouldAddAssetToDB() {
        // given
        initDatabaseByUser();
        AssetDto dto = new AssetDtoBuilder()
                .withAmount(new BigDecimal(11))
                .withIncomeDate(Instant.now())
                .withCategory(AssetCategory.BONUS)
                .build();

        // when
        service.setAsset(dto);

        // then
        var allAssetInDB = assetsRepository.findAll();
        assertThat(allAssetInDB).hasSize(1);
        var entity = allAssetInDB.get(0);
        assertThat(entity.getCategory()).isEqualTo(dto.getCategory());
        assertThat(entity.getAmount()).isEqualTo(dto.getAmount());
        assertThat(entity.getIncomeDate()).isEqualTo(dto.getIncomeDate());
    }

    @Test
    void shouldReturnListOnlyWithOneCategory() {
        // given
        initDataBaseByDefaultMockUserAndHisAssets();
        var category = AssetCategory.OTHER;

        // when
        var allAssetsWithOneCategory = service.getAssetsByCategory(category);

        // then
        assertThat(allAssetsWithOneCategory).hasSize(1);
    }

    @Test
    void shouldDeleteAllAssetsOfChosenUser() {
        // given
        initDataBaseByDefaultMockUserAndHisAssets();
        initDataBaseBySecondMockUserAndHisAssets();
        int numberOfAllAssets = 6; // changed to reflect correct expectation
        int numberOfLeaveAssets = 3; // changed to reflect correct expectation

        List<UserEntity> all = (List<UserEntity>) userRepository.findAll();
        var userToDeleteAssets = all.get(0); // simplified to get the first user
        var userToLeaveAssets = all.get(1); // simplified to get the second user

        var allAssetsInDatabase = assetsRepository.findAll();
        assertThat(allAssetsInDatabase).hasSize(numberOfAllAssets);

        // when
        service.deleteAssetByUser(userToDeleteAssets);

        // then
        var assetsAfterDelete = assetsRepository.findAll();
        assertThat(assetsAfterDelete).hasSize(numberOfLeaveAssets);

        var assetsUserId = assetsAfterDelete.stream()
                .map(AssetEntity::getUser)
                .map(UserEntity::getId)
                .collect(Collectors.toSet());
        assertThat(assetsUserId).hasSize(1)
                .containsExactly(userToLeaveAssets.getId());
    }

    @Test
    void shouldThrowExceptionIfOneFiltersAreMissing() {
        // Given
        initDatabaseByUser();

        // When
        Map<String, String> filters = new HashMap<>();
        filters.put("from", "2024-06-22");

        AssetIncompleteException exception = assertThrows(AssetIncompleteException.class,
                () -> service.getAssetsByFilter(filters));

        // Then
        String expectedMessage = "to"; // corrected expected message
        assertThat(expectedMessage).isEqualTo(exception.getMessage());
    }
}
