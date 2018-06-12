package com.minhduc.goiymonan.User;

/**
 * Created by minhduc on 12/9/2017.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhduc.goiymonan.R;
import com.squareup.picasso.Picasso;

import java.util.List;
public class VideoYoutubeAdapter extends BaseAdapter {


    private Context context;
    private int layout;
    private List<VideoYoutube> videoYouTubeList;

    public VideoYoutubeAdapter(Context context, int layout, List<VideoYoutube> videoYouTubeList) {
        this.context = context;
        this.layout = layout;
        this.videoYouTubeList = videoYouTubeList;
    }

    @Override
    public int getCount() {
        return videoYouTubeList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        ImageView imgThumbnail;
        TextView txtTitle;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if(view == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.imgThumbnail = (ImageView) view.findViewById(R.id.imageviewHinhyotube);
            holder.txtTitle = (TextView) view.findViewById(R.id.textviewTitleYoutube);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        VideoYoutube videoYouTube = videoYouTubeList.get(i);

        holder.txtTitle.setText(videoYouTube.getTitle());
        Picasso.with(context).load(videoYouTube.getUrlVideo()).into(holder.imgThumbnail);

        return view;
    }
}