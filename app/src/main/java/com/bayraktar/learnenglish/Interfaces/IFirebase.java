package com.bayraktar.learnenglish.Interfaces;

import com.bayraktar.learnenglish.Models.Firebase.Word;

import java.util.List;

public interface IFirebase {

    void onGetWordList(List<Word> wordList);

    void onSetWord(boolean isSuccess);
}
