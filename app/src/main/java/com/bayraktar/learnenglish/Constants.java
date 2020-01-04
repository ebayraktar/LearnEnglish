package com.bayraktar.learnenglish;

import com.bayraktar.learnenglish.Manager.MessageManager;

public class Constants {
    public static int BACKGROUND_DRAWABLE = -1;
    public static int BACKGROUND_POSITION = -1;
    public static MessageManager messageManager;

    public static int[] backgroundPositions = new int[]{
            R.drawable.background_0,
            R.drawable.background_1,
            R.drawable.background_2,
            R.drawable.background_3,
            R.drawable.background_4,
            R.drawable.background_5,
            R.drawable.background_6,
            R.drawable.background_7,
            R.drawable.background_8,
            R.drawable.background_9,
            R.drawable.background_10,

    };
    public static int[] languagePositions = new int[]{
            R.drawable.ic_en,
            R.drawable.ic_tr,
            R.drawable.ic_es
    };
    public static String[] localePositions = new String[]{
            "en",
            "tr",
            "es"
    };
}
