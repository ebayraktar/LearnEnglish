
package com.bayraktar.learnenglish.Models.Oxford;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThesaurusLink {

    @SerializedName("entry_id")
    @Expose
    public String entryId;
    @SerializedName("sense_id")
    @Expose
    public String senseId;

}
