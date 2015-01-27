package com.lando.matchhistory.AsyncTask;

import android.os.AsyncTask;

/**
 * Created by Lando on 1/2/2015.
 */
public abstract class BaseTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    protected UpdateResponse mListener=null;

    public interface UpdateResponse {
        void processFinish();
    }
    public void setListener(UpdateResponse listener){
        mListener = listener;
    }
}
