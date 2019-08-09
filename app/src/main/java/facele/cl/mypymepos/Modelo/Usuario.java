package facele.cl.mypymepos.Modelo;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String abonadoIdentificacion = "17726389-3";
    private int datafonoIdentificacion = 1234123;
    private String vendedorEmail = "vendedor@vendedor.cl";

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
}
