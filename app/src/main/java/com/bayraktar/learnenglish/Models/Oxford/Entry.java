
package com.bayraktar.learnenglish.Models.Oxford;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Entry {

    @SerializedName("etymologies")
    @Expose
    public List<String> etymologies = null;
    @SerializedName("homographNumber")
    @Expose
    public String homographNumber;
    @SerializedName("senses")
    @Expose
    public List<Sense> senses = null;
    @SerializedName("grammaticalFeatures")
    @Expose
    public List<GrammaticalFeature> grammaticalFeatures = null;

}
