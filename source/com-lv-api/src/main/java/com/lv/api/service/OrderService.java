package com.lv.api.service;

import com.lv.api.constant.Constants;
import com.lv.api.dto.ErrorCode;
import com.lv.api.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class OrderService {

    private static final Map<Integer, Set<Integer>> EMPLOYEE_STATE_MAP;

    static {
        EMPLOYEE_STATE_MAP = new ConcurrentHashMap<>();
        EMPLOYEE_STATE_MAP.put(Constants.ORDER_STATUS_NEW, Set.of(Constants.ORDER_STATUS_PAID, Constants.ORDER_STATUS_CHECKOUT));
        EMPLOYEE_STATE_MAP.put(Constants.ORDER_STATUS_PAID, Set.of(Constants.ORDER_STATUS_CHECKOUT, Constants.ORDER_STATUS_COMPLETED));
        EMPLOYEE_STATE_MAP.put(Constants.ORDER_STATUS_CHECKOUT, Set.of(Constants.ORDER_STATUS_FAILED, Constants.ORDER_STATUS_DELIVERED));
        EMPLOYEE_STATE_MAP.put(Constants.ORDER_STATUS_DELIVERED, Set.of(Constants.ORDER_STATUS_COMPLETED));
        EMPLOYEE_STATE_MAP.put(Constants.ORDER_STATUS_COMPLETED, Set.of(Constants.ORDER_STATUS_RETURNED));
        EMPLOYEE_STATE_MAP.put(Constants.ORDER_STATUS_FAILED, Set.of());
        EMPLOYEE_STATE_MAP.put(Constants.ORDER_STATUS_RETURNED, Set.of());
    }

    public List<Integer> getAvailableStatesEmployee(Integer state) {
        if (EMPLOYEE_STATE_MAP.containsKey(state)) {
            return new ArrayList<>(EMPLOYEE_STATE_MAP.get(state));
        } else {
            throw new RequestException(ErrorCode.ORDER_STATUS_NOT_FOUND, "Order status not found");
        }
    }

    public boolean checkValidNextStateEmployee(Integer currentState, Integer nextState) {
        if (EMPLOYEE_STATE_MAP.containsKey(currentState)) {
            return EMPLOYEE_STATE_MAP.get(currentState).contains(nextState);
        } else {
            throw new RequestException(ErrorCode.ORDER_STATUS_NOT_FOUND, "Order status not found");
        }
    }
}
