
package com.bayraktar.learnenglish.Models.Oxford;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("lexicalEntries")
    @Expose
    public List<LexicalEntry> lexicalEntries = null;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("word")
    @Expose
    public String word;

}
