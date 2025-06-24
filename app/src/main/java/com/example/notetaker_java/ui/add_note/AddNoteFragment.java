package com.example.notetaker_java.ui.add_note;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notetaker_java.DB.DBHelper;
import com.example.notetaker_java.MainActivity;
import com.example.notetaker_java.databinding.FragmentAddNoteBinding;
import com.example.notetaker_java.R;

import java.util.ArrayList;
import java.util.List;

public class AddNoteFragment extends Fragment {

	private DBHelper myDB; // Экземпляр нашего помощника базы данных
	private EditText editTextNoteTitle, editTextNoteContent, editTextNoteId;
	private ImageButton buttonAddNote, buttonViewNotes, buttonUpdateNote, buttonDeleteNote;
	private TextView textViewNotesResult;

	private FragmentAddNoteBinding binding;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		AddNoteViewModel addNoteViewModel = new ViewModelProvider(this).get(AddNoteViewModel.class);

//		binding = FragmentAddNoteBinding.inflate(inflater, container, false);
//		View root = binding.getRoot();

//        final TextView textView = binding.textAddNote;
//        addNoteViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
		View view = inflater.inflate(R.layout.fragment_add_note, container, false);

		myDB = new DBHelper(getContext());

		editTextNoteTitle = view.findViewById(R.id.editTextNoteTitle);
		editTextNoteContent = view.findViewById(R.id.editTextNoteContent);
		editTextNoteId = view.findViewById(R.id.editTextNoteId);

		buttonAddNote = view.findViewById(R.id.buttonAddNote);
//		buttonViewNotes = view.findViewById(R.id.buttonViewNotes);
		buttonUpdateNote = view.findViewById(R.id.buttonUpdateNote);
		buttonDeleteNote = view.findViewById(R.id.buttonDeleteNote);

//		viewAllNotesContent();

		addNote();
//		viewAllNotes();
		updateNote();
		deleteNote();

		return view;
	}

	public void addNote() {
		buttonAddNote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String title   = editTextNoteTitle.getText().toString();
				String content = editTextNoteContent.getText().toString();

				if (title.isEmpty()) {
					Toast.makeText(getContext(), "Заголовок заметки не может быть пустым!", Toast.LENGTH_SHORT).show();
					return;
				}

				boolean isInserted = myDB.InsertNote(title, content);

				if (isInserted) {
					Toast.makeText(getContext(), "Заметка успешно добавлена", Toast.LENGTH_SHORT).show();
					clearFields();
//					viewAllNotesContent();
				} else {
					Toast.makeText(getContext(), "Ошибка при добавлении заметки", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void viewAllNotes() {
		buttonViewNotes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				viewAllNotesContent(); // Вызываем внутренний метод для получения и отображения данных
			}
		});
	}

//	private void viewAllNotesContent() {
//		Cursor res = myDB.getAllNotes();
//		if (res.getCount() == 0) {
//			textViewNotesResult.setText("Список заметок пуст.");
//			Toast.makeText(getContext(), "Заметок не найдено", Toast.LENGTH_SHORT).show();
//			return;
//		}
//
//		StringBuilder       buffer = new StringBuilder();
//		List<DBHelper.Note> notes  = new ArrayList<>();
//
//		while (res.moveToNext()) {
//			int    id        = res.getInt(res.getColumnIndexOrThrow(myDB.COL_ID));
//			String title     = res.getString(res.getColumnIndexOrThrow(myDB.COL_TITLE));
//			String content   = res.getString(res.getColumnIndexOrThrow(myDB.COL_CONTENT));
//			String timestamp = res.getString(res.getColumnIndexOrThrow(myDB.COL_TIMESTAMP));
//			notes.add(new DBHelper.Note(id, title, content, timestamp));
//		}
//		res.close();
//
//		for (DBHelper.Note note : notes) {
//			buffer.append(note.toString()).append("\n\n");
//		}
//		textViewNotesResult.setText(buffer.toString());
//	}

	public void updateNote() {
		buttonUpdateNote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String id      = editTextNoteId.getText().toString();
				String title   = editTextNoteTitle.getText().toString();
				String content = editTextNoteContent.getText().toString();
//				String timestamp = editTextNoteTitle.getText().toString();

				if (id.isEmpty() || title.isEmpty()) {
					Toast.makeText(getContext(), "Для обновления введите ID и заголовок заметки!", Toast.LENGTH_SHORT).show();
					return;
				}

				boolean isUpdate = myDB.UpdateNote(id, content);

				if (isUpdate) {
					Toast.makeText(getContext(), "Заметка успешно обновлена", Toast.LENGTH_SHORT).show();
					clearFields();
				} else {
					Toast.makeText(getContext(), "Ошибка обновления заметки или заметка с таким ID не найдена", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void deleteNote() {
		buttonDeleteNote.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String id = editTextNoteId.getText().toString().trim();

				if (id.isEmpty()) {
					Toast.makeText(getContext(), "Пожалуйста, введите ID заметки для удаления!", Toast.LENGTH_SHORT).show();
					return;
				}

				// Опционально: показать диалог подтверждения перед удалением
				new AlertDialog.Builder(getContext()).setTitle("Подтверждение удаления").setMessage("Вы уверены, что хотите удалить заметку с ID: " + id + "?").setPositiveButton("Да", (dialog, which)->{
					int deletedRows = myDB.DeleteNote(id);
					if (deletedRows > 0) {
						Toast.makeText(getContext(), "Заметка успешно удалена", Toast.LENGTH_SHORT).show();
						clearFields();
//						viewAllNotesContent(); // Обновляем список заметок
					} else {
						Toast.makeText(getContext(), "Ошибка удаления заметки или заметка с таким ID не найдена", Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton("Нет", null).show();
			}
		});
	}

	private void clearFields() {
		editTextNoteTitle.setText("");
		editTextNoteContent.setText("");
		editTextNoteId.setText("");
//		editTextId.setText("");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}