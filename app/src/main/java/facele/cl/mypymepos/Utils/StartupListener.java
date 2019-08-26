package facele.cl.mypymepos.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import facele.cl.mypymepos.Activities.SplashScreen;

public class StartupListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, SplashScreen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}