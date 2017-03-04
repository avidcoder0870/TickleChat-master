package com.techpro.chat.ticklechat.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ErrorBean {
    @Getter
    @Setter
    private List<Errors> errors;

    public class Errors {
        @Getter
        @Setter
        private String message;

        @Getter
        @Setter
        private String http_code;

        @Getter
        @Setter
        private String code;
    }
}
