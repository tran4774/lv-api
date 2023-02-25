package com.lv.api.utils.converters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.productconfig.ProductConfigDto;
import com.lv.api.exception.RequestException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.util.List;

@Slf4j
public class ProductConfigJsonConverter implements AttributeConverter<List<ProductConfigDto>, String> {

    private final Gson gson = new Gson();

    @Override
    public String convertToDatabaseColumn(List<ProductConfigDto> extraVariant) {
        String json;
        try {
            json = gson.toJson(extraVariant);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new RequestException(ErrorCode.GENERAL_ERROR_INVALID, "Variant data is invalid");
        }
        return json;
    }

    @Override
    public List<ProductConfigDto> convertToEntityAttribute(String extraVariantJson) {
        List<ProductConfigDto> extraVariant;
        try {
            extraVariant = gson.fromJson(extraVariantJson, TypeToken.getParameterized(List.class, ProductConfigDto.class).getType());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new RequestException(ErrorCode.GENERAL_ERROR_INVALID, "Variant data is invalid");
        }
        return extraVariant;
    }
}
