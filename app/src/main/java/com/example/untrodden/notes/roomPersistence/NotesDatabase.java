package com.example.untrodden.notes.roomPersistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {NotesTable.class},version = 1,exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    public abstract DatabaseAccessObject getDao();

}
