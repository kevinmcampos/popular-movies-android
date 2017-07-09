package me.kevincampos.popularmovies.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.data.api.FetchMoviesTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new FetchMoviesTask(getBaseContext()).execute();
    }
}
