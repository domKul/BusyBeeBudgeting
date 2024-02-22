package pl.dicedev.services;

import pl.dicedev.repositories.entities.UserEntity;
import pl.dicedev.services.dtos.AssetDto;

import java.util.List;

public interface MyServiceInterface {

    List<AssetDto> getAllAssets(UserEntity user);

}
