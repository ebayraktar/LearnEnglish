package com.bayraktar.learnenglish.ui.firebase;

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

import com.bayraktar.learnenglish.Models.Firebase.Word;
import com.bayraktar.learnenglish.R;

public class FirebaseFragment extends Fragment {

    private FirebaseViewModel mViewModel;
    TextView tv_random;
    Button btn_repeat;

    public static FirebaseFragment newInstance() {
        return new FirebaseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_firebase, container, false);
        tv_random = view.findViewById(R.id.tv_random);
        btn_repeat = view.findViewById(R.id.btn_repeat);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FirebaseViewModel.class);
        mViewModel.getWord(0).observe(getViewLifecycleOwner(), new Observer<Word>() {
            @Override
            public void onChanged(Word word) {
                if (word != null && word.getLanguage() != null) {
                    tv_random.setText(word.getLanguage().get(0).getCode());
                } else {
                    tv_random.setText("NULL");
                }
            }
        });
        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getWord(32);
            }
        });
    }
}