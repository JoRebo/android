package facele.cl.mypymepos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import facele.cl.mypymepos.R;

public class Perfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil);

        Toolbar toolbar = findViewById(R.id.toolbarActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PERFIL");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button cambiarContrasena = findViewById(R.id.cambiarContraseña);

        cambiarContrasena.setOnClickListener(view -> {
            Intent intent = new Intent(this, CambiarContrasena.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
