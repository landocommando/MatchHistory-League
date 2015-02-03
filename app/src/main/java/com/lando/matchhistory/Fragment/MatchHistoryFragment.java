package com.lando.matchhistory.Fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.lando.matchhistory.Adapter.MatchAdapter;
import com.lando.matchhistory.AsyncTask.MatchUpdateTask;
import com.lando.matchhistory.Models.Summoner;
import com.lando.matchhistory.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MatchHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchHistoryFragment extends Fragment implements ObservableScrollViewCallbacks,MatchUpdateTask.MatchUpdateResponse {
    // TODO: Rename parameter arguments, choose names that match
    private static final String SUMMONER_ID = "id";

    private MatchAdapter mMatchAdapter;
    private long mPlayerId;
    private MatchUpdateTask mTask;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id summoner id
     * @return A new instance of fragment MatchHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchHistoryFragment newInstance(long id) {
        MatchHistoryFragment fragment = new MatchHistoryFragment();
        Bundle args = new Bundle();
        args.putLong(SUMMONER_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public MatchHistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlayerId = getArguments().getLong(SUMMONER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSwipeRefreshLayout = (SwipeRefreshLayout)inflater.inflate(R.layout.fragment_match_history, container, false);
        ObservableRecyclerView rc = (ObservableRecyclerView) mSwipeRefreshLayout.findViewById(R.id.history_recycler_view);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());

        mMatchAdapter = new MatchAdapter(getActivity(),mPlayerId);

        rc.setHasFixedSize(true);
        rc.setLayoutManager(layoutParams);
        rc.setAdapter(mMatchAdapter);
        rc.setScrollViewCallbacks(this);

        //a fix to allow swiperefreshlayout to refresh
        rc.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mTask = new MatchUpdateTask(getActivity(), mPlayerId, "na");
                mTask.setListener(MatchHistoryFragment.this);
                mTask.execute((Void) null);
            }
        });
        return mSwipeRefreshLayout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString()
                  //  + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void processFinish(boolean finished) {
        mSwipeRefreshLayout.setRefreshing(false);
        mTask.setListener(null);
        mTask = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab =((ActionBarActivity) getActivity()).getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }

        }
    }
}
