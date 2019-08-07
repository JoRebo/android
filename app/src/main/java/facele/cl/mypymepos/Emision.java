package facele.cl.mypymepos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Objects;

public class Emision extends AppCompatActivity {

    TextView operacion;
    TextView resultado;
    String mensajeError = "Syntax error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        dialog.setOnShowListener(arg0 -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark)));

        dialog.show();
    }

    public void emitir(View view) {
        calcularResultado(true);
        if (resultado.getText().toString().equals(mensajeError)) {
            dialogConstructor("Error", "No se puede emitir la boleta ya que existen errores de cálculo");
        } else if (operacion.getText().toString().equals("0")) {
            dialogConstructor("Error", "Debe ingresar una operación");
        }
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
            Log.d("ecuacion", ecuacion);

            String[] separadoPorEspacios = ecuacion.split(" ");
            String ultimoEspacio = separadoPorEspacios[separadoPorEspacios.length - 1];
            Log.d("ultimoEspacio", ultimoEspacio);

            String[] separadoPorParentesis = ultimoEspacio.split("[(]");
            Log.d(separadoPorParentesis.length + "", "aa");
            if (separadoPorParentesis.length != 0) {
                String ultimoParentesis = separadoPorParentesis[separadoPorParentesis.length - 1];
                Log.d("ultimoParentesis", ultimoParentesis);
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
}


