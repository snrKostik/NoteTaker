package com.example.notetaker_java.ui.spaced_repetition;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notetaker_java.DB.DBHelper;
import com.example.notetaker_java.R;
import com.example.notetaker_java.databinding.FragmentSpacedRepetitionBinding;

import java.util.ArrayList;
import java.util.List;

public class SpacedRepetitionFragment extends Fragment {

    private DBHelper myDB;
    private TextView textViewNotesResult;
    private FragmentSpacedRepetitionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SpacedRepetitionViewModel spacedRepetitionViewModel =
                new ViewModelProvider(this).get(SpacedRepetitionViewModel.class);

        View view = inflater.inflate(R.layout.fragment_spaced_repetition, container, false);

        myDB = new DBHelper(getContext());

//        binding = FragmentSpacedRepetitionBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
        textViewNotesResult = view.findViewById(R.id.textViewNotesResult);

//        final TextView textView = binding.textSpacedRepetition;
        viewAllNotesContent();
//        spacedRepetitionViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void viewAllNotesContent() {
        Cursor res = myDB.getAllNotes();
        if (res.getCount() == 0) {
            textViewNotesResult.setText("Список заметок пуст.");
            Toast.makeText(getContext(), "Заметок не найдено", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder       buffer = new StringBuilder();
        List<DBHelper.Note> notes  = new ArrayList<>();

        while (res.moveToNext()) {
            int    id        = res.getInt(res.getColumnIndexOrThrow(myDB.COL_ID));
            String title     = res.getString(res.getColumnIndexOrThrow(myDB.COL_TITLE));
            String content   = res.getString(res.getColumnIndexOrThrow(myDB.COL_CONTENT));
            String timestamp = res.getString(res.getColumnIndexOrThrow(myDB.COL_TIMESTAMP));
            notes.add(new DBHelper.Note(id, title, content, timestamp));
        }
        res.close();

        for (DBHelper.Note note : notes) {
            buffer.append(note.toString()).append("\n\n");
        }
        textViewNotesResult.setText(buffer.toString());
    }
}