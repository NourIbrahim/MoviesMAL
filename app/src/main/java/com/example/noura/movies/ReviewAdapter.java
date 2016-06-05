package com.example.noura.movies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by noura on 31/12/15.
 */
public class ReviewAdapter extends ArrayAdapter<ReviewItem> {
    private Context Context;
    //layout
    private int layoutResourceId;
    private ArrayList<ReviewItem> reviewData ;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     * @param reviewData A List of AndroidMovie objects to display in a list*/

    public ReviewAdapter( Context context, int layoutResourceId, ArrayList<ReviewItem> reviewData) {
        //super(context, 0, Movies);
        super(context, layoutResourceId, reviewData);
        this.layoutResourceId = layoutResourceId;
        this.Context = context;
        this.reviewData = reviewData;

        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.

    }

    /**
     * Updates grid data and refresh grid items.
     * @param reviewData
     */
    public void setReviewData(ArrayList<ReviewItem> reviewData) {
        this.reviewData = reviewData;
        notifyDataSetChanged();
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     *
     *                    (search online for "android view recycling" to learn more)
     * @return The View for the position in the AdapterView.
     */



    @Override
    public ReviewItem getItem(int position) {

        return super.getItem(position);
    }
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
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            //to display text and text

            holder.titleTextView = (TextView) row.findViewById(R.id.content);
            holder.titleTextView1 = (TextView) row.findViewById(R.id.author);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        // Gets the AndroidMovie object from the ArrayAdapter at the appropriate position
        ReviewItem item = reviewData.get(position);
        holder.titleTextView.setText(item.getContent());

//        ReviewItem item2 = reviewData.get(position);
        holder.titleTextView1.setText(item.getAuthor());


          return row;
    }

//    @Override
//    public int getCount() {
//
//        reviewData.size();
//        return super.getCount();
//    }

    static class ViewHolder {
        TextView titleTextView;
        public TextView titleTextView1;
    }
}