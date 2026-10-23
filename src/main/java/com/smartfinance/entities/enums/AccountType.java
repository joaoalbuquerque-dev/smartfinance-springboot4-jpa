package com.smartfinance.entities.enums;

public enum AccountType {

    CHECKING(1),
    SAVINGS(2),
    CASH(3);

    private final int code;

    AccountType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AccountType fromCode(Integer code) {
        if (code == null) {
            throw new IllegalArgumentException("AccountType code cannot be null");
        }

        for (AccountType value : AccountType.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid AccountType code");
    }
}
