package me.kevincampos.popularmovies.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.kevincampos.popularmovies.R;
import me.kevincampos.popularmovies.databinding.ActivityHomeBinding;
import me.kevincampos.popularmovies.ui.SettingsActivity;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        setSupportActionBar(binding.toolbar);
        TextView toolbarText = (TextView) binding.toolbar.getChildAt(0);
        toolbarText.setTextColor(Color.BLACK);
        toolbarText.setFontFeatureSettings("smcp");

        fixStatusBarPadding();

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getBaseContext(), getSupportFragmentManager());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setOffscreenPageLimit(3);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_action:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void fixStatusBarPadding() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0 && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);

            LinearLayout.LayoutParams separatorLayoutParams = (LinearLayout.LayoutParams) binding.statusBarPadding.getLayoutParams();
            separatorLayoutParams.height = statusBarHeight;
            binding.statusBarPadding.setLayoutParams(separatorLayoutParams);
        }
    }

    private class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        private final static int PAGE_COUNT = 3;

        private final Context context;

        public FragmentPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return new PopularMoviesFragment();
                }
                case 1: {
                    return new TopRatedMoviesFragment();
                }
                case 2: {
                    return new FavoriteMoviesFragment();
                }
                default:
                    throw new RuntimeException("Invalid position for FragmentPagerAdapter: " + position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: {
                    return context.getString(R.string.tab_popular_movies);
                }
                case 1: {
                    return context.getString(R.string.tab_top_rated_movies);
                }
                case 2: {
                    return context.getString(R.string.tab_favorite_movies);
                }
                default:
                    throw new RuntimeException("Invalid position for FragmentPagerAdapter: " + position);
            }
        }

    }
}
