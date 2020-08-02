package com.bayraktar.learnenglish.Models;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class MobileResult {
    int code;
    String message;
    Object result;

    public MobileResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public <T> T getResult(@NonNull Class<T> valueType) {
        if (result == null || result.equals(""))
            return null;
        Gson gson = new Gson();
        String json = gson.toJson(result);
        return gson.fromJson(json, valueType);
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
