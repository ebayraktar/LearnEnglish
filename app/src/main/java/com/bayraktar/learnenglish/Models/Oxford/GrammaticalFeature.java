
package com.bayraktar.learnenglish.Models.Oxford;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GrammaticalFeature {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("type")
    @Expose
    public String type;

}
