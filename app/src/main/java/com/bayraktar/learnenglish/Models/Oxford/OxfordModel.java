
package com.bayraktar.learnenglish.Models.Oxford;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OxfordModel {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("metadata")
    @Expose
    public Metadata metadata;
    @SerializedName("results")
    @Expose
    public List<Result> results = null;
    @SerializedName("word")
    @Expose
    public String word;

}
