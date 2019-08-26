package facele.cl.mypymepos.Activities;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import facele.cl.mypymepos.R;
import facele.cl.mypymepos.Utils.DeviceAdmin;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        enableKioskMode();

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashScreen.this, Login.class);
            SplashScreen.this.startActivity(mainIntent);
            SplashScreen.this.finish();
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void enableKioskMode() {

        ComponentName deviceAdmin = new ComponentName(this, DeviceAdmin.class);
        DevicePolicyManager mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

        // First of all, to access anything you must be device owner
        if (mDpm.isDeviceOwnerApp(getPackageName())) {

            // If not device admin, ask to become one
            if (!mDpm.isAdminActive(deviceAdmin) &&
                    mDpm.isDeviceOwnerApp(getPackageName())) {

                Log.v("ee", "Not device admin. Asking device owner to become one.");

                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "You need to be a device admin to enable kiosk mode.");

                startActivity(intent);
            }

            // Device owner and admin : enter kiosk mode
            else {
                mDpm.setLockTaskPackages(deviceAdmin, new String[]{
                        getPackageName(), /** PUT OTHER PACKAGE NAMES HERE! */
                });
                startLockTask();
            }
        }

        // Not device owner - can't access anything
        else {
            Log.v("ee", "Not device owner");
        }
    }

    @Override
    public void onBackPressed() {
    }
}
