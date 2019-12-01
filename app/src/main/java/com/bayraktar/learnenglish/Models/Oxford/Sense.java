
package com.bayraktar.learnenglish.Models.Oxford;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sense {

    @SerializedName("definitions")
    @Expose
    public List<String> definitions = null;
    @SerializedName("domains")
    @Expose
    public List<Domain> domains = null;
    @SerializedName("examples")
    @Expose
    public List<Example> examples = null;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("shortDefinitions")
    @Expose
    public List<String> shortDefinitions = null;
    @SerializedName("registers")
    @Expose
    public List<Register_> registers = null;
    @SerializedName("subsenses")
    @Expose
    public List<Subsense> subsenses = null;
    @SerializedName("thesaurusLinks")
    @Expose
    public List<ThesaurusLink> thesaurusLinks = null;
    @SerializedName("regions")
    @Expose
    public List<Region> regions = null;

}
