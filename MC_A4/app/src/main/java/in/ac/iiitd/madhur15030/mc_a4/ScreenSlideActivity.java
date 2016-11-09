
package in.ac.iiitd.madhur15030.mc_a4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import in.ac.iiitd.madhur15030.mc_a4.database.DBHelper;

/**
 * Created by Madhur on 04/11/16.
 */

public class ScreenSlideActivity extends AppCompatActivity {

    private final String TODO_INDEX="toindex";

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(getIntent().getIntExtra(TODO_INDEX, 0));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Log.v("DEBUG", String.valueOf(position));
            return ToDoDetailFragment.create(position);
        }
        @Override
        public int getCount() {
            return DBHelper.getInstance().getAllToDoRecord(getApplicationContext()).size();
        }
    }
}
