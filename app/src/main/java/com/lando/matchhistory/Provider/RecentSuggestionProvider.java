package com.lando.matchhistory.Provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by lcheruka on 1/6/2015.
 */
public class RecentSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.lando.lolmatches.Provider.RecentSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public RecentSuggestionProvider() {
        setupSuggestions(AUTHORITY,MODE);
    }
}
