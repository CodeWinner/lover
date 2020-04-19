package com.example.lovedays.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lovedays.R;

public class FragmentMain extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static FragmentMain newInstance(int page, String title) {
        FragmentMain fragmentMain = new FragmentMain();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentMain.setArguments(args);
        return fragmentMain;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
//        TextView tvLabel = view.findViewById(R.id.txtTitle);
//        Log.i("VIEW_PAGER","set : " + page + " -- " + title);
//        tvLabel.setText(title);
        return view;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt");
        title = getArguments().getString("someTitle");
        Log.i("VIEW_PAGER",page + " -- " + title);
    }
}
