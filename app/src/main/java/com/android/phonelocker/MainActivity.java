package com.android.phonelocker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import phonelocker.PhoneLocker;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final int times[] = {5, 10, 15, 30, 45, 60};
        ViewGroup buttons = (ViewGroup) findViewById(R.id.gridLayout);
        for (int i = 0, n = buttons.getChildCount(); i < n; ++i) {
            final int finalI = i;
            buttons.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmLock(times[finalI]);
                }
            });
        }
        Button button = (Button) findViewById(R.id.button);
        final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setValue(25);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10000);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmLock(numberPicker.getValue());
            }
        });
    }

    private void confirmLock(final long timeMinute) {
        if (timeMinute <= 0) {
            Toast.makeText(this, "时间值错误", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage(String.format("手机将锁定%d分钟", timeMinute))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        lock(timeMinute * 60);
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void lock(long timeSecond) {
        final long expiredTime = System.currentTimeMillis() + timeSecond * 1000;
        PhoneLocker.lock(this, getString(R.string.app_name), new PhoneLocker.OnLockCallback() {
            @Override
            public boolean askIfNeedLock() {
                return expiredTime >= System.currentTimeMillis();
            }
        });
    }
}
