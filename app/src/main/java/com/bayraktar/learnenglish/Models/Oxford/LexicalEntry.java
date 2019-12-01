
package com.bayraktar.learnenglish.Models.Oxford;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LexicalEntry {

    @SerializedName("entries")
    @Expose
    public List<Entry> entries = null;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("lexicalCategory")
    @Expose
    public LexicalCategory lexicalCategory;
    @SerializedName("pronunciations")
    @Expose
    public List<Pronunciation> pronunciations = null;
    @SerializedName("text")
    @Expose
    public String text;

}
