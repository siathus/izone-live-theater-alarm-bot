package com.izone.onethestorycgvalarm.dto;

import java.util.HashMap;
import java.util.Map;

public class NaverShortUrlResponseDto {
    private String message;
    private String code;
    private Map<String, String> result = new HashMap<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }
}
