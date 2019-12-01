
package com.bayraktar.learnenglish.Models.Oxford;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pronunciation {

    @SerializedName("audioFile")
    @Expose
    public String audioFile;
    @SerializedName("dialects")
    @Expose
    public List<String> dialects = null;
    @SerializedName("phoneticNotation")
    @Expose
    public String phoneticNotation;
    @SerializedName("phoneticSpelling")
    @Expose
    public String phoneticSpelling;

}
