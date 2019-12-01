
package com.bayraktar.learnenglish.Models.Oxford;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("operation")
    @Expose
    public String operation;
    @SerializedName("provider")
    @Expose
    public String provider;
    @SerializedName("schema")
    @Expose
    public String schema;

}
