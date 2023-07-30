package com.android.cameracapture.utils;

import android.content.Context;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.SparseIntArray;
import android.view.Surface;

import androidx.annotation.NonNull;

public class PictureCapturing {

    private static final String TAG = PictureCapturing.class.getSimpleName();
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private Context context;
    private boolean dispose;

    private HandlerThread backgroundThread;
    private Handler backgroundHandler;


    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;
    private ImageReader mImageReader;

    private PictureCapturing(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }


    public interface PictureCapturingListener {

        void onCaptureReady(PictureCapturing pictureCapturing);

        void onCaptureSuccess(PictureCapturing pictureCapturing, byte[] pictureData);

        void onCaptureFalse(PictureCapturing pictureCapturing);

    }
}
