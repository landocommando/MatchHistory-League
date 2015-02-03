package com.lando.matchhistory.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.lando.matchhistory.ApiClient.ApiClient;
import com.lando.matchhistory.MainActivity;
import com.lando.matchhistory.Models.Champion;
import com.lando.matchhistory.Models.Match;
import com.lando.matchhistory.Models.Summoner;
import com.lando.matchhistory.R;

import java.util.List;
import java.util.Map;

import io.realm.Realm;


/**
 * Created by Lando on 1/2/2015.
 */
public class MatchUpdateTask extends AsyncTask<Void,Void,Boolean> {

    protected MatchUpdateResponse mListener=null;

    public interface MatchUpdateResponse {
        void processFinish(boolean finished);
    }
    public void setListener(MatchUpdateResponse listener){
        mListener = listener;
    }

    private long mSummonerId;
    private final String mRegion;
    private final Context mContext;

    public MatchUpdateTask(Context context, long id, String region){
        this.mContext = context;
        this.mSummonerId = id;
        this.mRegion = region;
    }
    /**
     * Try to improve this method
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        Realm realm = null;
        boolean finish = true;
        try {
            realm = Realm.getInstance(mContext);
            realm.beginTransaction();
            Summoner summoner = realm.where(Summoner.class).equalTo("id",mSummonerId).findFirst();
            Map<String,List<Match>> matches = ApiClient.getLeagueApiClient().getMatches(mRegion,mSummonerId, 0,5,mContext.getResources().getString(R.string.api_key));
            Log.v(MainActivity.DEBUG_INFO,"Starting the loop");
            List<Match> matchlist = matches.get("matches");

            for (int i = 0; i < matchlist.size(); i++) {
                Match match;
                if ((match = realm.where(Match.class).equalTo("matchId", matchlist.get(i).getMatchId()).findFirst()) != null) {
                    if(summoner.getMatches().where().equalTo("matchId",match.getMatchId()).findFirst()!=null){
                        continue;
                    }
                }else {
                    match = ApiClient.getLeagueApiClient().getMatch(mRegion, matchlist.get(i).getMatchId(), mContext.getResources().getString(R.string.api_key));
                    match = realm.copyToRealm(match);
                }
                summoner.getMatches().add(match);
                Log.v(MainActivity.DEBUG_INFO,"Added Match "+match.getMatchId());
            }
            realm.commitTransaction();
        } catch (Exception e){
            Log.e(MainActivity.DEBUG_INFO,e.getMessage());
            realm.cancelTransaction();
            finish = false;
        }finally {
            if (realm != null) {
                realm.close();
            }
        }
        return finish;
    }
    @Override
    protected void onPostExecute(Boolean finish) {
        super.onPostExecute(finish);
        if(mListener != null)
            mListener.processFinish(finish);
    }
}
