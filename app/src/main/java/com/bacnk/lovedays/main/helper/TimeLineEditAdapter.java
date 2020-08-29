package com.bacnk.lovedays.main.helper;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bacnk.lovedays.R;
import com.bacnk.lovedays.main.units.TimeLine;
import com.github.vipulasri.timelineview.TimelineView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class TimeLineEditAdapter extends RecyclerView.Adapter<TimeLineEditAdapter.TimeLineViewHolder> {

    List<TimeLine> timeLines;
    Context context;

    public TimeLineEditAdapter(List<TimeLine> timeLines, Context context) {
        this.timeLines = timeLines;
        this.context = context;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline_edit, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        holder.text_timeline_date.setText(timeLines.get(position).getDate());
        // Set subject
        holder.text_timeline_title.setText(timeLines.get(position).getSubject());

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

        // Set content
        holder.text_timeline_content.setText(timeLines.get(position).getContent());

        // Set image
        String pathImage1 = timeLines.get(position).getImagePath1();
        String pathImage2 = timeLines.get(position).getImagePath2();
        String pathImage3 = timeLines.get(position).getImagePath3();

        Log.i("PATH_IMAGE", pathImage1 + "   PATH_IMAGE");
        Picasso.with(context).load(pathImage1).fit().centerCrop().into(holder.imageMemory1);
        Picasso.with(context).load(pathImage2).fit().centerCrop().into(holder.imageMemory2);
        Picasso.with(context).load(pathImage3).fit().centerCrop().into(holder.imageMemory3);

    }

    /**
     * Set image from path get from DB
     * @param path
     * @param imageView
     */
    private void setImageView(String path, ImageView imageView) {
        final File file = new File(path);
        if (path != null && file.exists()) {
            imageView.setImageURI(null);
            imageView.setImageURI(Uri.parse(path));
        } else {
            imageView.setImageResource(R.drawable.change_image);
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

    public class TimeLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TimelineView mTimelineView;
        public TextView text_timeline_date;
        public TextView text_timeline_title;
        public TextView text_timeline_content;
        public ImageView imageMemory1;
        public ImageView imageMemory2;
        public ImageView imageMemory3;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = itemView.findViewById(R.id.timeline);
            text_timeline_date = itemView.findViewById(R.id.text_timeline_date);
            text_timeline_title = itemView.findViewById(R.id.text_timeline_title);
            text_timeline_content = itemView.findViewById(R.id.text_content);
            imageMemory1 = itemView.findViewById(R.id.imageMemory1);
            imageMemory2 = itemView.findViewById(R.id.imageMemory2);
            imageMemory3 = itemView.findViewById(R.id.imageMemory3);

            mTimelineView.initLine(viewType);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
