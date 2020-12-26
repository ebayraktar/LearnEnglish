package com.bayraktar.learnenglish.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.bayraktar.learnenglish.R;
import com.bumptech.glide.Glide;
import com.huxq17.swipecardsview.BaseCardAdapter;

import java.util.ArrayList;
import java.util.List;

public class SlideCardAdapter extends BaseCardAdapter<Word> {

    private List<Word> modelList;

    public SlideCardAdapter() {
        modelList = new ArrayList<>();
    }

    public void setModelList(List<Word> modelList) {
        this.modelList = modelList;
    }

    @Override
    public int getVisibleCardCount() {
        return modelList.size();
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.item_slide_card;
    }

    @Override
    public void onBindData(int position, View cardView) {
        if (modelList == null || modelList.size() == 0) {
            return;
        }

        Word model = modelList.get(position);
        ImageView imageView = (ImageView) cardView.findViewById(R.id.imageViewCardItem);
        TextView textView = (TextView) cardView.findViewById(R.id.textView);

        textView.setText(model.getLanguage().get(0).getCode());

        Glide.with(cardView)
                .load(model.getImages().get(0))
                .fitCenter()
                .placeholder(R.drawable.ic_baseline_terrain_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(imageView);

    }
}
