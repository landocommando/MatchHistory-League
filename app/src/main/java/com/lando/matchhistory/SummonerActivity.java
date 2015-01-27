package com.lando.matchhistory;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_search);
        mRealm = Realm.getInstance(this);
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
