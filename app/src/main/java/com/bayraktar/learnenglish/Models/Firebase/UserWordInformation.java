package com.bayraktar.learnenglish.Models.Firebase;

public class UserWordInformation {
    String wordID;
    boolean isFav;
    private int approved;

    public UserWordInformation() {
    }

    public String getWordID() {
        return wordID;
    }

    public void setWordID(String wordID) {
        this.wordID = wordID;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }
}
