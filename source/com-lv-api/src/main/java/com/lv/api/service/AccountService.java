package com.lv.api.service;

import com.lv.api.dto.ErrorCode;
import com.lv.api.exception.RequestException;
import com.lv.api.storage.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void checkAccountCreate(String username, String phone, String email) {
        if (accountRepository.countAccountByUsername(username) > 0) {
            throw new RequestException(ErrorCode.ACCOUNT_ERROR_EXISTED, "Username is existed");
        }
        if (accountRepository.countAccountByPhone(phone) > 0) {
            throw new RequestException(ErrorCode.ACCOUNT_ERROR_PHONE_EXISTED, "Phone is existed");
        }
        if (accountRepository.countAccountByEmail(email) > 0) {
            throw new RequestException(ErrorCode.ACCOUNT_ERROR_EMAIL_EXISTED, "Email is existed");
        }
    }

    public void checkAccountUpdate(Long accountId, String phone, String email) {
        if (accountRepository.countByPhoneAndIdNot(phone, accountId) > 0) {
            throw new RequestException(ErrorCode.ACCOUNT_ERROR_PHONE_EXISTED, "Phone is existed");
        }
        if (accountRepository.countByEmailAndIdNot(email, accountId) > 0) {
            throw new RequestException(ErrorCode.ACCOUNT_ERROR_EMAIL_EXISTED, "Email is existed");
        }
    }
}
