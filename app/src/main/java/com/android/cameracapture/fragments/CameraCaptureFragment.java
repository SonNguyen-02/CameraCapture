package com.android.cameracapture.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.cameracapture.R;
import com.android.cameracapture.utils.BarsUtils;
import com.mct.base.ui.BaseFragment;
import com.mct.base.ui.transition.FragmentTransitionFactory;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Audio;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Grid;
import com.otaliastudios.cameraview.gesture.Gesture;
import com.otaliastudios.cameraview.gesture.GestureAction;
import com.otaliastudios.cameraview.markers.DefaultAutoFocusMarker;
import com.otaliastudios.cameraview.size.AspectRatio;
import com.otaliastudios.cameraview.size.SizeSelector;
import com.otaliastudios.cameraview.size.SizeSelectors;

import java.util.ArrayList;
import java.util.List;

public class CameraCaptureFragment extends BaseFragment implements View.OnClickListener {

    private final List<Bitmap> images = new ArrayList<>();

    private boolean mPlaySound;
    private boolean mShowFlash;
    private boolean mShowGrid;

    private ImageButton btnClose, btnSound, btnFlash, btnGrid;
    private CameraView camera;
    private ProgressBar pbTakePhoto;
    private ImageButton btnTakePhoto;
    private ViewGroup framePreviewRecent;
    private ImageView imgPreviewRecent;
    private TextView tvPreviewRecentCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera_capture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            BarsUtils.setStatusBarColor(requireActivity(), 0);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                extraTransaction().popFragment();
                break;
            case R.id.btn_sound:
                mPlaySound = !mPlaySound;
                invalidateToolbar();
                break;
            case R.id.btn_flash:
                mShowFlash = !mShowFlash;
                invalidateToolbar();
                break;
            case R.id.btn_grid:
                mShowGrid = !mShowGrid;
                invalidateToolbar();
                break;
            case R.id.btn_take_photo:
                camera.takePicture();
                break;
            case R.id.img_preview_recent:
                if (!images.isEmpty()) {
                    int[] pos = new int[2];
                    v.getLocationInWindow(pos);
                    Point circularRevealPosition = new Point(
                            pos[0] + v.getWidth() / 2,
                            pos[1] + v.getHeight() / 2
                    );
                    extraTransaction().addFragmentToStack(
                            PreviewImageFragment.newInstance(new ArrayList<>(images), circularRevealPosition),
                            FragmentTransitionFactory.createCircularRevealTransition()
                    );
                    images.clear();
                    invalidateBottomBar();
                }
                break;
        }
    }

    private void initView(@NonNull View view) {
        btnClose = view.findViewById(R.id.btn_close);
        btnSound = view.findViewById(R.id.btn_sound);
        btnFlash = view.findViewById(R.id.btn_flash);
        btnGrid = view.findViewById(R.id.btn_grid);
        camera = view.findViewById(R.id.camera);
        pbTakePhoto = view.findViewById(R.id.pb_take_photo);
        btnTakePhoto = view.findViewById(R.id.btn_take_photo);
        framePreviewRecent = view.findViewById(R.id.frame_preview_recent);
        imgPreviewRecent = view.findViewById(R.id.img_preview_recent);
        tvPreviewRecentCount = view.findViewById(R.id.tv_preview_recent_count);
    }

    private void initData() {
        camera.setLifecycleOwner(getViewLifecycleOwner());
        camera.setAudio(Audio.OFF);
        camera.setFacing(Facing.BACK);
        camera.setUseDeviceOrientation(true);
        camera.setAutoFocusMarker(new DefaultAutoFocusMarker());
        camera.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS);
        camera.mapGesture(Gesture.PINCH, GestureAction.ZOOM);
        camera.mapGesture(Gesture.SCROLL_HORIZONTAL, GestureAction.FILTER_CONTROL_1);

        SizeSelector max = SizeSelectors.and(
                SizeSelectors.maxWidth(1440),
                SizeSelectors.maxHeight(1920)
        );
        camera.setPictureSize(SizeSelectors.or(
                SizeSelectors.and(SizeSelectors.aspectRatio(AspectRatio.of(9, 16), 0), max),
                SizeSelectors.and(SizeSelectors.aspectRatio(AspectRatio.of(3, 4), 0), max),
                SizeSelectors.smallest()
        ));
        camera.addCameraListener(new CameraListener() {

            @Override
            public void onPictureShutter() {
                pbTakePhoto.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                result.toBitmap(1440, 1920, bitmap -> {
                    pbTakePhoto.setVisibility(View.GONE);
                    if (isHidden()) {
                        return;
                    }
                    if (bitmap == null) {
                        Toast.makeText(requireContext(), "Picture limit!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    images.add(bitmap);
                    invalidateBottomBar();
                });
            }

            @Override
            public void onCameraError(@NonNull CameraException exception) {
                pbTakePhoto.setVisibility(View.GONE);

            }
        });
        btnClose.setOnClickListener(this);
        btnSound.setOnClickListener(this);
        btnFlash.setOnClickListener(this);
        btnGrid.setOnClickListener(this);
        btnTakePhoto.setOnClickListener(this);
        imgPreviewRecent.setOnClickListener(this);
        invalidateToolbar();
    }

    private void invalidateToolbar() {
        btnSound.setImageResource(mPlaySound ? R.drawable.ic_sound : R.drawable.ic_sound_off);
        btnFlash.setImageResource(mShowFlash ? R.drawable.ic_flash_on : R.drawable.ic_flash_off);
        btnGrid.setImageResource(mShowGrid ? R.drawable.ic_grid : R.drawable.ic_grid_off);

        camera.setPlaySounds(mPlaySound);
        camera.setFlash(mShowFlash ? Flash.TORCH : Flash.OFF);
        camera.setGrid(mShowGrid ? Grid.DRAW_3X3 : Grid.OFF);
    }

    private void invalidateBottomBar() {
        if (images.isEmpty()) {
            framePreviewRecent.setVisibility(View.GONE);
            imgPreviewRecent.setImageBitmap(null);
            tvPreviewRecentCount.setText(String.valueOf(0));
        } else {
            framePreviewRecent.setVisibility(View.VISIBLE);
            imgPreviewRecent.setImageBitmap(images.get(images.size() - 1));
            tvPreviewRecentCount.setText(String.valueOf(images.size()));
        }
    }


}
