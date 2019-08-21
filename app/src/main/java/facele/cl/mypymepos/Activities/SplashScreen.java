package facele.cl.mypymepos.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import facele.cl.mypymepos.R;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashScreen.this, Login.class);
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();
        }, SPLASH_DISPLAY_LENGTH);

    }

    @Override
    public void onBackPressed() {
    }
}
