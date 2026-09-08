package com.smartfinance.entities.enums;

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

    public static AccountType valueOf(int code) {
        for(AccountType value : AccountType.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid AccountType code");
    }
}
