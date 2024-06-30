package pl.dicedev.services;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.dicedev.enums.FilterExpensesParametersEnum;
import pl.dicedev.mappers.AssetsMapper;
import pl.dicedev.repositories.AssetsRepository;
import pl.dicedev.repositories.entities.AssetEntity;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.AssetDto;
import pl.dicedev.validators.ExpensesFilterParametersValidator;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AssetsFilterRange extends FilteredRange<AssetEntity, AssetDto> {

    private final AssetsRepository assetsRepository;
    private final AssetsMapper assetsMapper;


    public AssetsFilterRange( AssetsRepository assetsRepository, AssetsMapper assetsMapper) {
        super("assets");
        this.assetsRepository = assetsRepository;
        this.assetsMapper = assetsMapper;
    }

    @Override
    protected List<AssetEntity> getAllEntityBetweenDate(UserEntity user, Map<String, String> filters) {
        return assetsRepository.findByUserAndIncomeDateBetween(
                user,
                Instant.parse(filters.get(FilterExpensesParametersEnum.FROM.getKey())),
                Instant.parse(filters.get(FilterExpensesParametersEnum.TO.getKey()))
        );
    }

    @Override
    protected List<AssetDto> mapEntitiesToDto(List<? extends AssetEntity> entities) {
        return entities.stream()
                .map(assetsMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }
}
