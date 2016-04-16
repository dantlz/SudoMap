package com.anchronize.sudomap.navigationdrawer;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.anchronize.sudomap.R;
import com.anchronize.sudomap.navigationdrawer.youreventfragments.PastFragment;
import com.anchronize.sudomap.navigationdrawer.youreventfragments.UpcomingFragment;
import com.anchronize.sudomap.objects.Event;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

//Glarence Zhao
//Tab UI code modified/referenced from Android Hive
//http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/

public class YourEventActivity extends AppCompatActivity {

    private Firebase ref;

    private Toolbar toolBar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //FOR TESTING
    private ArrayList<Event> upcomingEvents = new ArrayList<Event>();
    private ArrayList<Event> pastEvents = new ArrayList<Event>();
    public static final String UPCOMING_KEY = "UPCOMING EVENTS";
    public static final String PAST_KEY = "PAST EVENTS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_event);

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TEST
        populateEvents();

        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //set context for firebase
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://anchronize.firebaseio.com");
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPageAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        UpcomingFragment upcomingFragment = new UpcomingFragment();
        PastFragment pastFragment = new PastFragment();
        //setup Array passing
        Bundle upcomingBundle = new Bundle();
        upcomingBundle.putSerializable(UPCOMING_KEY, upcomingEvents);
        upcomingFragment.setArguments(upcomingBundle);
        Bundle pastBundle = new Bundle();
        pastBundle.putSerializable(PAST_KEY, pastEvents);
        pastFragment.setArguments(pastBundle);

        viewPageAdapter.addFragment(upcomingFragment, getResources().getString(R.string.upcoming_fragment_title));
        viewPageAdapter.addFragment(pastFragment, getResources().getString(R.string.past_fragment_title));
        viewPager.setAdapter(viewPageAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<Fragment>();
        private final List<String> fragmentTitleList = new ArrayList<String>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    //FOR TESTING
    private void populateEvents(){

        //past events
        Event springFest = new Event();
        springFest.setTitle("SPRINGFEST 2016!");
        Event conquest = new Event();
        conquest.setTitle("Conquest");
        pastEvents.add(springFest);
        pastEvents.add(conquest);

        //upcoming events
        Event lidoConcert = new Event();
        lidoConcert.setTitle("Lido feat. Snakehips");
        Event yeezusConcert = new Event();
        yeezusConcert.setTitle("YEEZUS!!!!!!!!!!");
        upcomingEvents.add(lidoConcert);
        upcomingEvents.add(yeezusConcert);
    }

}
