package com.lando.matchhistory;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.lando.matchhistory.Adapter.DrawerAdapter;
import com.lando.matchhistory.AsyncTask.SummonerUpdateTask;
import com.lando.matchhistory.AsyncTask.VersionUpdateTask;
import com.lando.matchhistory.Fragment.MasteriesFragment;
import com.lando.matchhistory.Fragment.MatchHistoryFragment;
import com.lando.matchhistory.Fragment.ProfileFragment;
import com.lando.matchhistory.Fragment.RuneFragment;
import com.lando.matchhistory.Models.ProfileIcon;
import com.lando.matchhistory.Models.Summoner;

import io.realm.Realm;
import io.realm.RealmChangeListener;


public class SummonerActivity extends ActionBarActivity{

    public static final String DEBUG_INFO = "MatchHistory";
    private Realm mRealm;
    private ProgressBar mProgressBar;
    private boolean mIsDownloadInProgress = false;
    private SummonerUpdateTask mSummonerTask;
    private SearchView mSearchView;

    //Fragments that relate to the navigation drawer
    private ProfileFragment mProfileFragment;
    private MatchHistoryFragment mMatchHistoryFragment;
    private MasteriesFragment mMasteriesFragment;
    private RuneFragment mRuneFragment;

    /**
     * Setting up navigation drawer
     */
    String TITLES[] = {"Profile","Match History","Masteries","Runes"};
    int ICONS[] = {R.drawable.profile,R.drawable.history,R.drawable.mastery,R.drawable.rune};
    private Toolbar mToolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    DrawerAdapter mAdapter;                               // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner);
        mRealm = Realm.getInstance(this);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,mToolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
        handleIntent(getIntent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        ComponentName cn = new ComponentName(this, SummonerActivity.class);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mSearchView.clearFocus();
        mSearchView.setIconified(true);
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {
        if(mIsDownloadInProgress)
            return;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            mIsDownloadInProgress = true;
            mProgressBar.setVisibility(View.VISIBLE);
            mSummonerTask = new SummonerUpdateTask(this,query,"na",new SummonerUpdateTask.UpdateSummonerListener() {
                @Override
                public void OnProcessComplete(String name) {
                    mIsDownloadInProgress = false;
                    mProgressBar.setVisibility(View.GONE);
                    mSummonerTask.setListener(null);
                    mSummonerTask = null;
                    //got the id now
                    Summoner summoner  = mRealm.where(Summoner.class).equalTo("name",query,false).findFirst();
                    setup(summoner);
                }
            });
            mSummonerTask.execute((Void)null);

        }
    }
    private void setup(Summoner summoner){
        mProfileFragment = ProfileFragment.newInstance("","");
        mMatchHistoryFragment = MatchHistoryFragment.newInstance(summoner.getId());
        mMasteriesFragment = MasteriesFragment.newInstance("","");
        mRuneFragment = RuneFragment.newInstance("","");

        ProfileIcon pi = mRealm.where(ProfileIcon.class).equalTo("id",summoner.getProfileIconId()).findFirst(); //get ProfileIcon for summoner
        mAdapter = new DrawerAdapter(getBaseContext(),TITLES,ICONS,summoner.getName(),summoner.getSummonerLevel(),pi.getImage().getFull());       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mRecyclerView.setAdapter(mAdapter);

        getFragmentManager().beginTransaction()
                .add(R.id.container, mMatchHistoryFragment).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mRealm.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                // notify the fragment
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRealm.removeAllChangeListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRealm != null)
            mRealm.close();
    }
}
