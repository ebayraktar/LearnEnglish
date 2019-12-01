package com.bayraktar.learnenglish.Interfaces;

import java.util.List;

public interface IAPIDataChanged {
    void onKeyChanged(String key);

    void onWordsChanged(String[] words);

    void onDefinitionChanged(String word, List<String> definitionList, List<String> exampleList);

    void onTranslationChanged(List<String> translatedString);

    void onErrorOccurred(String errorMessage);
}
