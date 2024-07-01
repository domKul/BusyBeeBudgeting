package pl.dicedev.validators;

import org.springframework.stereotype.Component;
import pl.dicedev.mappers.AssetsMapper;
import pl.dicedev.repositories.AssetsRepository;
import pl.dicedev.repositories.entities.AssetEntity;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.AssetDto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssetsFilterRange extends AbstractFilteredRange<AssetEntity, AssetDto> {

    private final AssetsRepository assetsRepository;
    private final AssetsMapper assetsMapper;

    public AssetsFilterRange(AssetsRepository assetsRepository, AssetsMapper assetsMapper) {
        super("assets");
        this.assetsRepository = assetsRepository;
        this.assetsMapper = assetsMapper;
    }

    @Override
    protected List<AssetEntity> getAllByUserAndDateBetween(UserEntity user, Instant from, Instant to) {
        return assetsRepository.findByUserAndIncomeDateBetween(user, from, to);
    }

    @Override
    protected List<AssetDto> mapEntitiesToDto(List<? extends AssetEntity> entities) {
        return entities.stream().map(assetsMapper::fromEntityToDto).collect(Collectors.toList());
    }
}
