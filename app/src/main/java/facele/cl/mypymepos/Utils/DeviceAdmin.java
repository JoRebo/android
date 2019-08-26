package facele.cl.mypymepos.Utils;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class DeviceAdmin extends DeviceAdminReceiver {

    void showToast(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showToast(context, "Enabled");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "Disabled";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showToast(context, "Disabled");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        showToast(context, "Password Change");
    }

    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(),
                DeviceAdmin.class);
    }

}