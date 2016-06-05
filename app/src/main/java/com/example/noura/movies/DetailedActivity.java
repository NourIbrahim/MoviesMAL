package com.example.noura.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailedActivityFragment.DETAIL_MOVIE,
                    getIntent().getParcelableExtra(DetailedActivityFragment.DETAIL_MOVIE));

            DetailedActivityFragment fragment = new DetailedActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
}