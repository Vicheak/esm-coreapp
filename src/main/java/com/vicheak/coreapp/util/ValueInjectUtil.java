package com.vicheak.coreapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class ValueInjectUtil {

    @Value("${file.base-uri}")
    private String baseUri;

    //check if the image is already set and then pass the baseUri + name
    public String getImageUri(String name) {
        return Objects.nonNull(name) ? baseUri + name : "no image uri set";
    }

    //check if the default expression for number
    public BigDecimal getBigDecimal(double number){
        return BigDecimal.valueOf(number);
    }

}
