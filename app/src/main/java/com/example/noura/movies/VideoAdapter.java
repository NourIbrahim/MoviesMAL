package com.example.noura.movies;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by noura on 01/01/16.
 */
public class VideoAdapter extends ArrayAdapter<VideoItem> {
    private Context context;
    //layout
    private int layoutResourceId;
    private ArrayList<VideoItem> videoData ;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     * @param videoData A List of AndroidMovie objects to display in a list*/

    public VideoAdapter( Context context, int layoutResourceId, ArrayList<VideoItem> videoData) {
        //super(context, 0, Movies);
        super(context, layoutResourceId, videoData);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.videoData = videoData;

        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.

    }

    /**
     * Updates grid data and refresh grid items.
     * @param videoData
     */
    public void setGridData(ArrayList<VideoItem> videoData) {
        this.videoData = videoData;
        notifyDataSetChanged();
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @return The View for the position in the AdapterView.
     */

//to display row that is content image and text


    @Override
    public VideoItem getItem(int position) {

        return super.getItem(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;


        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.

        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            //to display text and text

            holder.titleTextView3=(TextView)row.findViewById(R.id.key);
          //  holder.titleTextView5=(TextView)row.findViewById(R.id.name);


            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        // Gets the AndroidMovie object from the ArrayAdapter at the appropriate position


      VideoItem item4 = videoData.get(position);
        if(holder.titleTextView3==null){
            Log.e( "nourhan","holder.titleTextView1=null " );

        }
        holder.titleTextView3.setText(Html.fromHtml(item4.getKey()));

//
//        VideoItem item6 = videoData.get(position);
//        if(holder.titleTextView5==null){
//            Log.e( "nourhan","holder.titleTextView1=null " );
//
//        }
//        holder.titleTextView5.setText(Html.fromHtml(item6.getName()));



        return row;
    }

    static class ViewHolder {

        public TextView titleTextView3;
    //    public TextView titleTextView5;

    }

}
