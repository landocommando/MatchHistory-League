package com.lando.matchhistory.AsyncTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.os.Environment;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lando.matchhistory.ApiClient.ApiClient;
import com.lando.matchhistory.Models.Champion;

import com.lando.matchhistory.Models.Image;
import com.lando.matchhistory.Models.Item;

import com.lando.matchhistory.Models.SummonerSpell;
import com.lando.matchhistory.Models.Version;

import com.lando.matchhistory.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;


/**
 * Created by Lando on 12/2/2014.
 */

public class VersionUpdateTask extends BaseTask<Void,Void,Void>{

    private String mRegion;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    public VersionUpdateTask(Context context, String region){
        this.mContext = context;
        this.mRegion = region;
        mSharedPreferences = context.getSharedPreferences("LeagueVersion", Context.MODE_PRIVATE);

    }
    @Override
    protected Void doInBackground(Void... params) {
        Realm realm = null;
        try{
            realm = Realm.getInstance(mContext);
            realm.beginTransaction();
            realm.allObjects(Champion.class).clear();
            realm.allObjects(Item.class).clear();
            realm.allObjects(SummonerSpell.class).clear();
            realm.allObjects(Image.class).clear();

            getVersion();
            JsonObject championData = ApiClient.getLeagueApiClient().getChampionListBeta(mRegion, "true", "image", mContext.getResources().getString(R.string.api_key));
            JsonObject itemData = ApiClient.getLeagueApiClient().getItemList(mRegion, "image", mContext.getResources().getString(R.string.api_key));
            JsonObject summonerSpellData = ApiClient.getLeagueApiClient().getSummonerSpells(mRegion, "image", mContext.getResources().getString(R.string.api_key));

            processData(championData,realm,0);
            processData(itemData,realm,1);
            processData(summonerSpellData,realm,2);

            realm.commitTransaction();
        } catch (JSONException e) {
            e.printStackTrace();
            realm.cancelTransaction();
        } catch (IOException e) {
            e.printStackTrace();
            realm.cancelTransaction();
        } finally {
            if(realm != null){
                realm.close();
            }
        }
        return null;
    }

    private <T> void processData(JsonObject jsonData,Realm realm,int value) throws JSONException, IOException {
        jsonData = jsonData.getAsJsonObject("data");


        Set<Map.Entry<String,JsonElement>> set = jsonData.entrySet();
        Iterator<Map.Entry<String,JsonElement>> i = set.iterator();

        while(i.hasNext()){
            String object = i.next().getValue().getAsJsonObject().toString();
            Bitmap b = null;
            String filename = "";
            switch (value){
                case 0:
                    Champion c = realm.createObjectFromJson(Champion.class,object);
                    filename = c.getImage().getFull();
                    b = Picasso.with(mContext).load(ApiClient.getDataDragonAPIClient().getCharacterImage(mSharedPreferences.getString("Champ", ""), filename)).get();
                    break;
                case 1:
                    Item item = realm.createObjectFromJson(Item.class,object);
                    filename = item.getImage().getFull();
                    b = Picasso.with(mContext).load(ApiClient.getDataDragonAPIClient().getItemImage(mSharedPreferences.getString("Item",""),filename)).get();
                    break;
                case 2:
                    SummonerSpell s = realm.createObjectFromJson(SummonerSpell.class,object);
                    filename = s.getImage().getFull();
                    b = Picasso.with(mContext).load(ApiClient.getDataDragonAPIClient().getSummonerSpellImage(mSharedPreferences.getString("Summoner",""),filename)).get();
                    break;
            }
            saveImage(b,filename);
        }

    }

    private void saveImage(Bitmap b,String filename) throws IOException{
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/LolMatches/images",filename);
        if(!file.exists()){
            file.mkdirs();
            file.delete();
        }
        else if(file.exists() && file.isDirectory()){
            file.delete();
            file.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        b.compress(Bitmap.CompressFormat.PNG,75,outputStream);
        b.recycle();
        outputStream.close();
    }
    private void getVersion(){
        Version v = ApiClient.getDataDragonApiClient().getVersion(mRegion);
        //todo change strings to hard coded
        mSharedPreferences.edit()
                .putString("Champ",v.getVerison().getChampion())
                .putString("Item",v.getVerison().getItem())
                .putString("Language",v.getVerison().getLanguage())
                .putString("ProfileIcon",v.getVerison().getProfileicon())
                .putString("Mastery",v.getVerison().getMastery())
                .putString("Rune",v.getVerison().getRune())
                .putString("Summoner",v.getVerison().getSummoner())
                .putString("region",mRegion)
                .apply();

    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(mListener != null){
            mListener.processFinish();
        }
    }
}
