package com.bayraktar.learnenglish.Models;

public class Message {
    public String title;
    public String message;
    public String positiveButton;
    public String negativeButton;
    public int icon;

    public Message() {

    }

    public Message(String title, String message, String positiveButton, String negativeButton, int icon) {
        this.title = title;
        this.message = message;
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.icon = icon;
    }
}
