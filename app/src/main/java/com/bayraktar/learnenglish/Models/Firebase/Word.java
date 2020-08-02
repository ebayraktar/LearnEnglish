
package com.bayraktar.learnenglish.Models.Firebase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Word {
    String wordId;
    private Double dispersion;
    private Double frequency;
    private List<String> images = null;
    private Boolean isDeleted;
    private Boolean isLocked;
    private List<Language> language = null;
    private String partOfSpeechId;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public Word() {
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public Double getDispersion() {
        return dispersion;
    }

    public void setDispersion(Double dispersion) {
        this.dispersion = dispersion;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public List<Language> getLanguage() {
        return language;
    }

    public void setLanguage(List<Language> language) {
        this.language = language;
    }

    public String getPartOfSpeechId() {
        return partOfSpeechId;
    }

    public void setPartOfSpeechId(String partOfSpeechId) {
        this.partOfSpeechId = partOfSpeechId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}