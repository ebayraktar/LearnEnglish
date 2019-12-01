
package com.bayraktar.learnenglish.Models.Oxford;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("registers")
    @Expose
    public List<Register> registers = null;
    @SerializedName("notes")
    @Expose
    public List<Note> notes = null;

}
