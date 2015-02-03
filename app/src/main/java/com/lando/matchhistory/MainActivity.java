package com.lando.matchhistory;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.lando.matchhistory.AsyncTask.BaseTask;
import com.lando.matchhistory.AsyncTask.MatchUpdateTask;
import com.lando.matchhistory.AsyncTask.SummonerUpdateTask;
import com.lando.matchhistory.AsyncTask.VersionUpdateTask;
import com.lando.matchhistory.Models.Match;
import com.lando.matchhistory.Provider.RecentSuggestionProvider;

import io.realm.Realm;


public class MainActivity extends ActionBarActivity implements BaseTask.UpdateResponse {

    public static final String DEBUG_INFO = "MatchHistory";
    private ProgressBar mProgressBar;
    private boolean mIsDownloadInProgress = false;
    private BaseTask mTask;

    private SearchView mSearchView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        setSupportActionBar(mToolbar);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.refresh_images:
                updateVersionData();
                return true;
            case R.id.refresh_matches:

                return true;
            case R.id.check_database:

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void updateVersionData(){
        if(mIsDownloadInProgress)
            return;

        mTask = new VersionUpdateTask(this,"na");
        executeTask();
    }


    private void executeTask(){
        mTask.setListener(this);
        mIsDownloadInProgress = true;
        mProgressBar.setVisibility(View.VISIBLE);
            ((VersionUpdateTask)mTask).execute((Void) null);
    }
    @Override
    public void processFinish() {
        mTask.setListener(null);
        mTask = null;
        mIsDownloadInProgress = false;
        mProgressBar.setVisibility(View.GONE);
        Log.v(DEBUG_INFO,"Complete");
    }
}
