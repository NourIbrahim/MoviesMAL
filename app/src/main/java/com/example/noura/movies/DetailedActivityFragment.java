package com.example.noura.movies;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailedActivityFragment extends Fragment {

    //for share in menu
    private static final String FORECAST_SHARE_HASHTAG = " #MovieApp";
    private String mMovietStr;

    static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    private Toast nToast;

    //data show in detail
    private TextView titleTextView;
    private TextView overviewTextView;
    private TextView release_dataTextView;
    private TextView vote_averageTextView;
    private ImageView imageView;
    private ImageView backdrop;

    private String id;
    private String title;
    private String image;
    private String overview;
    private String vote_average;
    private String release_data;
    private String backdrop_path;


    private ScrollView mDetailLayout;

    //to display data in grid view


    Button Favorite;
    Button delete;


    public ListView videoListView;
    public ArrayList<VideoItem> videoData;
    public VideoAdapter videoAdapter;

    //object from adapter
    public ReviewAdapter reviewAdapter;
    public ListView mListView;
    //create array list from movie item inside mGridView
    public ArrayList<ReviewItem> reviewData;
    public Context context;
    public MovieItem mMovie;

    public DetailedActivityFragment() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to display menu
        setHasOptionsMenu(true);

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
//            mMovie = arguments.getParcelable(DetailedActivityFragment.DETAIL_MOVIE);
//            id = mMovie.getId();
//            title=mMovie.getTitle();
//            image=mMovie.getImage();
//            overview=mMovie.getOverview();
//            vote_average=mMovie.getVote_average();
//            release_data=mMovie.getRelease_date();
//            backdrop_path=mMovie.getBackdrop_path();

            id = arguments.getString("id");
            title = arguments.getString("title");
            image = arguments.getString("image");
            overview = arguments.getString("overview");
            release_data = arguments.getString("release_date");
            vote_average = arguments.getString("vote_average");
            backdrop_path = arguments.getString("backdrop_path");

        }


        context = getActivity();
        if (getActivity().getIntent().getExtras() != null) {

            id = getActivity().getIntent().getStringExtra("id");
            title = getActivity().getIntent().getStringExtra("title");
            image = getActivity().getIntent().getStringExtra("image");
            backdrop_path = getActivity().getIntent().getStringExtra("backdrop_path");
            overview = getActivity().getIntent().getStringExtra("overview");
            release_data = getActivity().getIntent().getStringExtra("release_data");
            vote_average = getActivity().getIntent().getStringExtra("vote_average");

        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detailed, container, false);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.show();

        titleTextView = (TextView) rootView.findViewById(R.id.title);
        overviewTextView = (TextView) rootView.findViewById(R.id.overview);
        release_dataTextView = (TextView) rootView.findViewById(R.id.release_date);
        vote_averageTextView = (TextView) rootView.findViewById(R.id.vote_average);
        imageView = (ImageView) rootView.findViewById(R.id.grid_item_image);
        backdrop = (ImageView) rootView.findViewById(R.id.backdrop);

        titleTextView.setText(title);
        overviewTextView.setText(overview);
        release_dataTextView.setText(release_data);
        vote_averageTextView.setText(vote_average);
        Picasso.with(getActivity()).load(image).into(imageView);
        Picasso.with(getActivity()).load(backdrop_path).into(backdrop);


        //for review
        mListView = (ListView) rootView.findViewById(R.id.listView_review);
        reviewData = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(getActivity(), R.layout.review_item, reviewData);

        mListView.setAdapter(reviewAdapter);

        FetchReviews reviewMovie = new FetchReviews();
        reviewMovie.execute(id);


        videoListView = (ListView) rootView.findViewById(R.id.listView_video);
        videoData = new ArrayList<>();
        videoAdapter = new VideoAdapter(getActivity(), R.layout.video_item, videoData);
        videoListView.setAdapter(videoAdapter);

        FetchVideo videoMovie = new FetchVideo();
        videoMovie.execute(id);


        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                VideoItem x = videoAdapter.getItem(position);
                String videoId = x.getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                intent.putExtra("VIDEO_ID", videoId);
                startActivity(intent);
            }
        });

//
//        //insert data
        Favorite = (Button) rootView.findViewById(R.id.btnSave);
        Favorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Helper help = new Helper(context);
                MovieItem item = new MovieItem();
                item.set_id(Integer.parseInt(id));
                item.setTitle(title);
                item.setImage(image);
                item.setOverview(overview);
                item.setRelease_date(release_data);
                item.setVote_average(vote_average);
                //   item.setBackdrop_path(backdrop_path);

                help.addFavor(item);

                //to show id that you save in sql lite use get_id() not getId
                Log.i("Done insret ", " " + item.get_id() + " " + item.getTitle() + " " + item.getOverview() + " " + item.getVote_average() + " " + item.getRelease_date());

                nToast = Toast.makeText(getActivity(), getString(R.string.added_to_favorites), Toast.LENGTH_SHORT);
                nToast.show();

            }
        });


        delete = (Button) rootView.findViewById(R.id.button);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Helper help = new Helper(context);
                MovieItem item = new MovieItem();
                item.set_id(Integer.parseInt(id));

                help.deleteFavor(item);
                Log.i("Done delete ", item.get_id() + " " + item.getTitle());

                nToast = Toast.makeText(getActivity(), getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT);
                nToast.show();

            }
        });

//        Button mPlayButton = (Button) rootView.findViewById(R.id.imageButton);
//        // Default button, if need set it in xml via background="@drawable/default"
//        mPlayButton.setBackgroundResource(R.drawable.ic_favorite_border);
//        mPlayButton.setOnClickListener(mTogglePlayButton);


        return rootView;
    }

//    private boolean isPlay=true;
//
//    View.OnClickListener mTogglePlayButton = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//            // 0change your button background
//
//            if (isPlay == true) {
//
//            }
//
//
//        }
//        else{
//
//            isPlay = !isPlay; // reverse
//        }
//
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

//        final MenuItem action_favorite = menu.findItem(R.id.action_favorite);
//        MenuItem action_share = menu.findItem(R.id.action_share);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);


//        new AsyncTask<Void, Void, Integer>() {
//            @Override
//            protected Integer doInBackground(Void... params) {
//
//                Integer v = Helper.getFavor(getId());
//                return v;
//            }
//
//            @Override
//            protected void onPostExecute(Integer getFavor) {
//                action_favorite.setIcon(getFavor == 1 ?
//                        R.drawable.ic_favorite_full :
//                        R.drawable.ic_favorite_border);
//            }
//        }.execute();

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);


        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }


    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.action_settings:
                //to open setting activity
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
//            case R.id.action_favorite:
//
//                if (mMovie != null) {
//                    // check if movie is in favorites or not
//                    new AsyncTask<Void, Void, Integer>() {
//
//                        @Override
//                        protected Integer doInBackground(Void... params) {
//                            return Helper.getFavor(getId());
//
//                        }
//
//                        @Override
//                        protected void onPostExecute(Integer getFavor) {
//                            // if it is in favorites
//                            if (getFavor == 1) {
//                                // delete from favorites
//                                new AsyncTask<Void, Void, Integer>() {
//                                    @Override
//                                    protected Integer doInBackground(Void... params) {
//                                        return Helper.getFavor(getId());
//
//
//                                    }
//
//                                    @Override
//                                    protected void onPostExecute(Integer rowsDeleted) {
//                                        mMovie.setIcon(R.drawable.ic_favorite_border);
//                                        if (nToast != null) {
//                                            nToast.cancel();
//                                        }
//                                        nToast = Toast.makeText(getActivity(), getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT);
//                                        nToast.show();
//                                    }
//                                }.execute();
//                            }
//
//                            // if it is not in favorites
//                            else {
//                                // add to favorites
//                                new AsyncTask<Void, Void, Uri>() {
//                                    @Override
//                                    protected Uri doInBackground(Void... params) {
//                                        ContentValues values = new ContentValues();
//
//                                        values.put("id", mMovie.getId());
//                                        values.put("title", mMovie.getTitle());
//                                        values.put("image", mMovie.getImage());
//                                        values.put("overview", mMovie.getOverview());
//                                        values.put("release_date", mMovie.getRelease_date());
//                                        values.put("vote_average", mMovie.getVote_average());
//                                        values.put("backdrop_path", mMovie.getBackdrop_path());
//
//                                        return getActivity().getContentResolver().insert(null,
//                                                values);
//                                    }
//
//                                    @Override
//                                    protected void onPostExecute(Uri returnUri) {
//                                        mMovie.setIcon(R.drawable.ic_favorite_full);
//                                        if (nToast != null) {
//                                            nToast.cancel();
//                                        }
//                                        nToast = Toast.makeText(getActivity(), getString(R.string.added_to_favorites), Toast.LENGTH_SHORT);
//                                        nToast.show();
//                                    }
//                                }.execute();
//
//
////        //noinspection SimplifiableIfStatement
////       // if (id == R.id.action_settings) {
////
////            //to open setting activity
////            startActivity(new Intent(getActivity(),SettingsActivity.class) );
////            return true;
////         }
//                            }
//                        }
//                    };
//                    return true;
//                }
//
//                return true;
//            default:

    }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onStart() {
        super.onStart();
        if (mMovie != null) {
            new FetchReviews().execute(Integer.toString(Integer.parseInt(mMovie.getId())));
            new FetchVideo().execute(Integer.toString(Integer.parseInt(mMovie.getId())));
        }
    }
    //asyncTsk
    public class FetchReviews extends AsyncTask<String, Void, Integer> {

        //onPostExecute and doInBackground

        // private final String LOG_TAG = FetchReviews.class.getSimpleName();

        //this integer is result to use in onPostExecute
        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            //when load in gitHub delet this for security
            String appid="18147b0826078c6d5e462bf97f3e032d";

            //  String sortType="popularity.desc";

            try{

                final String BASE_URL="http://api.themoviedb.org/3/movie/"+params[0]+"/reviews";

                //for sorting and settings
                final String APPID_PARAM="api_key";

                Uri builduri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, appid)
                        .build();

                // URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=18147b0826078c6d5e462bf97f3e032d");
                // Create the request to OpenMovie, and open the connection
                URL url = new URL(builduri.toString());

                //open connection
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.v(LOG_TAG, "Build URL " + url.toString());

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String to pull data continue
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    //failed internet
                    return result;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    //internet failed
                    return result;
                }

                //get jason string
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG,"movies json string "+moviesJsonStr);



            }catch (Exception e){

                Log.e("PlaceholderFragment", "Error reviews ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return result;

            }finally{
                if (urlConnection != null) {
                    //close connection
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        //close buffer to stop fetching data
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }


            try {


                getReviewDataFromJson(moviesJsonStr);
                //because internet well done result  =1 and send it to onPostExecute
                result=1;
                return result;
            } catch (JSONException e) {


                Log.e(LOG_TAG, e.getMessage(), e);
                e.getStackTrace();
            }

            //  return new String[0];
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                /** reviewAdapter.clear();
                 for (ReviewItem s : reviewData) {
                 Log.v(LOG_TAG, "Review entry onPostExecute: " + s.getContent());
                 Log.v(LOG_TAG, "Review entry onPostExecute: " + s.getAuthor());
                 }
                 //                reviewAdapter.setReviewData(reviewData);
                 //                reviewAdapter = new ReviewAdapter(context, R.layout.review_item, reviewData);
                 //                mListView.setAdapter(reviewAdapter);
                 reviewAdapter.setReviewData(reviewData);

                 **/
                reviewAdapter.setReviewData(reviewData);


            }

        }


    }



    private final  String LOG_TAG = FetchReviews.class.getSimpleName();


    //get data jason
    private  void getReviewDataFromJson(String moviesJsonStr)
            throws JSONException {
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_ID="id";
        final String OWM_CONTENT = "content";
        final String OWM_AUTHOR = "author";
        final String OWM_RESULT="results";



        //make jason object to hold movie jason string
        JSONObject movieJson = new JSONObject(moviesJsonStr);
        //make jason array to hold number of result this is contain detail of movie
        JSONArray resultsArray = movieJson.getJSONArray(OWM_RESULT);

        //String[] resultStrs = new String[20];


        //call every iten inside class MovieItem
        ReviewItem item;

        //do loop = resultArray that is get from api and i do not know how much
        for(int i = 0; i < resultsArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String content;
            String author;
            String id;


            //now i will make new item to next row
            item = new ReviewItem();

            // Get the JSON object representing the movie

            //specific object to show specific row
            JSONObject movies = resultsArray.getJSONObject(i);

            content = movies.getString(OWM_CONTENT);
            item.setContent(content);
            author = movies.getString(OWM_AUTHOR);
            item.setAuthor(author);

            id = movies.getString(OWM_ID);
            item.setId(id);


            //to add new row
            reviewData.add(item);
        }

        for (ReviewItem s : reviewData) {
            Log.v(LOG_TAG, "Content entry: " + s.getContent());
            Log.v(LOG_TAG, "Author entry: " + s.getAuthor());
        }


    }


    private final String LOG_TAG1 = FetchVideo.class.getSimpleName();

    public class FetchVideo extends AsyncTask<String,Void,Integer>   {


        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            String appid="23367f8a2064dd359f24c91e6991f106";
            try{

                final String BASE_URL="http://api.themoviedb.org/3/movie/"+params[0]+"/videos";
                final String APPID_PARAM="api_key";

                Uri builduri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(APPID_PARAM, appid)
                        .build();
                URL url = new URL(builduri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.v(LOG_TAG1, "Build URL " + url.toString());
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return result;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return result;
                }

                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG1,"videos json string "+moviesJsonStr);



            }catch (Exception e){

                Log.e("PlaceholderFragment", "Error videos ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return result;

            }finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }



            try {

                Log.e(" videos ",moviesJsonStr.toString());
                getVideosDataFromJson(moviesJsonStr);
                result=1;
                return result;
            } catch (JSONException e) {

                Log.e(LOG_TAG1, e.getMessage(), e);
                e.getStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                //  mGridAdapter.clear();
                for (VideoItem s : videoData) {
                    Log.v(LOG_TAG1, "Review entry onPostExecute: " + s.getKey());


                }
                videoAdapter.setGridData(videoData);
            }
        }
    }


    private void getVideosDataFromJson(String moviesJsonStr) throws JSONException{
        final String OWM_KEY = "key";
        final String OWM_ID="id";
        final String OWM_NAME="name";


        final String OWM_RESULTS="results";

        JSONObject forecastJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = forecastJson.getJSONArray(OWM_RESULTS);

        VideoItem item;

        videoData.clear();
        Log.e("resultsArray", String.valueOf(resultsArray.length()));

        for(int i = 0; i < resultsArray.length(); i++) {
            String key;
            String name;

            String id;

            item = new VideoItem();
            JSONObject movies = resultsArray.getJSONObject(i);



            key = movies.getString(OWM_KEY);
            item.setKey(key);
            name=movies.getString(OWM_NAME);
            item.setName(name);

            videoData.add(item);
        }

        for (VideoItem s : videoData) {
            Log.v(LOG_TAG1, "Video entry: " + s.getKey());
            Log.v(LOG_TAG1, "Video entry: " + s.getId());



        }


    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mMovietStr + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }


}
