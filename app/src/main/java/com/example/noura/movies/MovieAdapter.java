package com.example.noura.movies;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rabab on 18/12/15.
 */


//custom adapter
public class MovieAdapter extends ArrayAdapter<MovieItem> {
    private Context mContext;
    //layout
    private int layoutResourceId;
    private ArrayList<MovieItem> mGridData ;
    // Flag to determine if we want to use a separate view for "today".
    private boolean useTodayLayout=true;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *  @param mContext        The current context. Used to inflate the layout file.
     * @param mGridData A List of AndroidMovie objects to display in a list*/

    public MovieAdapter(FragmentActivity mContext, int layoutResourceId, ArrayList<MovieItem> mGridData) {
        //super(context, 0, Movies);
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;

        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.

    }

    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<MovieItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

//to display row that is content image and text

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;


        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            //to display text and image
            holder.titleTextView = (TextView) row.findViewById(R.id.movie_text);
            holder.imageView = (ImageView) row.findViewById(R.id.movie_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        // Gets the AndroidMovie object from the ArrayAdapter at the appropriate position
        MovieItem item = mGridData.get(position);
        holder.titleTextView.setText((item.getTitle()));

        Log.i("Hema" , item.getTitle());

        //image
        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        //Picasso.with(mContext).load("http://i.imgur.com/DvpvklR.png").into(holder.imageView);
        return row;
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        this.useTodayLayout = useTodayLayout;
    }

    static class ViewHolder {
        TextView titleTextView;
        ImageView imageView;
    }
}