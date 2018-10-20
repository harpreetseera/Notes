package com.example.untrodden.notes.modal;

public class NoteListData {
    String noteTitle;
    String noteDescription;
    int noteNo;

    public NoteListData(String noteTitle, String noteDescription, int noteNo) {
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
        this.noteNo = noteNo;
    }

    public String getNoteTitle() { return noteTitle; }

    public void setNoteTitle(String noteTitle) { this.noteTitle = noteTitle; }

    public String getNoteDescription() { return noteDescription; }

    public void setNoteDescription(String noteDescription) { this.noteDescription = noteDescription; }

    public int getNoteNo() { return noteNo; }

    public void setNoteNo(int noteNo) { this.noteNo = noteNo; }

  }
