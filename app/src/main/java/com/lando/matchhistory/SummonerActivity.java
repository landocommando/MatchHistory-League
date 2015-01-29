package com.lando.matchhistory;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.lando.matchhistory.Adapter.DrawerAdapter;
import com.lando.matchhistory.AsyncTask.BaseTask;
import com.lando.matchhistory.AsyncTask.MatchUpdateTask;
import com.lando.matchhistory.AsyncTask.SummonerUpdateTask;
import com.lando.matchhistory.AsyncTask.VersionUpdateTask;
import com.lando.matchhistory.Fragment.MatchHistoryFragment;
import com.lando.matchhistory.Models.Summoner;

import io.realm.Realm;
import io.realm.RealmChangeListener;


public class SummonerActivity extends ActionBarActivity implements BaseTask.UpdateResponse{

    public static final String DEBUG_INFO = "MatchHistory";
    private Realm mRealm;
    private ProgressBar mProgressBar;
    private boolean mIsDownloadInProgress = false;
    private SummonerUpdateTask mSummonerTask;
    private BaseTask mTask;

    private MatchHistoryFragment mMatchHistoryFragment;

    /**
     * Setting up navigation drawer
     */
    String TITLES[] = {"Profile","Match History","Masteries","Runes"};
    int ICONS[] = {R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
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
        getMenuInflater().inflate(R.menu.menu_summoner_search, menu);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
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
                    Summoner summoner  = mRealm.where(Summoner.class).equalTo("name",query).findFirst();
                    setup(summoner);
                }
            });

        }
    }
    private void setup(Summoner summoner){
        mMatchHistoryFragment = MatchHistoryFragment.newInstance(summoner.getId());

        mAdapter = new DrawerAdapter(getBaseContext(),TITLES,ICONS,summoner.getName(),summoner.getSummonerLevel(),summoner.getProfileIconId());       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture
        mRecyclerView.setAdapter(mAdapter);

    }
    private void updateMatches(Summoner player){
        if (mIsDownloadInProgress)
            return;
        mTask = new MatchUpdateTask(this,player.getName(),"na");
        executeTask();
    }
    private void executeTask(){
        mTask.setListener(this);
        mIsDownloadInProgress = true;
        mProgressBar.setVisibility(View.VISIBLE);
        if(mTask instanceof MatchUpdateTask)
            ((MatchUpdateTask)mTask).execute((Void)null);
        else if (mTask instanceof VersionUpdateTask)
            ((VersionUpdateTask)mTask).execute((Void) null);
    }
    @Override
    public void processFinish() {
        mTask.setListener(null);
        mTask = null;
        mIsDownloadInProgress = false;
        mProgressBar.setVisibility(View.GONE);
        Log.v(DEBUG_INFO, "Complete");
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
