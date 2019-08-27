package facele.cl.mypymepos.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.udojava.evalex.Expression;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Objects;

import facele.cl.mypymepos.Conexiones.ApiCliente;
import facele.cl.mypymepos.Modelo.RESTEmision;
import facele.cl.mypymepos.Modelo.RESTEmisionObtener;
import facele.cl.mypymepos.Modelo.Usuario;
import facele.cl.mypymepos.R;
import facele.cl.mypymepos.Utils.GeneradorXML;
import facele.cl.mypymepos.Utils.Impresora;
import facele.cl.mypymepos.Utils.SDKAPI;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Emision extends AppCompatActivity {

    private TextView operacion;
    private TextView resultado;
    private String mensajeError = "Syntax error";
    private Usuario usuario;
    private SDKAPI sdkapi;
    private Context context;
    private Leds leds;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();

        try {
            sdkapi = SDKAPI.getInstance();
        } catch (NoClassDefFoundError e) {
            Log.e("SDKAPI", "Dispositivo no soportado");
        }

        SharedPreferences mPrefs = getSharedPreferences("Storage", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("usuario", "failed");
        Log.d("json", json);
        usuario = gson.fromJson(json, Usuario.class);
        setContentView(R.layout.activity_emision);

        Toolbar toolbar = findViewById(R.id.toolbarActivity);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("EMISIÓN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        resultado = findViewById(R.id.resultado);

        operacion = findViewById(R.id.operacion);
        operacion.setText("0");
        operacion.setMovementMethod(new ScrollingMovementMethod());

        operacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                operacion.removeTextChangedListener(this);
                formatearNumeros();
                operacion.addTextChangedListener(this);
                if (!operacion.getText().toString().equals("0"))
                    calcularResultado(false);
            }
        });

        Button button = findViewById(R.id.btn_borrar);
        button.setOnClickListener(view -> borrar());
        //noinspection AndroidLintClickableViewAccessibility
        button.setOnTouchListener(new View.OnTouchListener() {

            private Handler mHandler;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 500);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;

                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    borrar();
                    mHandler.postDelayed(this, 100);
                }
            };

        });
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

    public void emitir(View view) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(40, 0, 40, 0);
        final EditText input = new EditText(Emision.this);
        input.setLayoutParams(lp);
        input.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setLines(1);
        input.setMaxLines(1);
        input.setHint("Email cliente (Opcional)");
        input.setHintTextColor(getResources().getColor(R.color.grey));
        container.addView(input);

        calcularResultado(true);
        if (resultado.getText().toString().equals(mensajeError)) {
            dialogConstructor("Error", "No se puede emitir la boleta ya que existen errores de cálculo");
            return;
        } else if (operacion.getText().toString().equals("0")) {
            dialogConstructor("Error", "Debe ingresar una operación");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Confirmar", (dialog, id) -> {
            email = input.getText().toString();
            enviarEmision();
        });
        builder.setNegativeButton("Cancelar", (dialog, id) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setTitle("Confirmar");
        dialog.setMessage("¿Está seguro de emitir una boleta por $" + resultado.getText().toString() + "?");

        dialog.setView(container);

        dialog.setOnShowListener(arg0 -> {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        });

        dialog.show();
    }


    private void enviarEmision() {
        leds = new Leds();
        Thread th = new Thread(leds);
        th.start();

        final ProgressDialog loading = ProgressDialog.show(this, "Cargando", "Espere...", false, false);

        RequestBody requestBody = RequestBody.create(new GeneradorXML().obtenerInputEmisionCrear(operacion.getText().toString(), resultado.getText().toString().replace(".", "")), MediaType.parse("text/plain"));
        Call<RESTEmision> call = ApiCliente.get().postEmitir(usuario.getAbonadoIdentificacion(), usuario.getDatafonoIdentificacion(), usuario.getVendedorEmail(), requestBody);
        call.enqueue(new Callback<RESTEmision>() {

            @Override
            public void onResponse(@NonNull Call<RESTEmision> call, @NonNull Response<RESTEmision> response) {
                loading.dismiss();

                if (response.isSuccessful()) {
                    //TODO exito
                    obtenerTed(response.body().getId(), response.body().getFolioDTE() + "");

                } else {
                    leds.shutdown();
                    Log.d("error", "1");
                    try {
                        JSONObject jObj = new JSONObject(response.errorBody().string());
                        if (jObj.has("Message"))
                            dialogConstructor("Error", jObj.getString("Message"));
                        if (jObj.has("Error"))
                            dialogConstructor("Error", jObj.getString("Error"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<RESTEmision> call, @NonNull Throwable t) {
                //TODO error de conexion
                leds.shutdown();
                loading.dismiss();
                Toast.makeText(Emision.this, "Error de conexion al servidor", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void obtenerTed(Integer id, String folio) {

        final ProgressDialog loading = ProgressDialog.show(this, "Cargando", "Espere...", false, false);

        Call<RESTEmisionObtener> call = ApiCliente.get().getEmitir(usuario.getAbonadoIdentificacion(), id);
        call.enqueue(new Callback<RESTEmisionObtener>() {

            @Override
            public void onResponse(@NonNull Call<RESTEmisionObtener> call, @NonNull Response<RESTEmisionObtener> response) {
                loading.dismiss();
                leds.shutdown();

                if (response.isSuccessful()) {
                    //TODO exito
                    GeneradorXML generadorXML = new GeneradorXML();
                    Bitmap tedbitmap = generadorXML.getTedFromBytes(response.body().getBytesXML());

                    imprimir(tedbitmap, folio);

                } else {
                    Log.d("error", "1");
                    try {
                        JSONObject jObj = new JSONObject(response.errorBody().string());
                        if (jObj.has("Message"))
                            dialogConstructor("Error", jObj.getString("Message"));
                        if (jObj.has("Error"))
                            dialogConstructor("Error", jObj.getString("Error"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<RESTEmisionObtener> call, @NonNull Throwable t) {
                //TODO error de conexion
                leds.shutdown();
                loading.dismiss();
                Toast.makeText(Emision.this, "Error de conexion al servidor", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void imprimir(Bitmap bitmap, String folio) {
        try {
            Impresora impresora = new Impresora(context);

            //TODO faltan campos!
            //impresora.imprimir(usuario.getNombre(), usuario.getRut(), "Boleta Electronica", folio, "66.666.666-6", "Cliente boleta", "Venta", "$" + resultado.getText().toString(), usuario.getLogo(), bitmap);
        } catch (NoClassDefFoundError e) {
            Log.e("SDKAPI", "Dispositivo no soportado");
        }

        Log.d("emision", "correcta");
    }

    public void borrar() {
        if (!operacion.getText().toString().equals("0")) {
            if (operacion.getText().toString().length() == 1) {
                operacion.setText("0");
                resultado.setText("");
            } else if (operacion.getText().toString().substring(operacion.length() - 1).equals("("))
                operacion.setText(operacion.getText().toString().substring(0, operacion.length() - 2));

            else if (operacion.getText().toString().substring(operacion.length() - 2).equals(") "))
                operacion.setText(operacion.getText().toString().substring(0, operacion.length() - 2));

            else if (operacion.getText().toString().substring(operacion.length() - 1).equals(" "))
                operacion.setText(operacion.getText().toString().substring(0, operacion.length() - 3));

            else
                operacion.setText(operacion.getText().toString().substring(0, operacion.length() - 1));
        }
    }

    public void vaciar(View view) {
        operacion.setText("0");
        resultado.setText("0");
    }

    @SuppressLint("SetTextI18n")
    public void mas(View view) {

        operacion.setText(operacion.getText() + " + ");
    }

    @SuppressLint("SetTextI18n")
    public void por(View view) {
        operacion.setText(operacion.getText() + " * ");
    }

    @SuppressLint("SetTextI18n")
    public void menos(View view) {
        operacion.setText(operacion.getText() + " - ");
    }

    @SuppressLint("SetTextI18n")
    public void abrirParentesis(View view) {
        if (operacion.getText().toString().equals("0"))
            operacion.setText("(");
        else
            operacion.setText(operacion.getText() + " (");

    }

    @SuppressLint("SetTextI18n")
    public void cerrarParentesis(View view) {
        String texto = operacion.getText().toString();
        int abiertos = texto.length() - texto.replace("(", "").length();
        int cerrados = texto.length() - texto.replace(")", "").length();
        if (abiertos > cerrados)
            operacion.setText(operacion.getText() + ") ");
    }

    @SuppressLint("SetTextI18n")
    public void coma(View view) {
        if (!(operacion.getText().toString().charAt(operacion.getText().toString().length() - 1) == ",".charAt(0))) {
            try {
                Integer.parseInt(operacion.getText().toString().charAt(operacion.getText().toString().length() - 1) + "");
                operacion.setText(operacion.getText() + ",");
            } catch (Exception e) {
                operacion.setText(operacion.getText() + "0,");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void uno(View view) {
        if (operacion.getText().toString().equals("0"))
            operacion.setText("1");
        else
            operacion.setText(operacion.getText() + "1");
    }

    @SuppressLint("SetTextI18n")
    public void dos(View view) {
        if (operacion.getText().toString().equals("0"))
            operacion.setText("2");
        else
            operacion.setText(operacion.getText() + "2");

    }

    @SuppressLint("SetTextI18n")
    public void tres(View view) {
        if (operacion.getText().toString().equals("0"))
            operacion.setText("3");
        else
            operacion.setText(operacion.getText() + "3");

    }

    @SuppressLint("SetTextI18n")
    public void cuatro(View view) {
        if (operacion.getText().toString().equals("0"))
            operacion.setText("4");
        else
            operacion.setText(operacion.getText() + "4");

    }

    @SuppressLint("SetTextI18n")
    public void cinco(View view) {
        if (operacion.getText().toString().equals("0"))
            operacion.setText("5");
        else
            operacion.setText(operacion.getText() + "5");

    }

    @SuppressLint("SetTextI18n")
    public void seis(View view) {
        if (operacion.getText().toString().equals("0"))
            operacion.setText("6");
        else
            operacion.setText(operacion.getText() + "6");

    }

    @SuppressLint("SetTextI18n")
    public void siete(View view) {
        if (operacion.getText().toString().equals("0"))
            operacion.setText("7");
        else
            operacion.setText(operacion.getText() + "7");

    }

    @SuppressLint("SetTextI18n")
    public void ocho(View view) {
        if (operacion.getText().toString().equals("0"))
            operacion.setText("8");
        else
            operacion.setText(operacion.getText() + "8");

    }

    @SuppressLint("SetTextI18n")
    public void nueve(View view) {
        if (operacion.getText().toString().equals("0"))
            operacion.setText("9");
        else
            operacion.setText(operacion.getText() + "9");

    }

    @SuppressLint("SetTextI18n")
    public void cero(View view) {
        if (operacion.getText().toString().equals("0"))
            operacion.setText("0");
        else
            operacion.setText(operacion.getText() + "0");

    }

    public void formatearNumeros() {
        try {
            String ecuacion = operacion.getText().toString();

            String[] separadoPorEspacios = ecuacion.split(" ");
            String ultimoEspacio = separadoPorEspacios[separadoPorEspacios.length - 1];

            String[] separadoPorParentesis = ultimoEspacio.split("[(]");
            if (separadoPorParentesis.length != 0) {
                String ultimoParentesis = separadoPorParentesis[separadoPorParentesis.length - 1];
                ultimoParentesis = formatoMiles(ultimoParentesis.replace(".", ""));
                separadoPorParentesis[separadoPorParentesis.length - 1] = ultimoParentesis;
                String joinParentesis = join("(", separadoPorParentesis);
                separadoPorEspacios[separadoPorEspacios.length - 1] = joinParentesis;
                String joinEspacios = join(" ", separadoPorEspacios);
                operacion.setText(joinEspacios);
            }

        } catch (Exception e) {
            Log.e("formatearNumeros", "No se puede formatear");
        }
    }

    public static String join(String separator, String... values) {
        StringBuilder sb = new StringBuilder(128);
        int end = 0;
        for (String s : values) {
            if (s != null) {
                sb.append(s);
                end = sb.length();
                sb.append(separator);
            }
        }
        return sb.substring(0, end);
    }

    public String formatoMiles(String resultado) {
        Long result = Long.parseLong(resultado);
        NumberFormat formato = NumberFormat.getIntegerInstance();
        String formateado = formato.format(result);
        return formateado.replace(",", ".");
    }

    public void calcularResultado(boolean verificar) {
        try {
            String ecuacion = operacion.getText().toString().replace(".", "").replace(",", ".");
            if (!verificar) {
                ecuacion = ecuacion.trim();
                boolean continuar = true;
                String ultimoCaracter = ecuacion.charAt(ecuacion.length() - 1) + "";

                while (continuar) {
                    try {
                        if (!ultimoCaracter.equals(")")) {
                            Integer.parseInt(ultimoCaracter);
                        }
                        continuar = false;
                    } catch (Exception e) {
                        ecuacion = ecuacion.substring(0, ecuacion.length() - 1);
                        ultimoCaracter = ecuacion.charAt(ecuacion.length() - 1) + "";
                    }
                }
            }
            BigDecimal result;

            Expression expression = new Expression(ecuacion);
            expression.setRoundingMode(RoundingMode.HALF_UP);
            expression.setPrecision(128);
            result = expression.eval();
            BigInteger bint = result.setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger();

            resultado.setText(formatoMiles(bint.toString()));
        } catch (Exception e) {
            resultado.setText(mensajeError);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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


