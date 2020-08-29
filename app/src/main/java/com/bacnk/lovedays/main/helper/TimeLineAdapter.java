package com.bacnk.lovedays.main.helper;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bacnk.lovedays.R;
import com.bacnk.lovedays.main.units.TimeLine;
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
        holder.text_timeline_title.setText(timeLines.get(position).getSubject());
        holder.text_timeline_content.setText(timeLines.get(position).getContent());

        // Set status
        if (timeLines.get(position).getStatus() != null && timeLines.get(position).getStatus().equals("active")) {
            (holder).mTimelineView.setMarker(context.getDrawable(R.drawable.ic_maker_active_edit));
            (holder).mTimelineView.setStartLineColor(ContextCompat.getColor(context, R.color.white), 2);
        } else if (timeLines.get(position).getStatus() != null && timeLines.get(position).getStatus().equals("inactive")) {
            (holder).mTimelineView.setMarker(context.getDrawable(R.drawable.ic_marker_inactive));

            // Line Start
            (holder).mTimelineView.setStartLineColor(ContextCompat.getColor(context, R.color.white), 0);
            (holder).mTimelineView.setEndLineColor(ContextCompat.getColor(context, R.color.white), 0);
        }
        else {
            (holder).mTimelineView.setMarker(context.getDrawable(R.drawable.ic_maker));
            (holder).mTimelineView.setStartLineColor(ContextCompat.getColor(context, R.color.white), 3);
            (holder).mTimelineView.setEndLineColor(ContextCompat.getColor(context, R.color.white), 3);
        }

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
        public TextView text_timeline_title;
        public TextView text_timeline_content;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = itemView.findViewById(R.id.timeline);
            text_timeline_date = itemView.findViewById(R.id.text_timeline_date);
            text_timeline_title = itemView.findViewById(R.id.text_timeline_title);
            text_timeline_content = itemView.findViewById(R.id.text_content);
            mTimelineView.initLine(viewType);
        }

    }

}
