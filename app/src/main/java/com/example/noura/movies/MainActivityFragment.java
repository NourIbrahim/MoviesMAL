package com.example.noura.movies;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

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
public class MainActivityFragment extends Fragment {

    boolean mTwoPane;


    //object from  custome adapter
    //   private ArrayAdapter<String> movieAdpter;

    public MainActivityFragment() {
    }

    private int mPosition = ListView.INVALID_POSITION;
    private boolean mUseTodayLayout;

    private static final String SELECTED_KEY = "selected_position";


    public interface Callback {
        void onItemSelected(MovieItem movie);
    }

    /**
     @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
     super.onCreateOptionsMenu(menu, inflater);
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {


     // Handle action bar item clicks here. The action bar will
     // automatically handle clicks on the Home/Up button, so long
     // as you specify a parent activity in AndroidManifest.xml.
     int id = item.getItemId();
     if (id == R.id.action_refresh) {
     updateMovies();

     return true;
     }
     return super.onOptionsItemSelected(item);
     }

     **/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to display menu
        setHasOptionsMenu(true);


/*
        //insert data by me "frozen + 1 "
        Helper help = new Helper(getActivity());
        MovieItem x = new MovieItem();
        x.setTitle("frozen");
        x.setImage("diydgeufg.jpg");


        help.addFavor(x);
       Log.i("add" , x.getTitle());



*/




    }

    private final  String LOG_TAG = FetchMovies.class.getSimpleName();

    //to display data in grid view
    private GridView mGridView;

    //object from adapter
    private MovieAdapter mGridAdapter;
    //  private MovieAdapter mFavorAdapter;

    //create array list from movie item inside mGridView
    //to pop and vote
    private ArrayList<MovieItem> mGridData;


    //to favorite
    //  private ArrayList<MovieItem>mGridDataa;
    //  private ArrayList<MovieItem> mGridFavor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        //inflate to layout that contain grid view
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        //display this object array inside grid view to each  row
        mGridView = (GridView) rootView.findViewById(R.id.movie_grid);

        //Initialize with empty data to get new row
        mGridData = new ArrayList<>();

        //initialize custom adapter

        // Get a reference to the grid view, and attach this adapter to it.
        // Grid view to display images
        //get activity because use fragment i want parent activity for it
        mGridAdapter = new MovieAdapter(getActivity(), R.layout.list_item_movies, mGridData);
        mGridView.setAdapter(mGridAdapter);







        //to execute asyncTask to popular movies
        //   new FetchMovies().execute(formatHighLows());
        // new FetchMovies().execute("vote_average.desc");


        //when click on item in grid view this data will display
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Get item at position
                MovieItem item = (MovieItem) parent.getItemAtPosition(position);
                ((Callback) getActivity()).onItemSelected(item);

                //Pass the image title and url to DetailsActivity
//                Intent intent = new Intent(getActivity(), DetailedActivity.class);
//                intent.putExtra("id", item.getId());
//                intent.putExtra("title", item.getTitle());
//                intent.putExtra("image", item.getImage());
//                intent.putExtra("overview", item.getOverview());
//                intent.putExtra("release_data", item.getRelease_date());
//                intent.putExtra("vote_average", item.getVote_average());
//                intent.putExtra("id", item.getId());
//                intent.putExtra("backdrop_path", item.getBackdrop_path());

                //Start details activity
            //    startActivity(intent);
                mPosition = position;

            }

        });

        // If there's instance state, mine it for useful information.
        // The end-goal here is that the user never knows that turning their device sideways
        // does crazy lifecycle related things.  It should feel like some stuff stretched out,
        // or magically appeared to take advantage of room, but data or place in the app was never
        // actually *lost*.
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            // The listview probably hasn't even been populated yet.  Actually perform the
            // swapout in onLoadFinished.
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mGridAdapter.setUseTodayLayout(mUseTodayLayout);




        return rootView;
    }
    /**

     private void updateMovies() {
     FetchMovies movieTask = new FetchMovies();
     movieTask.execute(" vote_average.desc");

     }

     **/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    private String formatHighLows() {
        // For presentation, assume the user doesn't care about tenths of a degree.

        String result = null;
        SharedPreferences sharedprefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String unitType = sharedprefs.getString(getString(R.string.pref_movie_key), getString(R.string.pref_movie_pop));

        if (unitType.equals(getString(R.string.pref_movie_pop))) {


            result = "popularity.desc";
            Log.v(LOG_TAG, "pop" + result);
        } else if (unitType.equals(getString(R.string.pref_movies_high))) {

            result = "vote_average.desc";
            Log.v(LOG_TAG, "high" + result);
            //Log.v(LOG_TAG, "high: " + unitType);


        } else if (unitType.equals(getString(R.string.pref_favorite))){







            result="f";
            Log.v(LOG_TAG, "favor" + result);







        }




        return result;
    }


    //get data jason
    private  void getMovieDataFromJson(String moviesJsonStr)
            throws JSONException {
        // These are the names of the JSON objects that need to be extracted.
        final String OWM_RESULTS = "results";
        final String OWM_OVERVIEW = "overview";
        final String OWM_VOTE_AVERAGE = "vote_average";
        final String OWM_ORIGINAL_TITLE = "original_title";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWN_RELEASE_DATE = "release_date";
        final String OWM_ID="id";
        final String OWM_BACKPOSTER="backdrop_path";



        //make jason object to hold movie jason string
        JSONObject movieJson = new JSONObject(moviesJsonStr);
        //make jason array to hold number of result this is contain detail of movie
        JSONArray resultsArray = movieJson.getJSONArray(OWM_RESULTS);

        //String[] resultStrs = new String[20];


        //call every iten inside class MovieItem
        MovieItem item;

        //do loop = resultArray that is get from api and i do not know how much
        for(int i = 0; i < resultsArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String original_title;
            String overview;
            String vote_average;
            String poster_path;
            String release_date;
            String id;
            String backdrop_path;


            //now i will make new item to next row
            item = new MovieItem();

            // Get the JSON object representing the movie

            //specific object to show specific row
            JSONObject movies = resultsArray.getJSONObject(i);
            overview = movies.getString(OWM_OVERVIEW);
            item.setOverview(overview);
            original_title = movies.getString(OWM_ORIGINAL_TITLE);
            item.setTitle(original_title);
            vote_average = movies.getString(OWM_VOTE_AVERAGE);
            item.setVote_average(vote_average);
            id = movies.getString(OWM_ID);
            item.setId(id);
            //base url = http://image.tmdb.org/t/p/w185/ + url poster
            poster_path = "http://image.tmdb.org/t/p/w185/" + movies.getString(OWM_POSTER_PATH);
            item.setImage(poster_path);
            backdrop_path="http://image.tmdb.org/t/p/w185/" + movies.getString(OWM_BACKPOSTER);
            item.setBackdrop_path(backdrop_path);

            release_date = movies.getString(OWN_RELEASE_DATE);
            item.setRelease_date(release_date);
            //  resultStrs[i] =poster_path + " - " + original_title +  " - " + vote_average +  " - " +overview +" - " + release_date;
            //  resultStrs[i] ="http://i.imgur.com"+poster_path;

            //to add new row
            mGridData.add(item);
        }

        for (MovieItem s : mGridData) {
            Log.v(LOG_TAG, "Movies entry: " + s.getTitle());
            Log.v(LOG_TAG, "Movies entry: " + s.getImage());
        }
//        return resultStrs;


    }

    @Override
    public void onStart() {
        super.onStart();
        String sortedType=formatHighLows();

        if(sortedType!="f") {
            mGridAdapter.clear();
            //  mFavorAdapter.clear();
            FetchMovies fetchM = new FetchMovies();
            Log.d("sort by", sortedType);
            fetchM.execute(sortedType);
        }
        else {
            Log.d("in sql lite", sortedType);
            //  mGridDataa = new ArrayList<>();
            mGridAdapter.clear();
            Helper help = new Helper(getActivity());
            mGridData= (ArrayList<MovieItem>) help.getAllFavors();
            // mGridView.setAdapter(mGridAdapter);

            Log.d("grid", String.valueOf(mGridData));
            mGridAdapter= new MovieAdapter(getActivity(), R.layout.list_item_movies, mGridData);
            mGridView.setAdapter(mGridAdapter);

            // mGridAdapter.setGridData(mGridDataa);
            //  mFavorAdapter.setNotifyOnChange(true);


        }

        // new FetchMovies().execute(formatHighLows());


    }



    //asyncTsk
    public class FetchMovies extends AsyncTask<String, Void, Integer> {

        //onPostExecute and doInBackground


        @Override
        protected void onPostExecute(Integer result) {
            //i will get result from doInBackground
            //super.onPostExecute(strings);

            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }



        }

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

                final String BASE_URL="http://api.themoviedb.org/3/discover/movie?";

                //for sorting and settings
                final String SORT_PARAM="sort_by";
                final String APPID_PARAM="api_key";

                Uri builduri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
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

                Log.e("PlaceholderFragment ", "Error Movies ", e);
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


                getMovieDataFromJson(moviesJsonStr);
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
    }


    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
        if (mGridAdapter != null) {
            mGridAdapter.setUseTodayLayout(mUseTodayLayout);
        }
    }
}