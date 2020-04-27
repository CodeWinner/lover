package com.example.lovedays.main.helper;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lovedays.R;
import com.example.lovedays.main.units.TimeLine;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    List<TimeLine> timeLines;
    Context context;

    public TimeLineAdapter (List<TimeLine> timeLines, Context context) {
        this.timeLines = timeLines;
        this.context = context;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        holder.text_timeline_date.setText(timeLines.get(position).getDate());
        if (timeLines.get(position).getStatus() != null && timeLines.get(position).getStatus().equals("active"))
            ((TimeLineViewHolder) holder).mTimelineView.setMarker(context.getDrawable(R.drawable.ic_maker_active));
        else if (timeLines.get(position).getStatus() != null && timeLines.get(position).getStatus().equals("inactive"))
            ((TimeLineViewHolder) holder).mTimelineView.setMarker(context.getDrawable(R.drawable.ic_marker_inactive));
        else
            ((TimeLineViewHolder) holder).mTimelineView.setMarker(context.getDrawable(R.drawable.ic_maker));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setMaker(TimeLineViewHolder holder, int drawable, int colorFilter) {
        holder.mTimelineView.setMarker(context.getDrawable(drawable), colorFilter);
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return timeLines.size();
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder{
        public TimelineView mTimelineView;
        public TextView text_timeline_date;
        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = itemView.findViewById(R.id.timeline);
            text_timeline_date = itemView.findViewById(R.id.text_timeline_date);

            mTimelineView.initLine(viewType);
        }

    }

}
