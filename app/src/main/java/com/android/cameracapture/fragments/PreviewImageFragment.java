package com.android.cameracapture.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.cameracapture.R;
import com.android.cameracapture.utils.BarsUtils;
import com.google.android.material.card.MaterialCardView;
import com.mct.base.ui.BaseFragment;
import com.mct.base.ui.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class PreviewImageFragment extends BaseFragment {

    private static final String KEY_IMAGES = "KEY_IMAGES";
    private static final String KEY_CIRCULAR_REVEAL_POSITION = "KEY_circularRevealPosition";

    @NonNull
    public static PreviewImageFragment newInstance(ArrayList<Bitmap> images, Point circularRevealPosition) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_IMAGES, images);
        args.putParcelable(KEY_CIRCULAR_REVEAL_POSITION, circularRevealPosition);
        PreviewImageFragment fragment = new PreviewImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<Bitmap> images;
    private Point circularRevealPosition;

    private Toolbar toolbar;
    private RecyclerView rcvImages;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.images = requireArguments().getParcelableArrayList(KEY_IMAGES);
        this.circularRevealPosition = requireArguments().getParcelable(KEY_CIRCULAR_REVEAL_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        BarsUtils.setStatusBarColor(requireActivity(), ContextCompat.getColor(requireActivity(), R.color.colorPrimaryDark));
    }

    @NonNull
    @Override
    protected Point getCircularPosition() {
        return circularRevealPosition;
    }

    private void initView(@NonNull View view) {
        toolbar = view.findViewById(R.id.toolbar);
        rcvImages = view.findViewById(R.id.rcv_images);
    }

    private void initData() {
        toolbar.setNavigationOnClickListener(v -> extraTransaction().popFragment());

        rcvImages.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        rcvImages.setAdapter(new ImageAdapter(images));
    }

    static class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

        List<Bitmap> images;

        public ImageAdapter(List<Bitmap> images) {
            this.images = images;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            MaterialCardView cardView = new MaterialCardView(parent.getContext());
            return new ImageViewHolder(cardView);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            holder.imageView.setImageBitmap(images.get(position));
            holder.itemView.post(() -> {
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
                lp.height = (int) (holder.imageView.getWidth() * 3 / 2f);
                holder.itemView.setLayoutParams(lp);
            });
        }

        @Override
        public int getItemCount() {
            if (images != null) {
                return images.size();
            }
            return 0;
        }

        static class ImageViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;

            public ImageViewHolder(@NonNull ViewGroup itemView) {
                super(itemView);

                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                int margin = ScreenUtils.dp2px(8);
                lp.setMargins(margin, margin, margin, margin);
                itemView.setLayoutParams(lp);

                imageView = new ImageView(itemView.getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                itemView.addView(imageView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }

    }

}
