package facele.cl.mypymepos.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import facele.cl.mypymepos.Conexiones.ApiCliente;
import facele.cl.mypymepos.Modelo.RestDatafonoObtener;
import facele.cl.mypymepos.Modelo.Usuario;
import facele.cl.mypymepos.R;
import facele.cl.mypymepos.Utils.GeneradorXML;
import facele.cl.mypymepos.Utils.Impresora;
import facele.cl.mypymepos.Utils.SDKAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VincularDatafono extends AppCompatActivity {
    private SDKAPI sdkapi;
    private Leds leds;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vincular_datafono);
        Usuario usuario = null;

        Impresora impresora = new Impresora(getApplicationContext());
        Drawable drawableTED = getResources().getDrawable(R.drawable.logo_facele_2);
        Bitmap bitmap = ((BitmapDrawable) drawableTED).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] logo = stream.toByteArray();
        GeneradorXML generadorXML = new GeneradorXML();
        Bitmap ted = generadorXML.getTedFromBytes("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iSVNPLTg4NTktMSI/Pgo8RFRFIHZlcnNpb249IjEuMCIgeG1sbnM9Imh0dHA6Ly93d3cuc2lpLmNsL1NpaUR0ZSI+CiAgPERvY3VtZW50byBJRD0iQkUtMzktNDIxLTE4MDk0OTA4LTkiPgogICAgPEVuY2FiZXphZG8+CiAgICAgIDxJZERvYz4KICAgICAgICA8VGlwb0RURT4zOTwvVGlwb0RURT4KICAgICAgICA8Rm9saW8+NDIxPC9Gb2xpbz4KICAgICAgICA8RmNoRW1pcz4yMDE5LTA4LTE0PC9GY2hFbWlzPgogICAgICAgIDxJbmRTZXJ2aWNpbz4zPC9JbmRTZXJ2aWNpbz4KICAgICAgPC9JZERvYz4KICAgICAgPEVtaXNvcj4KICAgICAgICA8UlVURW1pc29yPjE4MDk0OTA4LTk8L1JVVEVtaXNvcj4KICAgICAgICA8UnpuU29jRW1pc29yPkNsYXVkaW8gSGlkYWxnbzwvUnpuU29jRW1pc29yPgogICAgICAgIDxHaXJvRW1pc29yPkdJUk8gVEVTVDwvR2lyb0VtaXNvcj4KICAgICAgICA8Q2RnU0lJU3VjdXI+MTIzNDU2Nzg5PC9DZGdTSUlTdWN1cj4KICAgICAgICA8RGlyT3JpZ2VuPmRpcmVjY2lvbjwvRGlyT3JpZ2VuPgogICAgICAgIDxDbW5hT3JpZ2VuPmNvbXVuYTwvQ21uYU9yaWdlbj4KICAgICAgICA8Q2l1ZGFkT3JpZ2VuPmNpdWRhZDwvQ2l1ZGFkT3JpZ2VuPgogICAgICA8L0VtaXNvcj4KICAgICAgPFJlY2VwdG9yPgogICAgICAgIDxSVVRSZWNlcD42NjY2NjY2Ni02PC9SVVRSZWNlcD4KICAgICAgICA8UnpuU29jUmVjZXA+Q0xJRU5URSBCT0xFVEE8L1J6blNvY1JlY2VwPgogICAgICA8L1JlY2VwdG9yPgogICAgICA8VG90YWxlcz4KICAgICAgICA8TW50VG90YWw+ODg4ODwvTW50VG90YWw+CiAgICAgIDwvVG90YWxlcz4KICAgIDwvRW5jYWJlemFkbz4KICAgIDxEZXRhbGxlPgogICAgICA8TnJvTGluRGV0PjE8L05yb0xpbkRldD4KICAgICAgPE5tYkl0ZW0+OC44ODg8L05tYkl0ZW0+CiAgICAgIDxRdHlJdGVtPjE8L1F0eUl0ZW0+CiAgICAgIDxQcmNJdGVtPjg4ODg8L1ByY0l0ZW0+CiAgICAgIDxNb250b0l0ZW0+ODg4ODwvTW9udG9JdGVtPgogICAgPC9EZXRhbGxlPgogICAgPFRFRCB2ZXJzaW9uPSIxLjAiPgogICAgICA8REQ+CiAgICAgICAgPFJFPjE4MDk0OTA4LTk8L1JFPgogICAgICAgIDxURD4zOTwvVEQ+CiAgICAgICAgPEY+NDIxPC9GPgogICAgICAgIDxGRT4yMDE5LTA4LTE0PC9GRT4KICAgICAgICA8UlI+NjY2NjY2NjYtNjwvUlI+CiAgICAgICAgPFJTUj5DTElFTlRFIEJPTEVUQTwvUlNSPgogICAgICAgIDxNTlQ+ODg4ODwvTU5UPgogICAgICAgIDxJVDE+OC44ODg8L0lUMT4KICAgICAgICA8Q0FGIHZlcnNpb249IjEuMCI+CiAgICAgICAgICA8REE+CiAgICAgICAgICAgIDxSRT4xODA5NDkwOC05PC9SRT4KICAgICAgICAgICAgPFJTPkNMQVVESU8gSElEQUxHTzwvUlM+CiAgICAgICAgICAgIDxURD4zOTwvVEQ+CiAgICAgICAgICAgIDxSTkc+CiAgICAgICAgICAgICAgPEQ+MTwvRD4KICAgICAgICAgICAgICA8SD4xMDAwMDAwMDwvSD4KICAgICAgICAgICAgPC9STkc+CiAgICAgICAgICAgIDxGQT4yMDE5LTA4LTExPC9GQT4KICAgICAgICAgICAgPFJTQVBLPgogICAgICAgICAgICAgIDxNPnlweGtsejBKaFZaVExmUjZzS1huYlZTdm1UWTU5M0M4NUZQell6aVBaT2FFMlh6Qk9CU2U5bllDb3lIZGErcERyeUIzdkRQbENZZWhEUmVEblpwRnBRPT08L00+CiAgICAgICAgICAgICAgPEU+QXc9PTwvRT4KICAgICAgICAgICAgPC9SU0FQSz4KICAgICAgICAgICAgPElESz4xMDA8L0lESz4KICAgICAgICAgIDwvREE+CiAgICAgICAgICA8RlJNQSBhbGdvcml0bW89IlNIQTF3aXRoUlNBIj5kOW1Qb2RqQ3FxU0pMZW5FWmxjU2RCWElJUXRaQ1k3alEzbG5kd29kWG0zT0xyTkJ4bVJDUisvU1Y5cXF5bktHbUl6b2pZQS9HMkplMGZrN25SeUgrQT09PC9GUk1BPgogICAgICAgIDwvQ0FGPgogICAgICAgIDxUU1RFRD4yMDE5LTA4LTE0VDE1OjUyOjM1PC9UU1RFRD4KICAgICAgPC9ERD4KICAgICAgPEZSTVQgYWxnb3JpdG1vPSJTSEExd2l0aFJTQSI+QWVSMTdFR0w3TnJtaFZqNlBtWjMzc1RHNkNuajVZVGh5Z3VHRnA2cTd3VjBKdWl4cGdIb1g1RWpwTXJETElrbytBOE1za3NlcHV3MG9JKzhiaWVLcmc9PTwvRlJNVD4KICAgIDwvVEVEPgogICAgPFRtc3RGaXJtYT4yMDE5LTA4LTE0VDE1OjUyOjM1LjU3N1o8L1Rtc3RGaXJtYT4KICA8L0RvY3VtZW50bz4KPC9EVEU+");
        impresora.imprimir("76.001.565-2", "Facele SPA", "Boleta Electrónica", "123", "Pedro de Valdivia Nte. 129 of. 302", "Providencia", "Santiago", "Luis Thayer Ojeda 0130 of. 203", "Providencia", "Santiago", "19201293", "ACTIVIDADES DE CONSULTORÍA DE INFORMÁTICA Y DE GESTIÓN DE INSTALACIONES INFORMÁTICAS", "26/08/2019 15:20", "17.726.389-3", "José Rebolledo S.", "$599.990", "$559.990", logo, ted);

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
