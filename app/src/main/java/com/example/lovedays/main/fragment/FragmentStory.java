package com.example.lovedays.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovedays.R;
import com.example.lovedays.main.helper.TimeLineAdapter;
import com.example.lovedays.main.units.TimeLine;

import java.util.ArrayList;
import java.util.List;

public class FragmentStory extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
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
        recyclerView = view.findViewById(R.id.TimeLineRecycle);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        List<TimeLine> timeLines = new ArrayList<>();
        TimeLine a = new TimeLine();
        a.setId("1");
        a.setDate("12/02/96");
        a.setStatus("inactive");
        timeLines.add(a);
        TimeLine b = new TimeLine();
        b.setId("1");
        b.setDate("12/02/96");
        b.setStatus("inactive");
        timeLines.add(b);
        TimeLine c = new TimeLine();
        c.setId("1");
        c.setDate("12/02/96");
        c.setStatus("active");
        timeLines.add(c);
        TimeLine d = new TimeLine();
        d.setId("1");
        d.setDate("12/02/96");
        timeLines.add(d);
        TimeLine e = new TimeLine();
        e.setId("1");
        e.setDate("12/02/96");
        timeLines.add(e);
        mAdapter = new TimeLineAdapter(timeLines, getContext());
        recyclerView.setAdapter(mAdapter);
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
