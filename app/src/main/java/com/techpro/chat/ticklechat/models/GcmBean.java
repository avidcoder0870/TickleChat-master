package com.techpro.chat.ticklechat.models;

import lombok.Getter;
import lombok.Setter;

public class GcmBean {
    @Getter
    @Setter
    private Data data;

    public class Data {
        @Getter
        @Setter
        private String id;

        @Getter
        @Setter
        private String token;

        @Getter
        @Setter
        private String uid;

        @Getter
        @Setter
        private String channel;
    }
}