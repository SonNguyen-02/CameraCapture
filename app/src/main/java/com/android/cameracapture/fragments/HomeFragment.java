package com.android.cameracapture.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.android.cameracapture.R;
import com.mct.base.ui.BaseFragment;
import com.mct.base.ui.transition.FragmentTransitionFactory;

public class HomeFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start_capture).setOnClickListener(v -> extraTransaction().addFragmentToStack(
                new CameraCaptureFragment(),
                FragmentTransitionFactory.createTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ));
    }
}
