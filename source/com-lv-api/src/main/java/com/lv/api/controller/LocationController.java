package com.lv.api.controller;

import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.location.LocationDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.location.CreateLocationForm;
import com.lv.api.form.location.UpdateLocationForm;
import com.lv.api.mapper.LocationMapper;
import com.lv.api.storage.model.Location;
import com.lv.api.storage.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/locations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class LocationController extends ABasicController {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @GetMapping(value = "/get-chill-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<List<LocationDto>> getChillLocation(@RequestParam(name = "id", required = false) Long id) {
        List<Location> locations;
        if (id == null) {
            locations= locationRepository.findAll();
        } else {
            Location location = locationRepository.findLocationById(id)
                    .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Location not found"));
            locations = location.getSubLocationList();
        }
        if (locations != null) {
            if(!locations.isEmpty())
                return new ApiMessageDto<>(locationMapper.fromEntityListToLocationDtoList(locations), "List location success");
            throw new RequestException(ErrorCode.LOCATION_ERROR_CHILL_LOCATION_EMPTY, "Chill location is empty");
        }
        throw new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Location not found");
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<LocationDto> getLocation(@RequestParam Long id) {
        Location location = locationRepository.findLocationById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Location not found"));
        return new ApiMessageDto<>(locationMapper.fromEntityToLocationDto(location), "Get location successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateLocationForm createLocationForm) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.CATEGORY_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        Location parentLocation = null;
        if (createLocationForm.getParentId() != null) {
            parentLocation = locationRepository.findLocationById(createLocationForm.getParentId())
                    .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Location not found"));
            if (parentLocation.getKind() - createLocationForm.getKind() != -1)
                throw new RequestException(ErrorCode.LOCATION_ERROR_INVALID_PARENT, "Invalid parent location");
        }
        Location location = locationMapper.fromCreateLocationFromToEntity(createLocationForm, parentLocation);
        locationRepository.save(location);
        return new ApiMessageDto<>("Create location successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateLocationForm updateLocationForm) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.CATEGORY_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        Location location = locationRepository.findLocationById(updateLocationForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Location not found"));
        if(location.getStatus().equals(updateLocationForm.getStatus())) {
            location.getSubLocationList().forEach(l -> l.setStatus(updateLocationForm.getStatus()));
            locationRepository.saveAll(location.getSubLocationList());
        }
        locationMapper.fromUpdateLocationFormToEntity(updateLocationForm, location);
        locationRepository.save(location);
        return new ApiMessageDto<>("Update location successfully");
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Not allowed to delete.");
        }
        Location location = locationRepository.findLocationById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.LOCATION_ERROR_NOTFOUND, "Location not found"));
        locationRepository.delete(location);
        return new ApiMessageDto<>("Delete location successfully");
    }
}
