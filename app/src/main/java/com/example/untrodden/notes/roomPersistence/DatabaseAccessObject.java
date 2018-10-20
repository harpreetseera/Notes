package com.example.untrodden.notes.roomPersistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DatabaseAccessObject {

    @Insert
    public void addNoteData(NotesTable notesTable);

    @Query("DELETE FROM notes_table")
    public void deleteRecords();

    @Query("DELETE FROM notes_table WHERE noteNo = :noteNo")
    public void deleteRecordForNoteNo(int noteNo);

//    @Query( "SELECT * FROM notes_table WHERE noteNo = :noteNo")
//    public List<NotesTable> getNotelist(String noteNo);

    @Query( "SELECT * FROM notes_table")
    public List<NotesTable> getNotelist();

    @Query( "SELECT * FROM notes_table WHERE note_detail LIKE :key")
    public List<NotesTable> searchNotes(String key);

    @Query("UPDATE notes_table SET note_detail = :noteDetail  WHERE noteNo = :noteNo")
    public void updateNoteDetail(int noteNo , String noteDetail);

}
