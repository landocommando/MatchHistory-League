package com.lando.matchhistory.Adapter;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lando.matchhistory.MainActivity;
import com.lando.matchhistory.Models.Participant;
import com.lando.matchhistory.Models.ParticipantIdentity;
import com.lando.matchhistory.Models.Summoner;
import com.lando.matchhistory.R;
import com.lando.matchhistory.Models.Champion;
import com.lando.matchhistory.Models.Item;
import com.lando.matchhistory.Models.Match;
import com.lando.matchhistory.Models.Stats;
import com.lando.matchhistory.Models.SummonerSpell;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView champImage;
        public ImageView[] itemImage;
        public ImageView[] spellImage;
        public TextView matchStatusTextView;
        public TextView matchDurationTextView;
        public TextView matchCreationTextView;
        public TextView matchTypeTextView;

        public TextView participantLevelTextView;
        public TextView participantKillsTextView;
        public TextView participantDeathsTextView;
        public TextView participantAssistsTextView;
        public TextView participantKDATextView;
        public TextView participantCreepScoreTextView;
        public TextView participantCreepScoreRateTextView;


        public ViewHolder(View itemView) {
            super(itemView);

            champImage = (ImageView)itemView.findViewById(R.id.ivChamp);
            itemImage = new ImageView[7];
            spellImage = new ImageView[2];
            itemImage[0] = (ImageView)itemView.findViewById(R.id.item1);
            itemImage[1] = (ImageView)itemView.findViewById(R.id.item2);
            itemImage[2] = (ImageView)itemView.findViewById(R.id.item3);
            itemImage[3] = (ImageView)itemView.findViewById(R.id.item4);
            itemImage[4] = (ImageView)itemView.findViewById(R.id.item5);
            itemImage[5] = (ImageView)itemView.findViewById(R.id.item6);
            itemImage[6] = (ImageView)itemView.findViewById(R.id.item7);

            spellImage[0] = (ImageView)itemView.findViewById(R.id.spell1);
            spellImage[1] = (ImageView)itemView.findViewById(R.id.spell2);

            matchStatusTextView = (TextView)itemView.findViewById(R.id.tvWin);
            matchDurationTextView = (TextView)itemView.findViewById(R.id.tvDuration);
            matchCreationTextView = (TextView)itemView.findViewById(R.id.tvCreation);
            matchTypeTextView = (TextView)itemView.findViewById(R.id.tvGameType);

            participantLevelTextView = (TextView)itemView.findViewById(R.id.tvLevel);
            participantKillsTextView = (TextView)itemView.findViewById(R.id.tvKills);
            participantDeathsTextView =(TextView)itemView.findViewById(R.id.tvDeaths);
            participantAssistsTextView =(TextView)itemView.findViewById(R.id.tvAssists);
            participantKDATextView = (TextView)itemView.findViewById(R.id.tvKillDeathRate);
            participantCreepScoreTextView = (TextView)itemView.findViewById(R.id.tvCreepScore);
            participantCreepScoreRateTextView = (TextView)itemView.findViewById(R.id.tvCreepScoreRate);
        }

    }
    public static interface OnItemClickListener{
        public void onItemClick(Match match);
    }
    private OnItemClickListener listener;
    private long mPlayerID;
    private Context mContext;
    private SimpleDateFormat mSimpleDateFormat;
    public RealmResults<Match> mData;
    private Realm mRealm;
    public MatchAdapter(Context context, long id){
        mContext = context;
        mSimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        mPlayerID = id;
        mRealm = Realm.getInstance(mContext);
        mRealm.setAutoRefresh(true);
        mRealm.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                notifyDataSetChanged();
            }
        });
        mData = mRealm.where(Summoner.class).equalTo("id",id).findFirst().getMatches().where().findAll();
        listener = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.matches_list_item2,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Match data = getItem(i);
        if(data == null)
            return;

        Participant p = getParticipant(data.getParticipants(),data.getParticipantIdentities());
        Stats s = p.getStats();
        Champion c = mRealm.where(Champion.class).equalTo("id",p.getChampionId()).findFirst();

        loadImage(viewHolder.champImage, c.getImage().getFull());

        if(s.getItem0()>0)
            loadImage(viewHolder.itemImage[0], mRealm.where(Item.class).equalTo("id", s.getItem0()).findFirst().getImage().getFull());
        if(s.getItem1()>0)
            loadImage(viewHolder.itemImage[1], mRealm.where(Item.class).equalTo("id", s.getItem1()).findFirst().getImage().getFull());
        if(s.getItem2()>0)
            loadImage(viewHolder.itemImage[2], mRealm.where(Item.class).equalTo("id", s.getItem2()).findFirst().getImage().getFull());
        if(s.getItem6()>0)
            loadImage(viewHolder.itemImage[3], mRealm.where(Item.class).equalTo("id", s.getItem6()).findFirst().getImage().getFull());
        if(s.getItem3()>0)
            loadImage(viewHolder.itemImage[4], mRealm.where(Item.class).equalTo("id", s.getItem3()).findFirst().getImage().getFull());
        if(s.getItem4()>0)
            loadImage(viewHolder.itemImage[5], mRealm.where(Item.class).equalTo("id", s.getItem4()).findFirst().getImage().getFull());
        if(s.getItem5()>0)
            loadImage(viewHolder.itemImage[6], mRealm.where(Item.class).equalTo("id",s.getItem5()).findFirst().getImage().getFull());


        loadImage(viewHolder.spellImage[0],mRealm.where(SummonerSpell.class).equalTo("id",p.getSpell1Id()).findFirst().getImage().getFull());
        loadImage(viewHolder.spellImage[1],mRealm.where(SummonerSpell.class).equalTo("id", p.getSpell2Id()).findFirst().getImage().getFull());


        viewHolder.matchStatusTextView.setText(s.isWinner() ? "Victory" : "Defeat");
        viewHolder.matchDurationTextView.setText(data.getMatchDuration()/100 + ":"+data.getMatchDuration()%100);
        Date date = new Date((long)data.getMatchCreation());
        viewHolder.matchCreationTextView.setText(mSimpleDateFormat.format(date.getTime()));
        viewHolder.matchTypeTextView.setText(data.getQueueType());

        viewHolder.participantLevelTextView.setText(s.getChampLevel()+"");
        viewHolder.participantKillsTextView.setText(s.getKills()+"");
        viewHolder.participantDeathsTextView.setText(s.getDeaths()+"");
        viewHolder.participantAssistsTextView.setText(s.getAssists()+"");
        viewHolder.participantKDATextView.setText(Math.round((double)s.getKills()/(double)s.getDeaths()*100)/100+"");
        viewHolder.participantCreepScoreTextView.setText(s.getMinionsKilled()+s.getNeutralMinionsKilled()+"");


    }

    private Participant getParticipant(RealmList<Participant> p, RealmList<ParticipantIdentity> pi){
        Log.v("Player ID",mPlayerID+"");
        Log.v(MainActivity.DEBUG_INFO,"|");
        Participant participant = null;
        for(int i =0;i < pi.size();i++){
            Log.v(MainActivity.DEBUG_INFO,"Index"+pi.get(i).getParticipantId()+": Summoner "+pi.get(i).getPlayer().getSummonerId());
            if(pi.get(i).getPlayer().getSummonerId() == mPlayerID){
                for(int j = 0; j < p.size();j++){
                    if(p.get(j).getParticipantId() == pi.get(i).getParticipantId()){
                        participant = p.get(j);
                    }
                }
                break;
            }
        }
        return participant;
    }

    private void loadImage(ImageView i, String filename) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/LolMatches/images", filename);
        Picasso.with(mContext).load(file).error(R.drawable.ic_launcher).into(i);
    }

    @Override
    public int getItemCount() {
        return mData == null? 0:mData.size();
    }

    public Match getItem(int position){
        return mData == null? null:mData.get(position);
    }



}