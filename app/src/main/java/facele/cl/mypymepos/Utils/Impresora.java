package facele.cl.mypymepos.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Impresora {

    private SDKAPI sdk;
    private Context context;
    private boolean isBusy = false;

    public Impresora(Context context) {
        sdk = SDKAPI.getInstance();
        this.context = context;
        sdk.setPrnGray(0);
    }

    public void imprimir(String abonadoIdentificacion, String razonSocialEmisor, String tipoDTE, String folio, String direccionCasaMatriz, String comunaCasaMatriz, String ciudadCasaMatriz, String direccionSucursal, String comunaSucursal, String ciudadSucursal, String codigoSucursal, String giro, String fechaEmision, String rutReceptor, String nombreReceptor, String venta, String monto, byte[] logo, Bitmap ted) {
        if (isBusy) {
            return;
        }
        sdk.initPrinter();
        isBusy = true;

        sdk.setPrnText("------------------------------", 1, 1);
        //sdk.setPrnText("\n", 1, 1);
        sdk.setPrnText("R.U.T " + abonadoIdentificacion, 1, 1);
        sdk.setPrnText(tipoDTE, 1, 1);
        sdk.setPrnText("N° " + folio, 1, 1);
        sdk.setPrnText("------------------------------", 1, 1);
        sdk.startPrint();
        if (logo != null) {
            Bitmap bmlogo;
            bmlogo = BitmapFactory.decodeByteArray(logo, 0, logo.length);
            sdk.setPrnBitmap(bmlogo, 1);
        }
        sdk.setPrnText(razonSocialEmisor, 1, 0);
        sdk.setPrnText("Casa Matriz: " + direccionCasaMatriz + ", " + comunaCasaMatriz + ", " + ciudadCasaMatriz, 1, 0);
        sdk.setPrnText("Sucursal: " + direccionSucursal + "," + comunaSucursal + ", " + ciudadSucursal, 1, 0);
        sdk.setPrnText("Código Sucursal: " + codigoSucursal, 1, 0);
        sdk.setPrnText("Giro: " + giro, 1, 0);
        sdk.setPrnText("------------------------------", 1, 1);
        sdk.setPrnText("Receptor: " + nombreReceptor, 1, 1);
        sdk.setPrnText("RUT:       " + rutReceptor, 1, 0);
        sdk.setPrnText("Fecha Emisión: " + fechaEmision, 1, 0);
        sdk.setPrnText("------------------------------", 1, 1);
        sdk.setPrnText("Descripc    Cant    Precio", 1, 0);
        sdk.setPrnText(venta + "       1      " + monto, 1, 0);
        sdk.setPrnText("------------------------------", 1, 1);
        sdk.setPrnText("Total: " + monto, 1, 2);
        sdk.setPrnText("------------------------------", 1, 1);
        sdk.startPrint();

        sdk.setPrnColorBitmap(ted, 0);
        sdk.feedPaper(15, 0);

        isBusy = false;

    }
}
