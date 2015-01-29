package com.lando.matchhistory.AsyncTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import android.os.Environment;
import android.util.Log;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lando.matchhistory.ApiClient.ApiClient;
import com.lando.matchhistory.Models.Champion;
import com.lando.matchhistory.Models.Image;
import com.lando.matchhistory.Models.Item;

import com.lando.matchhistory.Models.Mastery;
import com.lando.matchhistory.Models.ProfileIcon;
import com.lando.matchhistory.Models.Rune;
import com.lando.matchhistory.Models.Summoner;
import com.lando.matchhistory.Models.SummonerSpell;
import com.lando.matchhistory.Models.Version;

import com.lando.matchhistory.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmObject;


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
            Map<String,JsonObject> championData = ApiClient.getLeagueApiClient().getChampionListBeta(mRegion, "true", "image", mContext.getResources().getString(R.string.api_key));
            Map<String,JsonObject> itemData = ApiClient.getLeagueApiClient().getItemList(mRegion, "image", mContext.getResources().getString(R.string.api_key));
            Map<String,JsonObject> summonerSpellData = ApiClient.getLeagueApiClient().getSummonerSpells(mRegion, "image", mContext.getResources().getString(R.string.api_key));
            Map<String,JsonObject> profileIconData = ApiClient.getDataDragonApiClient().getProfileIcons(mSharedPreferences.getString(ProfileIcon.class.getSimpleName(),""),"en_US");
            processData(Champion.class,championData.get("data"), realm);
            processData(Item.class,itemData.get("data"),realm);
            processData(SummonerSpell.class,summonerSpellData.get("data"),realm);
            processData(ProfileIcon.class,profileIconData.get("data"),realm);

            realm.commitTransaction();
        } catch (JSONException e) {
            e.printStackTrace();
            realm.cancelTransaction();
        } catch (IOException e) {
            e.printStackTrace();
            realm.cancelTransaction();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            realm.cancelTransaction();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            realm.cancelTransaction();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            realm.cancelTransaction();
        } finally {
            if(realm != null){
                realm.close();
            }
        }
        return null;
    }

    /**
     *
     * @param clazz The class to create object of from
     * @param jsonObjects list of jsonobjects to be converted into class type clazz
     * @param realm
     * @param <T> must extends RealmObject
     * @throws JSONException
     * @throws IOException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private <T extends RealmObject> void processData(Class<T> clazz, JsonObject jsonObjects, Realm realm) throws JSONException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Iterator<Map.Entry<String,JsonElement>> iterator = jsonObjects.entrySet().iterator();

        while (iterator.hasNext()){
            String json = iterator.next().getValue().toString();
            T t = realm.createObjectFromJson(clazz,json);

            Image i = (Image) t.getClass().getMethod("getImage").invoke(t, null);

            String path = ApiClient.getDataDragonAPIClient().getImage(mSharedPreferences.getString(clazz.getSimpleName(),""),i.getFull(),clazz);

            Log.v(clazz.getSimpleName(), mSharedPreferences.getString(clazz.getSimpleName(),""));
            Log.v(clazz.getSimpleName(), path);
            Bitmap b = Picasso.with(mContext).load(path).get();
            saveImage(b,i.getFull());

        }
    }

    /**
     *
     * @param b bitmap to save to file
     * @param filename the name to give the image
     * @throws IOException
     */
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

    /**
     * Downloads the latest patch version and saves the data into shared preferences.
     */
    private void getVersion(){
        Version v = ApiClient.getDataDragonApiClient().getVersion(mRegion);
        //todo change strings to hard coded
        mSharedPreferences.edit()
                .putString(Champion.class.getSimpleName(),v.getVerison().getChampion())
                .putString(Item.class.getSimpleName(), v.getVerison().getItem())
                .putString("Language",v.getVerison().getLanguage())
                .putString(ProfileIcon.class.getSimpleName(),v.getVerison().getProfileicon())
                .putString(Mastery.class.getSimpleName(),v.getVerison().getMastery())
                .putString(Rune.class.getSimpleName(),v.getVerison().getRune())
                .putString(SummonerSpell.class.getSimpleName(), v.getVerison().getSummoner())
                .putString("region", mRegion)
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
