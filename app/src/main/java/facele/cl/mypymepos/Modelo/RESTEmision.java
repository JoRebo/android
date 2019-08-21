package facele.cl.mypymepos.Modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RESTEmision {

    @SerializedName("sucursal")
    @Expose
    private RESTSucursal sucursal;
    @SerializedName("tipoDTE")
    @Expose
    private Integer tipoDTE;
    @SerializedName("folioDTE")
    @Expose
    private Integer folioDTE;
    @SerializedName("fechaEmision")
    @Expose
    private String fechaEmision;
    @SerializedName("montoTotal")
    @Expose
    private Integer montoTotal;
    @SerializedName("vendedor")
    @Expose
    private RESTVendedor vendedor;
    @SerializedName("receptor")
    @Expose
    private RESTReceptor receptor;
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("fechaRegistro")
    @Expose
    private String fechaRegistro;
    @SerializedName("id")
    @Expose
    private Integer id;

    public RESTSucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(RESTSucursal sucursal) {
        this.sucursal = sucursal;
    }

    public Integer getTipoDTE() {
        return tipoDTE;
    }

    public void setTipoDTE(Integer tipoDTE) {
        this.tipoDTE = tipoDTE;
    }

    public Integer getFolioDTE() {
        return folioDTE;
    }

    public void setFolioDTE(Integer folioDTE) {
        this.folioDTE = folioDTE;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Integer getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Integer montoTotal) {
        this.montoTotal = montoTotal;
    }

    public RESTVendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(RESTVendedor vendedor) {
        this.vendedor = vendedor;
    }

    public RESTReceptor getReceptor() {
        return receptor;
    }

    public void setReceptor(RESTReceptor receptor) {
        this.receptor = receptor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}