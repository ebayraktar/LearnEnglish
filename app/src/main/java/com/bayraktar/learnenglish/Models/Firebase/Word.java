package com.bayraktar.learnenglish.Models.Firebase;

public class Word {
    private String en;
    private String tr;
    private String sp;
    private String parts_of_speech;

    public Word(String en, String tr, String sp, String parts_of_speech) {
        this.en = en;
        this.tr = tr;
        this.sp = sp;
        this.parts_of_speech = parts_of_speech;
    }

    public Word() {
    }

    public String getEn() {
        return en;
    }

    public String getTr() {
        return tr;
    }

    public String getSp() {
        return sp;
    }

    public String getParts_of_speech() {
        return parts_of_speech;
    }

}
