package com.android.cameracapture;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

import com.android.cameracapture.fragments.HomeFragment;
import com.android.cameracapture.utils.BarsUtils;
import com.mct.base.ui.BaseActivity;
import com.permissionx.guolindev.PermissionX;

import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContainerId() {
        return Window.ID_ANDROID_CONTENT;
    }

    @Override
    protected boolean showToastOnBackPressed() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarsUtils.setStatusBarColor(this, Color.TRANSPARENT);
        BarsUtils.setAppearanceLightStatusBars(this, false);
        BarsUtils.setAppearanceLightNavigationBars(this, false);
        BarsUtils.setNavigationBarColor(this, Color.TRANSPARENT);

        requestCameraPermission();
    }

    private void requestCameraPermission() {
        PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA)
                .onExplainRequestReason((scope, deniedList) -> scope.showRequestReasonDialog(deniedList,
                        "This app need camera permission! Please grant.",
                        "OK",
                        "Cancel"))
                .onForwardToSettings((scope, deniedList) -> scope.showForwardToSettingsDialog(deniedList,
                        "You must grant camera permission in the setting.",
                        "Go to the setting",
                        "Cancel"))
                .request(this::onPermissionResult);
    }

    private void onPermissionResult(boolean allGranted,
                                    @NonNull List<String> grantedList,
                                    @NonNull List<String> deniedList) {
        if (allGranted) {
            extraTransaction().replaceFragment(new HomeFragment());
        } else {
            finish();
        }
    }
}