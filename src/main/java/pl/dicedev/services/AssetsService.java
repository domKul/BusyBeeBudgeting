package pl.dicedev.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.dicedev.enums.AssetCategory;
import pl.dicedev.mappers.AssetsMapper;
import pl.dicedev.repositories.AssetsRepository;
import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.AssetDto;
import pl.dicedev.validators.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AssetsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetsService.class);
    private final AssetsRepository assetsRepository;
    private final AssetsMapper assetsMapper;
    private final AssetValidator assetValidator;
    private final UserLogInfoService userLogInfoService;
    private final FilteredRange assetsFilterRange;

    public AssetsService(AssetsRepository assetsRepository,
                         AssetsMapper assetsMapper,
                         FilterRangeFactory filterRangeFactory,
                         UserLogInfoService userLogInfoService) {
        this.assetsRepository = assetsRepository;
        this.assetsMapper = assetsMapper;
        this.assetValidator = (AssetValidator) FilterValidatorFactory.getValidator("assets");
        this.userLogInfoService = userLogInfoService;
        this.assetsFilterRange = filterRangeFactory.getFilterRange("assets");
    }

    public List<AssetDto> getAllAssets() {
        LOGGER.debug("Get all assets");
        var user = getUserEntity();
        return assetsRepository.getAssetEntitiesByUser(user)
                .stream()
                .map(assetsMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public void setAsset(AssetDto dto) {
        LOGGER.info("Set Asset");
        LOGGER.debug("AssetDto: " + dto);
        assetValidator.validate(dto);
        var user = getUserEntity();
        var entity = assetsMapper.fromDtoToEntity(dto, user);
        assetsRepository.save(entity);
        LOGGER.info("Asset Saved");
    }

    public void deleteAsset(AssetDto dto) {
        LOGGER.info("Delete asset");
        LOGGER.debug("AssetDto: " + dto);
        var user = getUserEntity();
        var entity = assetsMapper.fromDtoToEntity(dto, user);
        assetsRepository.delete(entity);
        LOGGER.info("Asset deleted");
    }

    public void updateAsset(AssetDto dto) {
        LOGGER.info("Update asset");
        LOGGER.debug("AssetDto: " + dto);
        var entity = assetsRepository.findById(dto.getId());
        entity.ifPresent(e -> {
            e.setAmount(dto.getAmount());
            assetsRepository.saveAndFlush(e);
        });
        LOGGER.info("Asset updated");
    }

    public List<AssetDto> getAssetsByCategory(AssetCategory category) {
        return assetsRepository.getAssetEntitiesByCategory(category)
                .stream()
                .map(assetsMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public void deleteAssetByUser(UserEntity userEntity) {
        assetsRepository.deleteAllByUser(userEntity);
    }

    private UserEntity getUserEntity() {
        LOGGER.info("getLoggedUserEntity");
        return userLogInfoService.getLoggedUserEntity();
    }
    public List<AssetDto>getAssetsByFilter(Map<String, String> filters){
        UserEntity loggedUserEntity = userLogInfoService.getLoggedUserEntity();
        return assetsFilterRange.getAllByFilter(loggedUserEntity,filters);
    }
}
