
package com.bayraktar.learnenglish.Models.Oxford;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subsense {

    @SerializedName("definitions")
    @Expose
    public List<String> definitions = null;
    @SerializedName("domains")
    @Expose
    public List<Domain_> domains = null;
    @SerializedName("examples")
    @Expose
    public List<Example_> examples = null;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("shortDefinitions")
    @Expose
    public List<String> shortDefinitions = null;
    @SerializedName("registers")
    @Expose
    public List<Register__> registers = null;
    @SerializedName("notes")
    @Expose
    public List<Note_> notes = null;

}
