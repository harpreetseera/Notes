package com.example.untrodden.notes.roomPersistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "notes_table")
public class NotesTable {
    @ColumnInfo(name = "note_title")
    String noteTitle;


    @ColumnInfo(name = "note_detail")
    String notetDetail;


    @NonNull
    @PrimaryKey(autoGenerate = true)
    int noteNo;

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNotetDetail() {
        return notetDetail;
    }

    public void setNotetDetail(String notetDetail) {
        this.notetDetail = notetDetail;
    }

    @NonNull
    public int getNoteNo() {
        return noteNo;
    }

    public void setNoteNo(@NonNull int noteNo) {
        this.noteNo = noteNo;
    }

}
