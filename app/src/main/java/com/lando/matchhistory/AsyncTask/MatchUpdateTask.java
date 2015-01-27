package com.lando.matchhistory.AsyncTask;

import android.content.Context;
import android.util.Log;

import com.lando.matchhistory.ApiClient.ApiClient;
import com.lando.matchhistory.MainActivity;
import com.lando.matchhistory.Models.Match;
import com.lando.matchhistory.Models.Summoner;
import com.lando.matchhistory.R;

import java.util.List;
import java.util.Map;

import io.realm.Realm;


/**
 * Created by Lando on 1/2/2015.
 */
public class MatchUpdateTask extends BaseTask<Void,Void,Void>{

    private String mSummonerName;
    private final String mRegion;
    private final Context mContext;

    public MatchUpdateTask(Context context, String summoner, String region){
        this.mContext = context;
        this.mSummonerName = summoner;
        this.mRegion = region;
    }
    /**
     * Try to improve this method
     */
    @Override
    protected Void doInBackground(Void... params) {
        Realm realm = null;

        try {
            realm = Realm.getInstance(mContext);
            realm.beginTransaction();
            Summoner summoner = realm.where(Summoner.class).equalTo("name",mSummonerName,false).findFirst();
            Map<String,List<Match>> matches = ApiClient.getLeagueApiClient().getMatches(mRegion, summoner.getId(), 0,5,mContext.getResources().getString(R.string.api_key));
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
        }finally {
            if (realm != null) {
                realm.close();
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(mListener != null)
            mListener.processFinish();
    }
}
