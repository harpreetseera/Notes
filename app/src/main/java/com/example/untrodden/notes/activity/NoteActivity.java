package com.example.untrodden.notes.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.untrodden.notes.WorkManager.NotifyWorker;
import com.example.untrodden.notes.broadcastReceiver.AlarmReceiver;
import com.example.untrodden.notes.R;
import com.example.untrodden.notes.roomPersistence.NotesTable;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class NoteActivity extends AppCompatActivity {
Button btnDone;
EditText etNoteDetail,etNoteTitle;
int noteNo;
private int mYear, mMonth, mDay, mHour, mMinute;
public final String WORK_TAG  = "NOTIFICATION_WORK_TAG";
public final String DB_EVENT_TAG  = "NOTIFICATION_EVENT_TAG";
public final int NOTIFICATION_ID  = 001;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        String noteTitle = getIntent().getStringExtra("noteTitle");
        String noteDetail = getIntent().getStringExtra("noteDetail");
        noteNo = getIntent().getIntExtra("noteNo",-1);
        btnDone = findViewById(R.id.btn_done);
        etNoteDetail = findViewById(R.id.et);
        etNoteTitle = findViewById(R.id.et_title);
        etNoteTitle.setText(noteTitle);
        etNoteDetail.setText(noteDetail);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNoteToDB();
//                if(!etNoteDetail.getText().toString().isEmpty()){
//                    NotesTable notesTable = new NotesTable();
//                    notesTable.setNoteTitle(etNoteTitle.getText().toString());
//                    notesTable.setNotetDetail(etNoteDetail.getText().toString());
//                    new AsyncCaller(notesTable).execute();
//
//                }
            }
        });
    }

    private void addNoteToDB() {
        if(!etNoteDetail.getText().toString().isEmpty()){
            NotesTable notesTable = new NotesTable();
            notesTable.setNoteTitle(etNoteTitle.getText().toString());
            notesTable.setNotetDetail(etNoteDetail.getText().toString());
            new AsyncCaller(notesTable).execute();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_alarm:{

//                Toast.makeText(getApplicationContext(),"ALARM",Toast.LENGTH_SHORT).show();
                //TODO: Set Alarm functionality

                getDateForAlarm();
                
                

                break;

            }
            case R.id.action_delete:{
                new AsyncCallerDeleteParticuarNote(noteNo).execute();
                break;
            }
            case R.id.action_done:{
                //TODO: Set done functionality

                addNoteToDB();
//                Toast.makeText(getApplicationContext(),"DONE",Toast.LENGTH_SHORT).show();
                break;

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDateForAlarm() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

//                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        getTimeForAlarm();


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void getTimeForAlarm(){

    final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
        new TimePickerDialog.OnTimeSetListener() {

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay,
                      int minute) {
        c.set(mYear,mMonth,mDay,hourOfDay,minute);


//        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(NoteActivity.this, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//        // cal.add(Calendar.SECOND, 5);
//  //      alarmMgr.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
//
//        OneTimeWorkRequest simpleRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .build();
//        WorkManager.getInstance().enqueue(simpleRequest);
//
//
//      store DBEventID to pass it to the PendingIntent and open the appropriate event page on notification click
        Data inputData = new Data.Builder().putInt(DB_EVENT_TAG, NOTIFICATION_ID).build();
//      we then retrieve it inside the NotifyWorker with:
//      final int DBEventID = getInputData().getInt(DBEventIDTag, ERROR_VALUE);

        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotifyWorker.class)
                .setInitialDelay(5000, TimeUnit.MILLISECONDS)
                .setInputData(inputData)
                .addTag(WORK_TAG)
                .build();
        WorkManager.getInstance().enqueue(notificationWork);


    }
            }, mHour, mMinute, false);
            timePickerDialog.show();
        }



    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        NotesTable notesTable;
        public AsyncCaller(NotesTable notesTable){
            this.notesTable = notesTable;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            MainActivity.notesDatabase.getDao().addNoteData(notesTable);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),"note added ",Toast.LENGTH_SHORT).show();
            //this method will be running on UI thread
        }

    }



    private class AsyncCallerDeleteParticuarNote extends AsyncTask<Void, Void, Void>
    {
        int  noteNoInner;
        public AsyncCallerDeleteParticuarNote(int noteNo){
            noteNoInner = noteNo;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            MainActivity.notesDatabase.getDao().deleteRecordForNoteNo(noteNoInner);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(),"Note Deleted",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(NoteActivity.this,MainActivity.class);
            startActivity(i);
            //this method will be running on UI thread
        }

    }

}
