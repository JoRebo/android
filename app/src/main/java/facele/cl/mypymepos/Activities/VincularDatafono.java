package facele.cl.mypymepos.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import facele.cl.mypymepos.Conexiones.ApiCliente;
import facele.cl.mypymepos.Modelo.RestDatafonoObtener;
import facele.cl.mypymepos.Modelo.Usuario;
import facele.cl.mypymepos.R;
import facele.cl.mypymepos.Utils.SDKAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VincularDatafono extends AppCompatActivity {
    private SDKAPI sdkapi;
    private Leds leds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vincular_datafono);
        Usuario usuario = null;
        leds = new Leds();

        Button boton = findViewById(R.id.consultarDatafono);
        EditText datafonoId = findViewById(R.id.datafonoIdentificacion);
        boton.setOnClickListener(v -> {
            datafonoId.setError(null);
            if (datafonoId.getText().toString().equals("")) {
                Log.d("error", "si");
                datafonoId.setError("Requerido");
                return;
            }
            final ProgressDialog loading = ProgressDialog.show(this, "Cargando", "Espere...", false, false);
            Call<RestDatafonoObtener> call = ApiCliente.get().getDatafono(datafonoId.getText().toString(), 1234);
            call.enqueue(new Callback<RestDatafonoObtener>() {

                @Override
                public void onResponse(@NonNull Call<RestDatafonoObtener> call, @NonNull Response<RestDatafonoObtener> response) {
                    loading.dismiss();
                    leds.shutdown();

                    if (response.isSuccessful()) {
                        RestDatafonoObtener datafono = response.body();
                        actualizarDatafono(datafono);

                    } else {
                        Log.d("error", "1");
                        try {
                            JSONObject jObj = new JSONObject(response.errorBody().string());
                            if (jObj.has("Message"))
                                dialogConstructor("Error", jObj.getString("Message"));
                            if (jObj.has("Error"))
                                dialogConstructor("Error", jObj.getString("Error"));
                        } catch (Exception e) {
                            Toast.makeText(VincularDatafono.this, "Error de conexion al servidor", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RestDatafonoObtener> call, @NonNull Throwable t) {
                    //TODO error de conexion
                    leds.shutdown();
                    loading.dismiss();
                    Toast.makeText(VincularDatafono.this, "Error de conexion al servidor", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        });
    }

    public void actualizarDatafono(RestDatafonoObtener datafono) {
        //TODO
    }

    public void dialogConstructor(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Ok", (dialog, id) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setOnShowListener(arg0 -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark)));

        dialog.show();
    }

    public class Leds implements Runnable {
        private volatile boolean shutdown;

        public void run() {
            while (!shutdown) {
                try {
                    sdkapi.setLEDStatus((byte) 1, (byte) 1, (byte) 1, (byte) 200);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    sdkapi.setLEDStatus((byte) 1, (byte) 0, (byte) 1, (byte) 200);

                    sdkapi.setLEDStatus((byte) 2, (byte) 1, (byte) 1, (byte) 200);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    sdkapi.setLEDStatus((byte) 2, (byte) 0, (byte) 1, (byte) 200);

                    sdkapi.setLEDStatus((byte) 4, (byte) 1, (byte) 1, (byte) 200);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    sdkapi.setLEDStatus((byte) 4, (byte) 0, (byte) 1, (byte) 200);

                    sdkapi.setLEDStatus((byte) 8, (byte) 1, (byte) 1, (byte) 200);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    sdkapi.setLEDStatus((byte) 1, (byte) 0, (byte) 1, (byte) 200);
                    sdkapi.setLEDStatus((byte) 2, (byte) 0, (byte) 1, (byte) 200);
                    sdkapi.setLEDStatus((byte) 4, (byte) 0, (byte) 1, (byte) 200);
                    sdkapi.setLEDStatus((byte) 8, (byte) 0, (byte) 1, (byte) 200);
                } catch (NullPointerException e) {
                    Log.e("SDKAPI", "Dispositivo no soportado");
                }
            }
        }

        public void shutdown() {
            shutdown = true;
        }
    }
}
