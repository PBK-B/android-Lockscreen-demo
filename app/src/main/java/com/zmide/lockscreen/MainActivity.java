package com.zmide.lockscreen;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private DevicePolicyManager devicePolicyManager;
    Button mBtnOnDevcontrol, mBtnUnDevcontrol, mLockscreen;
    String TAG = "天真";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

        viewInit();
        loadViewState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // 刷新页面授权状态
        loadViewState();
    }

    private void viewInit() {

        mBtnOnDevcontrol = findViewById(R.id.main_btn_up_devcontrol);
        mBtnUnDevcontrol = findViewById(R.id.main_btn_un_devcontrol);

        mLockscreen = findViewById(R.id.main_btn_lockscreen);


        mBtnOnDevcontrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 注册设备管理点击事件
                OnDevControl();
            }
        });

        mBtnUnDevcontrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 注销设备管理点击事件
                UnDevControl();

                // 刷新页面状态
                loadViewState();
            }
        });

        mLockscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 锁屏测试点击事件
                try {
                    // 若未注册设备管理器，跳转到配置界面
                    devicePolicyManager.lockNow();
                    // finish();
                    return;
                } catch (Exception e) {
                    toast("请先激活设备管理器！");
                }
            }
        });


    }

    private void setViewState(boolean is) {
        if (is) {
            mBtnOnDevcontrol.setVisibility(View.GONE);
            mBtnUnDevcontrol.setVisibility(View.VISIBLE);
        } else {
            mBtnOnDevcontrol.setVisibility(View.VISIBLE);
            mBtnUnDevcontrol.setVisibility(View.GONE);
        }
    }

    private void loadViewState() {
        // 若已注册设备管理器，显示取消设备管理器页面
        if (devicePolicyManager.isAdminActive(new ComponentName(this, DeviceMangerBc.class))) {
            setViewState(true);
        } else {
            setViewState(false);
        }
    }

    // 注销设备管理点击事件
    private void UnDevControl() {
        devicePolicyManager.removeActiveAdmin(new ComponentName(getApplication(), DeviceMangerBc.class));
    }

    // 注册设备管理点击事件
    private void OnDevControl() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, new ComponentName(getApplication(), DeviceMangerBc.class));
        startActivityForResult(intent, 1);
    }

    private void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


}