package com.vicheak.coreapp.api.salarygross;

import com.vicheak.coreapp.api.salarygross.web.GrossTypeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GrossTypeServiceImpl implements GrossTypeService {

    private final GrossTypeRepository grossTypeRepository;
    private final GrossTypeMapper grossTypeMapper;

    @Override
    public List<GrossTypeDto> loadAllGrossTypes() {
        return grossTypeMapper.toGrossTypeDto(grossTypeRepository.findAll());
    }

    @Override
    public GrossTypeDto loadGrossTypeByName(String name) {
        GrossType grossType = grossTypeRepository.findByNameIgnoreCase(name)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Gross type resource has not been found!")
                );

        return grossTypeMapper.toGrossTypeDto(grossType);
    }

    @Override
    public void createNewGrossType(GrossTypeDto grossTypeDto) {
        //check if gross type name already exists
        if (grossTypeRepository.existsByNameIgnoreCase(grossTypeDto.name()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Gross type name conflicts resources in the system!");

        GrossType grossType = grossTypeMapper.fromGrossTypeDto(grossTypeDto);
        grossTypeRepository.save(grossType);
    }

    @Override
    public void updateGrossTypeByName(String name, GrossTypeDto grossTypeDto) {
        //load gross type resource by name
        GrossType grossType = grossTypeRepository.findByNameIgnoreCase(name)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Gross type resource has not been found!")
                );

        //check if gross type name already exists
        if (Objects.nonNull(grossTypeDto.name()))
            if (!grossTypeDto.name().equalsIgnoreCase(grossType.getName()) &&
                    grossTypeRepository.existsByNameIgnoreCase(grossTypeDto.name()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Gross type name conflicts resources in the system!");

        //map from dto to entity (except all null values)
        grossTypeMapper.fromGrossTypeDto(grossType, grossTypeDto);

        //update resource
        grossTypeRepository.save(grossType);
    }

    @Override
    public void deleteGrossTypeByName(String name) {
        //load gross type resource by name
        GrossType grossType = grossTypeRepository.findByNameIgnoreCase(name)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Gross type resource has not been found!")
                );

        grossTypeRepository.delete(grossType);
    }

}
