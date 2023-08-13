package com.ddchat_server.controller.dto;

import lombok.Data;

@Data
public class ResponseDto<T> {
    private Boolean success;
    private String message;
    private String code;
    private T data;
    public ResponseDto(T data){
        this.success = true;
        this.message = null;
        this.data = data;
        this.code = "S1";
    }
    public ResponseDto(Boolean s, String m){
        this.success = s;
        this.message = m;
        this.code = "E2";
        this.data = null;
    }
}
