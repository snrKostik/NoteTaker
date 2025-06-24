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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetaker_java.DB.DBHelper;
import com.example.notetaker_java.R;
import com.example.notetaker_java.databinding.FragmentSpacedRepetitionBinding;
import com.example.notetaker_java.ui.NoteAdapter;

import java.util.ArrayList;
import java.util.List;

public class SpacedRepetitionFragment extends Fragment {
	private RecyclerView        recyclerViewNotes;
	private NoteAdapter         noteAdapter;
	private List<DBHelper.Note> noteList;

	private DBHelper                        myDB;
	private TextView                        textViewNotesResult;
	private FragmentSpacedRepetitionBinding binding;

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		SpacedRepetitionViewModel spacedRepetitionViewModel = new ViewModelProvider(this).get(SpacedRepetitionViewModel.class);

		View view = inflater.inflate(R.layout.fragment_spaced_repetition, container, false);

		myDB = new DBHelper(getContext());

		recyclerViewNotes = view.findViewById(R.id.recyclerViewNotes);
		recyclerViewNotes.setLayoutManager(new LinearLayoutManager(getContext()));

		noteList = new ArrayList<>();
		noteAdapter = new NoteAdapter(noteList);
		recyclerViewNotes.setAdapter(noteAdapter);

		viewAllNotesContent();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	private void viewAllNotesContent() {
		if (myDB == null) {
			Toast.makeText(getContext(), "Ошибка: База данных не инициализирована.", Toast.LENGTH_SHORT).show();
			return;
		}

		Cursor res = myDB.getAllNotes();
		if (res.getCount() == 0) {
			noteList.clear();
			noteAdapter.updateNotes(noteList);
			Toast.makeText(getContext(), "Заметок не найдено", Toast.LENGTH_SHORT).show();
			return;
		}
		List<DBHelper.Note> notesFromDb = new ArrayList<>();

		while (res.moveToNext()) {
			int    id        = res.getInt(res.getColumnIndexOrThrow(myDB.COL_ID));
			String title     = res.getString(res.getColumnIndexOrThrow(myDB.COL_TITLE));
			String content   = res.getString(res.getColumnIndexOrThrow(myDB.COL_CONTENT));
			String timestamp = res.getString(res.getColumnIndexOrThrow(myDB.COL_TIMESTAMP));

			notesFromDb.add(new DBHelper.Note(id, title, content, timestamp));
		}
		// Жопа

		res.close();

		noteList.clear();
		noteList.addAll(notesFromDb);
		noteAdapter.updateNotes(noteList);
	}
}