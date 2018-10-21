package com.example.untrodden.notes.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.untrodden.notes.R;
import com.example.untrodden.notes.Utils.Constants;
import com.example.untrodden.notes.adapters.NoteListAdapter;
import com.example.untrodden.notes.itemTouchListener.RecyclerTouchListener;
import com.example.untrodden.notes.modal.NoteListData;
import com.example.untrodden.notes.roomPersistence.NotesDatabase;
import com.example.untrodden.notes.roomPersistence.NotesTable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<NoteListData> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteListAdapter noteListAdapter;
    public static NotesDatabase notesDatabase;
    SharedPreferences preferences;


    @Override
    protected void onResume() {
        super.onResume();
        addNoteData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             Toolbar mToolbar =  (Toolbar) findViewById(R.id.toolbar);
             setActionBar(mToolbar);
        }

        createNotificationChannel();


        FloatingActionButton fab = findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        notesDatabase = Room.databaseBuilder(getApplicationContext(),NotesDatabase.class,"notes_db").build();

        noteListAdapter = new NoteListAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(noteListAdapter);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


               fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          Intent i = new Intent(MainActivity.this,NoteActivity.class);
                i.putExtra("position","-1");
                startActivity(i);
            }
        });

        addNoteData();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {


            @Override
            public void onClick(View view, int position) {
                NoteListData noteListData = noteList.get(position);
                Toast.makeText(getApplicationContext(), noteListData.getNoteTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,NoteActivity.class);
                i.putExtra("noteTitle",noteListData.getNoteTitle());
                i.putExtra("noteDetail",noteListData.getNoteDescription());
                i.putExtra("noteNo",noteListData.getNoteNo());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_search, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView mSearchView = (android.support.v7.widget.SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search note");

        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Happy Birthday", "MainActivityonQueryTextSubmit: " +query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                new AsyncCaller(newText).execute();
                Log.d("Happy Birthday", "MainActivityonQueryTextChange: "+newText);
                return false;
            }
        });


        return true;

    }

    private void addNoteData() {

        new AsyncCaller(null).execute();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        List<NotesTable> list;
        String keyword;

        public AsyncCaller(String keyword){
            this.keyword = keyword;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(keyword==null||keyword==""){
                list = notesDatabase.getDao().getNotelist();
               }
                else{
                list = notesDatabase.getDao().searchNotes("%"+keyword+"%");
            }
            noteList.clear();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            preferences.edit().putString("search_text",keyword).apply();
            for(NotesTable nt:list){
                noteList.add(new NoteListData(nt.getNoteTitle(),nt.getNotetDetail(),nt.getNoteNo()));
            }
            noteListAdapter.notifyDataSetChanged();
        }

    }



}

