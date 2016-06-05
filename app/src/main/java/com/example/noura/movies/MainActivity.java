package com.example.noura.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback{

    private static final String DetailedActivityFragment_TAG = "DFTAG";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailedActivityFragment(),
                                DetailedActivityFragment_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
         //  getSupportActionBar().setElevation(0f);
        }
        MainActivityFragment forecastFragment =  ((MainActivityFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movie));
        forecastFragment.setUseTodayLayout(!mTwoPane);

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_settings, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            //to open setting activity
//            startActivity(new Intent(this,SettingsActivity.class) );
//
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onItemSelected(MovieItem movie) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
          //  bundle.putString("name", fullName);

         //   arguments.putParcelable(DetailedActivityFragment.DETAIL_MOVIE, movie);

            DetailedActivityFragment fragment = new DetailedActivityFragment();
            fragment.setArguments(arguments);

           arguments.putString("id", movie.getId());
            arguments.putString("title", movie.getTitle());
            arguments.putString("image", movie.getImage());
            arguments.putString("overview", movie.getOverview());
            arguments.putString("release_date", movie.getRelease_date());
            arguments.putString("vote_average", movie.getVote_average());
arguments.putString("backdrop_path" , movie.getBackdrop_path());



            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DetailedActivityFragment_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailedActivity.class);
                  //  .putExtra(DetailedActivityFragment.DETAIL_MOVIE, movie);

         //   Intent intent = new Intent(this, DetailedActivity.class);
            intent.putExtra("id", movie.getId());
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("image", movie.getImage());
            intent.putExtra("overview", movie.getOverview());
            intent.putExtra("release_data", movie.getRelease_date());
            intent.putExtra("vote_average", movie.getVote_average());
            intent.putExtra("id", movie.getId());
            intent.putExtra("backdrop_path", movie.getBackdrop_path());

            startActivity(intent);
        }
    }
}
