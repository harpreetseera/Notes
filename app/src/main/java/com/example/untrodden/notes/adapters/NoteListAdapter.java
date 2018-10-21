package com.example.untrodden.notes.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.untrodden.notes.R;
import com.example.untrodden.notes.modal.NoteListData;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.MyViewHolder> {

    private List<NoteListData> noteList;
    SharedPreferences preferences;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView noteTitle, noteDescription;

        public MyViewHolder(View view) {
            super(view);
            noteTitle = (TextView) view.findViewById(R.id.tv1);
            noteDescription = (TextView) view.findViewById(R.id.tv2);
            context = view.getContext();
        }
    }


    public NoteListAdapter(List<NoteListData> noteList) {
        this.noteList = noteList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item_layout, parent, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NoteListData noteListData = noteList.get(position);
        String desc = noteListData.getNoteDescription();
        String title = noteListData.getNoteTitle();
        String searchText  = preferences.getString("search_text",null);
        Log.d(TAG, "onBindViewHolder: search text is "+searchText);
        if(searchText!=null) {
            if (searchText.length() > 0) {

                if (searchText != "") {
                    SpannableStringBuilder sb = new SpannableStringBuilder(desc);
                    SpannableStringBuilder sb2 = new SpannableStringBuilder(title);
                    Pattern word = Pattern.compile(searchText.toLowerCase());
                    Matcher match = word.matcher(desc.toLowerCase());
                    Matcher match2 = word.matcher(title.toLowerCase());
                    while (match.find()) {
                        ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.WHITE});
                        TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                        sb.setSpan(highlightSpan, match.start(), match.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    while (match2.find()) {
                        ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.WHITE});
                        TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                        sb2.setSpan(highlightSpan, match2.start(), match2.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    holder.noteDescription.setText(sb);
                    holder.noteTitle.setText(sb2);
                    } else {
                    holder.noteDescription.setText(Html.fromHtml(desc));
                    holder.noteTitle.setText(Html.fromHtml(title));
                }
            }
        } else{
            holder.noteDescription.setText(noteListData.getNoteDescription());
            holder.noteTitle.setText(noteListData.getNoteTitle());
        }
    }

    private String setHighlightedText(String searchText,String textToBeSearched) {
        String highlightedText=null;

        if (searchText != null) {
            if (searchText.length() > 0) {


                if (searchText != "") {
                    SpannableStringBuilder sb = new SpannableStringBuilder(textToBeSearched);
                    Pattern word = Pattern.compile(searchText.toLowerCase());
                    Matcher match = word.matcher(textToBeSearched.toLowerCase());

                    while (match.find()) {
                        ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.WHITE});
                        TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                        sb.setSpan(highlightSpan, match.start(), match.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    //       holder.noteDescription.setText(sb);
                    highlightedText= sb.toString();


                } else {
                    //    holder.noteDescription.setText(Html.fromHtml(desc));
                    highlightedText = Html.fromHtml(textToBeSearched).toString();
                }

            }
        }

        return highlightedText;
    }


    @Override
    public int getItemCount() {
        return noteList.size();
    }

}