package com.lv.api.controller;

import com.lv.api.dto.ApiMessageDto;
import com.lv.api.dto.ErrorCode;
import com.lv.api.dto.ResponseListObj;
import com.lv.api.dto.rank.RankDto;
import com.lv.api.exception.RequestException;
import com.lv.api.form.rank.CreateRankForm;
import com.lv.api.form.rank.UpdateRankForm;
import com.lv.api.mapper.RankMapper;
import com.lv.api.service.CommonApiService;
import com.lv.api.storage.criteria.RankCriteria;
import com.lv.api.storage.model.Rank;
import com.lv.api.storage.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/ranks")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class RankController extends ABasicController {

    private final RankRepository rankRepository;
    private final RankMapper rankMapper;
    private final CommonApiService commonApiService;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<RankDto>> list(RankCriteria rankCriteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.CATEGORY_ERROR_UNAUTHORIZED, "Not allowed get list.");
        }
        Page<Rank> pageRanks = rankRepository.findAll(rankCriteria.getSpecification(), pageable);
        List<RankDto> listRankDto = rankMapper.fromListEntityToListRankDto(pageRanks.getContent());
        return new ApiMessageDto<>(new ResponseListObj<>(listRankDto, pageRanks), "Get list success");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<RankDto> get(@PathVariable("id") Long id) {
        Rank result = rankRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.RANK_ERROR_NOT_FOUND, "Rank not found"));
        return new ApiMessageDto<>(rankMapper.fromRankEntityToRankDto(result), "Get rank successfully");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateRankForm createRankForm) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.CATEGORY_ERROR_UNAUTHORIZED, "Not allowed to create.");
        }
        rankRepository.findByName(createRankForm.getName())
                .ifPresent(r -> {
                    throw new RequestException(ErrorCode.RANK_ERROR_DUPLICATE_NAME, String.format("Rank %s was existed", r.getName()));
                });
        Rank rank = rankMapper.fromCreateRankFormToEntity(createRankForm);
        rankRepository.save(rank);
        return new ApiMessageDto<>("Create rank successfully.");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateRankForm updateRankForm) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.CATEGORY_ERROR_UNAUTHORIZED, "Not allowed to update.");
        }
        Rank rank = rankRepository.findById(updateRankForm.getId())
                .orElseThrow(() -> new RequestException(ErrorCode.RANK_ERROR_NOT_FOUND, "Rank not found"));
        rankRepository.findByName(updateRankForm.getName())
                .ifPresent(r -> {
                    throw new RequestException(ErrorCode.RANK_ERROR_DUPLICATE_NAME, String.format("Rank %s was existed", r.getName()));
                });
        if (StringUtils.isNoneBlank(rank.getAvatar()) && !updateRankForm.getAvatar().equals(rank.getAvatar())) {
            commonApiService.deleteFile(rank.getAvatar());
        }
        rankMapper.formUpdateRankFormToEntity(updateRankForm, rank);
        rankRepository.save(rank);
        return new ApiMessageDto<>("Update rank successfully");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Not allowed to delete.");
        }
        Rank rank = rankRepository.findById(id)
                .orElseThrow(() -> new RequestException(ErrorCode.RANK_ERROR_NOT_FOUND, "Rank not found"));
        commonApiService.deleteFile(rank.getAvatar());
        rankRepository.delete(rank);
        return new ApiMessageDto<>("Delete rank successfully");
    }
}
