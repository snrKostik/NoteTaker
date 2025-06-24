package com.example.notetaker_java.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.example.notetaker_java.R;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetaker_java.DB.DBHelper;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

	private List<DBHelper.Note> noteList;

	public NoteAdapter(List<DBHelper.Note> noteList) {
		this.noteList = noteList;
	}

	public static class NoteViewHolder extends RecyclerView.ViewHolder {
		public TextView textViewTitle;
		public TextView textViewContent;
		public TextView textViewTimestamp;

		public NoteViewHolder(View itemView) {
			super(itemView);
			textViewTitle = itemView.findViewById(R.id.textViewNoteTitle);
			textViewContent = itemView.findViewById(R.id.textViewNoteContent);
			textViewTimestamp = itemView.findViewById(R.id.textViewNoteTimestamp);
		}
	}

	@NonNull
	@Override
	public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
		return new NoteViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
		DBHelper.Note currentNote = noteList.get(position);

		holder.textViewTitle.setText(currentNote.getTitle());
		holder.textViewContent.setText(currentNote.getContent());
		holder.textViewTimestamp.setText(currentNote.getTimestamp());
		holder.textViewTitle.setText("ID: " + currentNote.getId() + " - " + currentNote.getTitle());
	}

	@Override
	public int getItemCount() {
		return noteList.size();
	}

	public void updateNotes(List<DBHelper.Note> newNoteList) {
		this.noteList = newNoteList;
		notifyDataSetChanged(); // Уведомляем адаптер об изменении данных, чтобы он перерисовал список
	}
}
