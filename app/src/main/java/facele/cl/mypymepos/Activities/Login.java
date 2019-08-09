package facele.cl.mypymepos.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import facele.cl.mypymepos.R;

public class Login extends AppCompatActivity {
    public static Context baseContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseContext = getBaseContext();

        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.boton_ingresar);

        login.setOnClickListener(v -> {
            Intent intent = new Intent(this, NavDrawer.class);
            startActivity(intent);
            this.finish();
        });
    }
}
