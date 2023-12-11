package com.vicheak.coreapp.api.auth.web;

public record VerifyCodeDto(Integer digit1,
                            Integer digit2,
                            Integer digit3,
                            Integer digit4,
                            Integer digit5,
                            Integer digit6) {

    public String getSixDigits() {
        return "" + digit1 + digit2 + digit3 + digit4 + digit5 + digit6;
    }

}
