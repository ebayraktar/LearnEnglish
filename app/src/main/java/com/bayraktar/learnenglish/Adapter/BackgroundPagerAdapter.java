package com.bayraktar.learnenglish.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bayraktar.learnenglish.R;

public class BackgroundPagerAdapter extends PagerAdapter {
    private Activity activity;
    private int[] images;

    public BackgroundPagerAdapter(Activity activity, int[] images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View itemView = inflater.inflate(R.layout.item_background_pager, container, false);

        ImageView image = itemView.findViewById(R.id.ivBackground);
        image.setImageResource(images[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
