package facele.cl.mypymepos.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import facele.cl.mypymepos.Modelo.Usuario;
import facele.cl.mypymepos.R;

public class Login extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static Context baseContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseContext = getBaseContext();

        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.boton_ingresar);
        EditText user = findViewById(R.id.texto_usuario);
        EditText pass = findViewById(R.id.texto_contraseña);

        login.setOnClickListener(v -> {
            user.setError("");
            pass.setError("");
            if (user.getText().toString().length() == 0)
                user.setError("Requerido");
            if (pass.getText().toString().length() == 0)
                pass.setError("Requerido");

            // TODO corregir cuando haya autentication
            Usuario usuario = new Usuario();
            usuario.setAbonadoIdentificacion("1234");
            usuario.setDatafonoIdentificacion(1234);
            usuario.setNombre("Facele SPA");
            usuario.setRut("76.001.565-2");
            usuario.setVendedorEmail("vendedor@vendedor.cl");
            SharedPreferences mPrefs =
                    getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = mPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(usuario);
            prefsEditor.putString("usuario", json);
            prefsEditor.apply();

            Intent intent = new Intent(this, NavDrawer.class);
            startActivity(intent);
            this.finish();
        });
    }
}
