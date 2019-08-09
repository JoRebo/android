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
    }

    public void imprimir(String nombreEmisor, String rutEmisor, String tipoDTE, String folio, String rutReceptor, String nombreReceptor, String venta, String monto, byte[] logo, byte[] ted) {
        if (isBusy) {
            return;
        }

        isBusy = true;
        if (logo != null) {
            Bitmap bmlogo;
            bmlogo = BitmapFactory.decodeByteArray(logo, 0, logo.length);
            sdk.setPrnBitmap(bmlogo, 1);
        } else {
            sdk.setPrnText("------------------------------", 1, 1);
        }
        sdk.setPrnText("\n", 1, 1);
        sdk.setPrnText(nombreEmisor, 3, 1);
        sdk.setPrnText("Rut:       " + rutEmisor, 1, 0);
        sdk.setPrnText(tipoDTE, 1, 0);
        sdk.setPrnText("Folio:     " + folio, 1, 0);
        sdk.setPrnText("------------------------------", 1, 1);
        sdk.setPrnText("Receptor", 1, 1);
        sdk.setPrnText("Rut:       " + rutReceptor, 1, 0);
        sdk.setPrnText("R. social: " + nombreReceptor, 1, 0);
        sdk.setPrnText("------------------------------", 1, 1);
        sdk.setPrnText("Descripc    Cant    Precio", 1, 0);
        sdk.setPrnText(venta + "       1      " + monto, 1, 0);
        sdk.setPrnText("------------------------------", 1, 1);
        sdk.setPrnText("Total: " + monto, 1, 2);
        sdk.setPrnText("------------------------------", 1, 1);
        sdk.startPrint();

        Bitmap bmted;
        bmted = BitmapFactory.decodeByteArray(ted, 0, ted.length);
        sdk.setPrnBitmap(bmted, 1);
        sdk.feedPaper(20, 0);

        isBusy = false;

    }
}
