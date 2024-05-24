package com.vicheak.coreapp.api.salarygross;

import com.vicheak.coreapp.api.salarygross.web.GrossTypeDto;

import java.util.List;

public interface GrossTypeService {

    /**
     * This method is used to load all gross types in the system
     * @return List<GrossTypeDto>
     */
    List<GrossTypeDto> loadAllGrossTypes();

    /**
     * This method is used to load gross type by specific name
     * @param name is the path parameter from client
     * @return GrossTypeDto
     */
    GrossTypeDto loadGrossTypeByName(String name);

    /**
     * This method is used to create new gross type resource
     * @param grossTypeDto is the request from client
     */
    void createNewGrossType(GrossTypeDto grossTypeDto);

    /**
     * This method is used to update gross type by specific name
     * @param name is the path parameter from client
     * @param grossTypeDto is the request from client
     */
    void updateGrossTypeByName(String name, GrossTypeDto grossTypeDto);

    /**
     * This method is used to delete gross type by specific name
     * @param name is the path parameter from client
     */
    void deleteGrossTypeByName(String name);

}
