package com.bayraktar.learnenglish.ui.random;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bayraktar.learnenglish.R;

public class RandomFragment extends Fragment {

    private RandomViewModel mViewModel;
    TextView tv_random;
    Button btn_repeat;

    public static RandomFragment newInstance() {
        return new RandomFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random, container, false);
        tv_random = view.findViewById(R.id.tv_random);
        btn_repeat = view.findViewById(R.id.btn_repeat);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RandomViewModel.class);

        mViewModel.getRandomWords(5).observe(getViewLifecycleOwner(), new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                Log.d("TAG", "onChanged: ");
                tv_random.setText("");
                if (strings != null) {
                    for (String word : strings) {
                        tv_random.setText(tv_random.getText() + word + "\r\n");
                    }
                } else {
                    tv_random.setText("Kelime bulunamadı");
                }
            }
        });
        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getRandomWords(3);
            }
        });
    }

}