package com.example.notetaker_java.ui.add_note;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notetaker_java.databinding.FragmentAddNoteBinding;

public class AddNoteFragment extends Fragment {

    private FragmentAddNoteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddNoteViewModel addNoteViewModel =
                new ViewModelProvider(this).get(AddNoteViewModel.class);

        binding = FragmentAddNoteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAddNote;
        addNoteViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}