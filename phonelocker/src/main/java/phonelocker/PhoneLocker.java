package phonelocker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;

public class PhoneLocker extends Activity {
    private static final String PERMISSION_DESCRIPTION = "description";
    private String mDescription = "";
    private static OnLockCallback sOnLockCallback;
    private OnLockCallback mOnLockCallback;
    private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LockScreenUtil.isAdminActive(context)) {
                lockScreen(context);
            }
        }
    };

    public interface OnLockCallback {
        boolean askIfNeedLock();
    }

    public static void lock(Context context, String permissionDescription, OnLockCallback callback) {
        sOnLockCallback = callback;
        context.startActivity(new Intent(context, PhoneLocker.class).putExtra(PERMISSION_DESCRIPTION, permissionDescription));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mOnLockCallback = sOnLockCallback;
        sOnLockCallback = null;
        if (null != getIntent().getExtras()) {
            mDescription = getIntent().getExtras().getString(PERMISSION_DESCRIPTION, "");
        }
        registerReceiver(mBroadCastReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadCastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LockScreenUtil.isAdminActive(this)) {
            lockScreen(this);
        } else {
            LockScreenUtil.activeManager(this, mDescription);
        }
    }

    private void lockScreen(Context context) {
        if (null == mOnLockCallback || mOnLockCallback.askIfNeedLock()) {
            LockScreenUtil.lockNow(context);
        } else {
            finish();
        }
    }
}
