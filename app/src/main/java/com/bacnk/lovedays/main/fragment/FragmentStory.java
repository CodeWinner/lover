package com.bacnk.lovedays.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bacnk.lovedays.common.LoveCommon;
import com.bacnk.lovedays.main.helper.RecyclerItemClickListener;
import com.bacnk.lovedays.main.helper.TimeLineAdapter;
import com.bacnk.lovedays.main.time_line.TimeLineActivity;
import com.bacnk.lovedays.main.units.TimeLine;
import com.bacnk.lovedays.R;
import com.bacnk.lovedays.main.database.DatabaseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FragmentStory extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private ImageButton mButtonAddMemory;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseService databaseService ;
    public List<TimeLine> timeLines = null;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date currentTime = Calendar.getInstance().getTime();
    public static int positionTimelineScroll = 0;

    // newInstance constructor for creating fragment with arguments
    public static FragmentStory newInstance(int page, String title) {
        FragmentStory fragmentStory = new FragmentStory();
        return fragmentStory;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);
        recyclerView = view.findViewById(R.id.TimeLineRecycle);
        mButtonAddMemory = view.findViewById(R.id.imageButtonAddMemory);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        // Select data timeline in SQL
        timeLines = databaseService.getAllTimeline();

        if (timeLines != null  && timeLines.size() > 0) {
            Collections.sort(timeLines);

            mAdapter = new TimeLineAdapter(setStatusTimeline(timeLines), getContext());
            recyclerView.setAdapter(mAdapter);

            recyclerView.smoothScrollToPosition(positionTimelineScroll);
        }

        // Click recycler view
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getContext(), TimeLineActivity.class);
                        Bundle dataPosition = new Bundle();
                        dataPosition.putString(LoveCommon.POS_BUNDLE_STRING, position + "");
                        intent.putExtras(dataPosition);
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }));

        mButtonAddMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TimeLineActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
            }
        });
        return view;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseService = new DatabaseService(getContext());
        databaseService.open();
    }

    /**
     * Set status
     * @param timeLines
     * @return
     */
    private List<TimeLine> setStatusTimeline(List<TimeLine> timeLines) {
        int i = 0;
        do {
            try {
                if (timeLines.get(i).getDate() != null
                        && sdf.parse(timeLines.get(i).getDate()).before(currentTime)) {

                    timeLines.get(i).setStatus("inactive");
                } else if (timeLines.get(i).getDate() != null
                        && sdf.parse(timeLines.get(i).getDate()).after(currentTime)
                        && i != 0) {

                    positionTimelineScroll = i ;
                    timeLines.get(i-1).setStatus("active");
                    return timeLines;
                }

                i++;
            } catch (ParseException e) {
                e.printStackTrace();
                i++;
            }
        } while (i < timeLines.size());

        return timeLines;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (databaseService != null) {
            timeLines = databaseService.getAllTimeline();
            if (timeLines != null && timeLines.size() > 0) {
                Collections.sort(timeLines);

                mAdapter = new TimeLineAdapter(setStatusTimeline(timeLines), getContext());
                recyclerView.setAdapter(mAdapter);

                recyclerView.smoothScrollToPosition(positionTimelineScroll);
            }
        }
    }


}
