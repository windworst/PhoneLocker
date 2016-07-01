package com.android.phonelocker;

import android.app.Activity;
import android.os.Bundle;

import phonelocker.PhoneLocker;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final long expiredTime = System.currentTimeMillis() + 3600 * 1000; //一个小时
        PhoneLocker.lock(this, getString(R.string.app_name), new PhoneLocker.OnLockCallback() {
            @Override
            public boolean askIfNeedLock() {
                return expiredTime >= System.currentTimeMillis();
            }
        });
    }
}
