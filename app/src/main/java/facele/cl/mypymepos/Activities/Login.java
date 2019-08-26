package facele.cl.mypymepos.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

import facele.cl.mypymepos.Modelo.Usuario;
import facele.cl.mypymepos.R;

public class Login extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static Context baseContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseContext = getBaseContext();

        SharedPreferences mPrefs =
                getSharedPreferences("Storage", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.boton_ingresar);
        EditText user = findViewById(R.id.texto_usuario);
        EditText pass = findViewById(R.id.texto_contraseña);
        CheckBox remember = findViewById(R.id.chb_recuerdame);

        user.setText(mPrefs.getString("user", ""));
        pass.setText(mPrefs.getString("pass", ""));
        if (!user.getText().toString().equals(""))
            remember.setChecked(true);

        login.setOnClickListener(v -> {
            boolean error = false;
            user.setError(null);
            pass.setError(null);
            if (user.getText().toString().length() == 0) {
                user.setError("Requerido");
                error = true;
            }
            if (!user.getText().toString().equals("Facele SPA")) {
                user.setError("Usuario no existe ");
                error = true;
            }
            if (pass.getText().toString().length() == 0) {
                pass.setError("Requerido");
                error = true;
            }
            if (error) {
                return;
            }

            // If remember
            if (remember.isChecked()) {
                prefsEditor.putString("user", user.getText().toString());
                prefsEditor.putString("pass", pass.getText().toString());
                prefsEditor.commit();
            } else {
                prefsEditor.putString("user", "");
                prefsEditor.putString("pass", "");
            }
            // ./If remember

            int datafonoIdentificacion = mPrefs.getInt("datafonoIdentificacion", 0);

            if (datafonoIdentificacion == 0) {
                Intent intent = new Intent(this, VincularDatafono.class);
                startActivity(intent);
                this.finish();
            } else {
                // TODO corregir cuando haya autentication
                Usuario usuario = new Usuario();
                usuario.setAbonadoIdentificacion("18094908-9");
                usuario.setNombre(user.getText().toString());

                Drawable drawableTED = getResources().getDrawable(R.drawable.logo_facele_2);
                Bitmap bitmap = ((BitmapDrawable) drawableTED).getBitmap();
                usuario.setDatafonoIdentificacion(datafonoIdentificacion);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] logo = stream.toByteArray();
                usuario.setLogo(logo);

                usuario.setRut("76.001.565-2");
                usuario.setVendedorEmail("testemail2@dominio.com");
                Gson gson = new Gson();
                String json = gson.toJson(usuario);
                prefsEditor.putString("usuario", json);
                prefsEditor.commit();
                Intent intent = new Intent(this, NavDrawer.class);
                startActivity(intent);
                this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
