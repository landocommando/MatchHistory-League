package com.lando.matchhistory.ApiClient;


import android.util.Log;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lando.matchhistory.MainActivity;
import com.lando.matchhistory.Models.Champion;
import com.lando.matchhistory.Models.Image;
import com.lando.matchhistory.Models.Item;
import com.lando.matchhistory.Models.Match;
import com.lando.matchhistory.Models.ProfileIcon;
import com.lando.matchhistory.Models.Summoner;
import com.lando.matchhistory.Models.SummonerSpell;
import com.lando.matchhistory.Models.Version;

import java.util.List;
import java.util.Map;

import io.realm.RealmObject;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


/**
 * Created by Lando on 11/28/2014.
 */
public class ApiClient {

    private static LeagueApiInterface mLeagueApiService;
    private static DataDragonApi mDataDragonAPIService;
    private static DataDragonApiInterface mDataDragonApiService;

    public static LeagueApiInterface getLeagueApiClient(){
        if(mLeagueApiService == null){
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .create();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://na.api.pvp.net")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new RestAdapter.Log() {
                        @Override
                        public void log(String msg) {
                            Log.i(MainActivity.DEBUG_INFO, msg);
                        }
                    })
                    .setConverter(new GsonConverter(gson))
                    .build();
            mLeagueApiService = restAdapter.create(LeagueApiInterface.class);
        }
        return mLeagueApiService;
    }
    public static DataDragonApiInterface getDataDragonApiClient(){
        if(mDataDragonApiService == null){
            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .create();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://ddragon.leagueoflegends.com")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setLog(new RestAdapter.Log() {
                        @Override
                        public void log(String msg) {
                            Log.i(MainActivity.DEBUG_INFO, msg);
                        }
                    })
                    .setConverter(new GsonConverter(gson))
                    .build();
            mDataDragonApiService = restAdapter.create(DataDragonApiInterface.class);
        }
        return mDataDragonApiService;
    }
    public static DataDragonApi getDataDragonAPIClient(){
        if(mDataDragonAPIService == null){

            mDataDragonAPIService = new DataDragonApi();
        }
        return mDataDragonAPIService;
    }
    public static interface LeagueApiInterface{

        @GET("/api/lol/{region}/v2.2/match/{matchId}")
        Match getMatch(@Path("region") String region, @Path("matchId") long id, @Query("api_key") String key);
        @GET("/api/lol/{region}/v2.2/matchhistory/{summonerId}")
        List<Match> getMatches(@Path("region") String region, @Path("summonerId") long id, @Query("api_key") String key);
        @GET("/api/lol/{region}/v2.2/matchhistory/{summonerId}")
        Map<String,List<Match>> getMatches(@Path("region") String region, @Path("summonerId") long id, @Query("beginIndex") int beginindex,@Query("endIndex") int endindex,@Query("api_key") String key);
        @GET("/api/lol/{region}/v1.4/summoner/by-name/{summonerNames}")
        Map<String,Summoner> getSummoner(@Path("region") String region, @Path("summonerNames") String summonerName,@Query("api_key") String key);
        @GET("/api/lol/static-data/{region}/v1.2/champion")
        Map<String,JsonObject> getChampionListBeta(@Path("region") String region,@Query("dataById") String databyid,@Query("champData") String data, @Query("api_key") String key);
        @GET("/api/lol/static-data/{region}/v1.2/item")
        Map<String,JsonObject> getItemList(@Path("region") String region,@Query("itemListData") String itemListDat, @Query("api_key") String key);
        @GET("/api/lol/static-data/{region}/v1.2/summoner-spell")
        Map<String,JsonObject> getSummonerSpells(@Path("region") String region,@Query("spellData") String spellData, @Query("api_key") String key);


    }

    public static class DataDragonApi{
        private String basePath = "http://ddragon.leagueoflegends.com/cdn/";

        public <T extends RealmObject> String getImage(String patch, String name,Class<T> clazz){
            if(clazz.getSimpleName().compareTo(Champion.class.getSimpleName())==0){
                return getCharacterImage(patch, name);
            }else if(clazz.getSimpleName().compareTo(Item.class.getSimpleName())==0){
                return getItemImage(patch,name);
            }else if(clazz.getSimpleName().compareTo(SummonerSpell.class.getSimpleName())==0){
                return getSummonerSpellImage(patch,name);
            }else if(clazz.getSimpleName().compareTo(ProfileIcon.class.getSimpleName())==0){
                return getProfileImage(patch,name);
            }
            return "";
        }
        private String getCharacterImage(String patch, String name){
            return basePath+patch+"/img/champion/"+name;
        }
        private String getItemImage(String patch, String id){
            return basePath+patch+"/img/item/"+id;
        }
        private String getProfileImage(String patch,String id){
            return basePath+patch+"/img/profileicon/"+id;
        }
        private String getSummonerSpellImage(String patch, String id){
            return basePath+patch+"/img/spell/"+id;
        }
    }
    public static interface DataDragonApiInterface{
        @GET("/realms/{region}.json")
        Version getVersion(@Path("region") String region);
        @GET("/cdn/{patch}/data/{region}/profileicon.json")
        Map<String,JsonObject> getProfileIcons(@Path("patch") String patch,@Path("region") String region);
    }
}
