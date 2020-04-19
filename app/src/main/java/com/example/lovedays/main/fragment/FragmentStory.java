package com.example.lovedays.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lovedays.R;

public class FragmentStory extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static FragmentStory newInstance(int page, String title) {
        FragmentStory fragmentStory = new FragmentStory();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentStory.setArguments(args);
        return fragmentStory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);
//        TextView tvLabel = view.findViewById(R.id.txtTitle3);
//        tvLabel.setText(page + " -- " + title);
        return view;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt");
        title = getArguments().getString("someTitle");
    }
}
