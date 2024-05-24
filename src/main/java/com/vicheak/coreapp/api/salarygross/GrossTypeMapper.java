package com.vicheak.coreapp.api.salarygross;

import com.vicheak.coreapp.api.salarygross.web.GrossTypeDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GrossTypeMapper {

    GrossTypeDto toGrossTypeDto(GrossType grossType);

    List<GrossTypeDto> toGrossTypeDto(List<GrossType> grossTypes);

    GrossType fromGrossTypeDto(GrossTypeDto grossTypeDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromGrossTypeDto(@MappingTarget GrossType grossType, GrossTypeDto grossTypeDto);

}
