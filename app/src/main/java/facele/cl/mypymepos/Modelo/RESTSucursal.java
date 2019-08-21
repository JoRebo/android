package facele.cl.mypymepos.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RESTSucursal {

    @SerializedName("codigoSII")
    @Expose
    private Integer codigoSII;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("comuna")
    @Expose
    private String comuna;
    @SerializedName("ciudad")
    @Expose
    private String ciudad;
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("id")
    @Expose
    private Integer id;

    public Integer getCodigoSII() {
        return codigoSII;
    }

    public void setCodigoSII(Integer codigoSII) {
        this.codigoSII = codigoSII;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}