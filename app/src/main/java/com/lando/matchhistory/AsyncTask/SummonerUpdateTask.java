package com.lando.matchhistory.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.lando.matchhistory.ApiClient.ApiClient;
import com.lando.matchhistory.MainActivity;
import com.lando.matchhistory.Models.Summoner;
import com.lando.matchhistory.R;

import java.util.Map;

import io.realm.Realm;


public class SummonerUpdateTask extends AsyncTask<Void,Void,Void>{

    private UpdateSummonerListener mListener;
    private final String mSummonerName;
    private final String mRegion;
    private final Context mContext;
    public interface UpdateSummonerListener{
        void OnProcessComplete(String name);
    }
    public void setListener(UpdateSummonerListener listener){
        mListener = listener;
    }

    public SummonerUpdateTask(Context context, String name, String region, UpdateSummonerListener mListener) {
        this.mContext = context;
        this.mSummonerName = name;
        this.mRegion = region;
        this.mListener = mListener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Realm realm = null;
        Summoner summoner = null;
        Summoner oldsummoner = null;

        try{
            realm = Realm.getInstance(mContext);
            realm.beginTransaction();
            Map<String,Summoner> summonerMap = ApiClient.getLeagueApiClient().getSummoner(mRegion, mSummonerName, mContext.getResources().getString(R.string.api_key));

            summoner = summonerMap.get(mSummonerName);
            if((oldsummoner = realm.where(Summoner.class).equalTo("id",summoner.getId()).findFirst())!=null){
                summoner.setMatches(oldsummoner.getMatches());
                oldsummoner.removeFromRealm();
            }
            summoner = realm.copyToRealm(summoner);
            Log.v(MainActivity.DEBUG_INFO, summoner.getName()+" was created");
            realm.commitTransaction();

        }catch (Exception e){
            Log.v(MainActivity.DEBUG_INFO,e.getMessage());
            realm.cancelTransaction();
        }finally {
            if(realm != null)
                realm.close();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        if(mListener != null){
            mListener.OnProcessComplete(mSummonerName);
        }
    }
}
