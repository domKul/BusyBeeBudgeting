package pl.dicedev.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.dicedev.builders.AssetDtoBuilder;
import pl.dicedev.builders.AssetEntityBuilder;
import pl.dicedev.enums.ValidatorsAssetEnum;
import pl.dicedev.excetpions.AssetIncompleteException;
import pl.dicedev.mappers.AssetsMapper;
import pl.dicedev.repositories.AssetsRepository;
import pl.dicedev.repositories.entities.AssetEntity;
import pl.dicedev.services.dtos.AssetDto;
import pl.dicedev.validators.AssetsFilterRange;
import pl.dicedev.validators.ExpensesFilteredRange;
import pl.dicedev.validators.FilterRangeFactory;
import pl.dicedev.validators.FilterValidatorFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AssetsServiceTest {

    @Mock
    private AssetsRepository assetsRepository;
    @Mock
    private UserLogInfoService userLogInfoService;

    private final FilterValidatorFactory assetValidator = new FilterValidatorFactory();

    private final AssetsMapper assetsMapper = new AssetsMapper();

    ExpensesFilteredRange expensesFilteredRange;
    AssetsFilterRange assetsFilterRangee;

    private final FilterRangeFactory assetsFilterRange = new FilterRangeFactory(expensesFilteredRange, assetsFilterRangee);

    private AssetsService service;

    @BeforeEach
    public void init() {
        service = new AssetsService(
                assetsRepository,
                assetsMapper,
                assetsFilterRange,
                userLogInfoService
                );

    }

    @Test
    void shouldReturnListWithOneElementIfThereIsOneElementInDatabase() {
        // given
        var asset = BigDecimal.ONE;
        AssetEntity assetEntity = new AssetEntityBuilder()
                .withAmount(asset)
                .build();
        List<AssetEntity> assetList = Collections.singletonList(assetEntity);
        Mockito.when(assetsRepository.getAssetEntitiesByUser(any())).thenReturn(assetList);

        // when
        var result = service.getAllAssets();

        // then
        Assertions.assertThat(result)
                .hasSize(1)
                .contains(new AssetDtoBuilder().withAmount(asset).build());
    }

    @Test
    void shouldReturnListWithTwoElementsIfThereWasTwoElementsInDatabase() {
        // given
        var assetOne = BigDecimal.ONE;
        var assetTwo = new BigDecimal("2");
        AssetEntity entityOne = new AssetEntityBuilder()
                .withAmount(assetOne)
                .build();
        AssetEntity entityTwo = new AssetEntityBuilder()
                .withAmount(assetTwo)
                .build();
        List<AssetEntity> assetsEntity = asList(entityOne, entityTwo);

        Mockito.when(assetsRepository.getAssetEntitiesByUser(any())).thenReturn(assetsEntity);

        // when
        var result = service.getAllAssets();

        // then
        Assertions.assertThat(result)
                .hasSize(2)
                .containsExactly(
                        new AssetDtoBuilder().withAmount(assetOne).build(),
                        new AssetDtoBuilder().withAmount(assetTwo).build()
                );
    }

    @Test
    void shouldVerifyIfTheRepositorySaveWasCalledOneTime() {
        // given
        BigDecimal asset = BigDecimal.ONE;
        Instant incomeDate = Instant.now();
        AssetDto dto = new AssetDtoBuilder()
                .withAmount(asset)
                .withIncomeDate(incomeDate)
                .build();
        AssetEntity entity = new AssetEntityBuilder()
                .withAmount(asset)
                .withIncomeDate(incomeDate)
                .build();

        // when
        service.setAsset(dto);

        // then
        Mockito.verify(assetsRepository, Mockito.times(1)).save(entity);

    }

    @Test
    void shouldVerifyIfTheRepositoryUpdateWasCalled() {
        // given
        BigDecimal asset = BigDecimal.ONE;
        var dto = new AssetDtoBuilder().withAmount(asset).build();
        var entity = new AssetEntityBuilder().withAmount(asset).build();
        Mockito.when(assetsRepository.findById(any())).thenReturn(Optional.of(entity));

        // when
        service.updateAsset(dto);

        // then
        Mockito.verify(assetsRepository, Mockito.times(1)).saveAndFlush(entity);
    }

    @Test
    void shouldThrowExceptionWhenAmountInAssetDtoIsNull() {
        // given
        AssetDto dto = new AssetDtoBuilder()
                .withIncomeDate(Instant.now())
                .build();

        // when
        var result = assertThrows(AssetIncompleteException.class,
                () -> service.setAsset(dto));

        // then
        assertEquals(ValidatorsAssetEnum.NO_AMOUNT.getMessage(), result.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenIncomeDateInAssetDtoIsNull() {
        // given
        AssetDto dto = new AssetDtoBuilder()
                .withAmount(BigDecimal.ONE)
                .build();

        // when
        var result = assertThrows(AssetIncompleteException.class,
                () -> service.setAsset(dto));

        // then
        assertEquals(ValidatorsAssetEnum.NO_INCOME_DATE.getMessage(), result.getMessage());

    }

    @Test
    void shouldThrowExceptionWhenIncomeDateAndAmountInAssetDtoIsNull() {
        // given
        AssetDto dto = new AssetDto();

        String messageSeparator = "; ";
        String expectedMessage = ValidatorsAssetEnum.NO_AMOUNT.getMessage()
                + messageSeparator
                + ValidatorsAssetEnum.NO_INCOME_DATE.getMessage();

        // when
        var result = assertThrows(AssetIncompleteException.class,
                () -> service.setAsset(dto));

        // then
        assertEquals(expectedMessage, result.getMessage());
    }

}