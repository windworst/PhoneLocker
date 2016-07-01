package phonelocker;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class LockScreenUtil {
    public static void activeManager(Context context, String description) {
        context.startActivity(new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                .putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(context, LockReceiver.class))
                .putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, description));
    }

    public static boolean isAdminActive(Context context) {
        return ((DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE)).isAdminActive(new ComponentName(context, LockReceiver.class));
    }

    public static void lockNow(Context context) {
        try {
            ((DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE)).lockNow();
        } catch (Exception e) {
            //if lock failed
        }
    }
}
