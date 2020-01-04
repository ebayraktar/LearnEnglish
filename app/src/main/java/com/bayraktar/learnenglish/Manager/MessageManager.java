package com.bayraktar.learnenglish.Manager;

import com.bayraktar.learnenglish.Models.Message;
import com.bayraktar.learnenglish.R;

public class MessageManager {
    private int language;

    public MessageManager(int language) {
        this.language = language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public Message getExit() {
        if (language == 0) {
            return en_exit;
        } else if (language == 1) {
            return tr_exit;
        } else if (language == 2) {
            return es_exit;
        }
        return null;
    }

    private Message en_exit = new Message("EXIT", "Are you sure you want to exit?", "EXIT", "CANCEL", R.drawable.ic_info_red_24dp);
    private Message tr_exit = new Message("ÇIKIŞ", "Çıkmak istediğine emin misin?", "ÇIKIŞ", "İPTAL", R.drawable.ic_info_red_24dp);
    private Message es_exit = new Message("SALIDA", "Seguro que quieres salir?", "SALIDA", "CANCELACIÓN", R.drawable.ic_info_red_24dp);
}
