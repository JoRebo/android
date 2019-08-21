package facele.cl.mypymepos.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RESTEmisionObtener {

    @SerializedName("bytesXML")
    @Expose
    private String bytesXML;

    public String getBytesXML() {
        return bytesXML;
    }

    public void setBytesXML(String bytesXML) {
        this.bytesXML = bytesXML;
    }

}