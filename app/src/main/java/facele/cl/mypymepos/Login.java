package facele.cl.mypymepos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.boton_ingresar);

        login.setOnClickListener(v -> {
            Intent intent = new Intent(this, NavDrawer.class);
            startActivity(intent);
            this.finish();
        });
    }
}
