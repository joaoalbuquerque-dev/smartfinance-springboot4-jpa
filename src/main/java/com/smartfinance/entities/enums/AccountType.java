package com.smartfinance.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import tools.jackson.databind.deser.ValueInstantiator;

public enum AccountType {

    CHECKING(1),
    SAVINGS(2),
    CASH(3);

    private final int code;

    private AccountType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AccountType valueOf(int code) {
        for(AccountType value : AccountType.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid AccountType code");
    }
}
