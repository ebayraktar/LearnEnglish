package com.bayraktar.learnenglish.ui.oxford;

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

import com.bayraktar.learnenglish.Models.Oxford.OxfordModel;
import com.bayraktar.learnenglish.R;

public class OxfordFragment extends Fragment {

    private OxfordViewModel mViewModel;
    TextView tv_random;
    Button btn_repeat;

    public static OxfordFragment newInstance() {
        return new OxfordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oxford, container, false);
        tv_random = view.findViewById(R.id.tv_random);
        btn_repeat = view.findViewById(R.id.btn_repeat);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OxfordViewModel.class);
        mViewModel.getRandomWords("Test").observe(getViewLifecycleOwner(), new Observer<OxfordModel>() {
            @Override
            public void onChanged(OxfordModel oxfordModel) {
                if (oxfordModel == null) {
                    tv_random.setText("NULL!");
                } else {
                    tv_random.setText(oxfordModel.results.get(0).word);
                }
            }
        });
        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getRandomWords("Button");
            }
        });
    }

}