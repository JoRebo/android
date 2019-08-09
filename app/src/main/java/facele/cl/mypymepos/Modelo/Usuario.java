package facele.cl.mypymepos.Modelo;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String abonadoIdentificacion;
    private int datafonoIdentificacion;
    private String vendedorEmail;
    private byte[] logo = null;
    private String nombre;
    private String rut;

    public String getVendedorEmail() {
        return vendedorEmail;
    }

    public void setVendedorEmail(String vendedorEmail) {
        this.vendedorEmail = vendedorEmail;
    }

    public String getAbonadoIdentificacion() {
        return abonadoIdentificacion;
    }

    public void setAbonadoIdentificacion(String abonadoIdentificacion) {
        this.abonadoIdentificacion = abonadoIdentificacion;
    }

    public int getDatafonoIdentificacion() {
        return datafonoIdentificacion;
    }

    public void setDatafonoIdentificacion(int datafonoIdentificacion) {
        this.datafonoIdentificacion = datafonoIdentificacion;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }
}
