package com.example.untrodden.notes.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.untrodden.notes.R;

public class SearchActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Toolbar mToolbar =  (Toolbar) findViewById(R.id.toolbar);
        TextView mEmptyView = (TextView) findViewById(R.id.emptyView);
        setActionBar(mToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_search, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView mSearchView = (android.support.v7.widget.SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search nawa note");

        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("HappyBirthday", "onQueryTextSubmit: " +query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                Log.d("Happy Birthday", "onQueryTextChange: "+newText);
                return false;
            }
        });


        return true;

    }
}
