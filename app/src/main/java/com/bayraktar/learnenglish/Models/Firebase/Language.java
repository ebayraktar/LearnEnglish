package com.bayraktar.learnenglish.Models.Firebase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Language {
    private Integer approved;
    private List<String> audioFile = null;
    private String code;
    private List<String> definitions = null;
    private List<String> examples = null;
    private Boolean is_deleted;
    private Boolean is_locked;
    private String phonetic_spelling;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public List<String> getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(List<String> audioFile) {
        this.audioFile = audioFile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<String> definitions) {
        this.definitions = definitions;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }

    public Boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public Boolean getIs_locked() {
        return is_locked;
    }

    public void setIs_locked(Boolean is_locked) {
        this.is_locked = is_locked;
    }

    public String getPhonetic_spelling() {
        return phonetic_spelling;
    }

    public void setPhonetic_spelling(String phonetic_spelling) {
        this.phonetic_spelling = phonetic_spelling;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}