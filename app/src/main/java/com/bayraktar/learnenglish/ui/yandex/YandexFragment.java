package com.bayraktar.learnenglish.ui.yandex;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bayraktar.learnenglish.Models.Yandex.YandexModel;
import com.bayraktar.learnenglish.R;

public class YandexFragment extends Fragment {

    private YandexViewModel mViewModel;
    TextView tv_random;
    Button btn_repeat;

    public static YandexFragment newInstance() {
        return new YandexFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yandex, container, false);
        tv_random = view.findViewById(R.id.tv_random);
        btn_repeat = view.findViewById(R.id.btn_repeat);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(YandexViewModel.class);
        mViewModel.translate("word", "en-tr").observe(getViewLifecycleOwner(), new Observer<YandexModel>() {
            @Override
            public void onChanged(YandexModel yandexModel) {
                if (yandexModel == null) {
                    tv_random.setText("NULL!");
                } else {
                    for (String word : yandexModel.text) {
                        tv_random.setText(tv_random.getText() + word + "\r\n");
                    }
                }
            }
        });

        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.translate("orange", "en-tr");
            }
        });
    }

}