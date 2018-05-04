package com.notifellow.su.notifellow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class NotesFragment extends Fragment {

    private static final String TASKS_KEY = "com.notifellow.su.notifellow.tasks_key";

    private FloatingActionButton fab;
    private ListView noteListView;
    static List<Note> noteList;
    static NoteAdapter noteAdapter;
    private SharedPreferences shared;

// TODO: IMPLEMENT NOTES ACTIVITY AS FRAGMENT HERE
// TODO: INSTEAD OF ADD NEW NOTES BUTTON, USE THE fab I PROVIDED YOU

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        fab = view.findViewById(R.id.fabAddNote);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), "Add Note!", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(NotesFragment.this.getActivity(), NoteCreateActivity.class);
                    myIntent.putExtra("email",shared.getString("email", "null"));
                    NotesFragment.this.startActivity(myIntent);
                }
            });
        }

        if (savedInstanceState == null) {
            NotesListActivity.schema = new NotesDBSchema(getContext());
            noteList = new ArrayList<>();
            noteList.add(new Note(null, null, null,null,null));
        } else {
            noteList = savedInstanceState.getParcelableArrayList(TASKS_KEY);
        }

        noteListView = view.findViewById(R.id.listView_Notes);
        int rowCount = 0;
        noteList = new ArrayList<>();

        Cursor allNotes = NotesListActivity.schema.getData();

        int idCol = allNotes.getColumnIndex("ID");
        int titleCol = allNotes.getColumnIndex("title");
        int noteCol = allNotes.getColumnIndex("note");
        int imageCol = allNotes.getColumnIndex("image_path");
        int emailCol = allNotes.getColumnIndex("email");

        allNotes.moveToFirst();
        rowCount = allNotes.getCount();
        int tempCount = rowCount;

        shared = getContext().getSharedPreferences("shared", MODE_PRIVATE);
        String email = shared.getString("email", "null");//GET EMAIL FROM SHARED

        if (allNotes != null && (tempCount > 0)) {

            do {
                if (email.equals(allNotes.getString(emailCol))) {
                    String id = allNotes.getString(idCol);
                    String title = allNotes.getString(titleCol);
                    String note = allNotes.getString(noteCol);
                    String image = allNotes.getString(imageCol);

//                    noteList.add(new Task(id, title, startDate + "\t\t" + startTime, endTime + "\t\t" + endDate, remindTime + "\t\t" + remindDate, location, wifi, note));
                    noteList.add(new Note(id, title, note, image, email));
                }
                tempCount--;
            } while (allNotes.moveToNext());
        }

        Collections.sort(noteList);

        noteAdapter = new NoteAdapter(getActivity(), noteList);
        noteListView.setAdapter(noteAdapter);

        return view;
    }

}
