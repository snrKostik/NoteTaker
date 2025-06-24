package com.example.notetaker_java.ui.spaced_repetition;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SpacedRepetitionViewModel extends ViewModel {

	private final MutableLiveData<String> mText;

	public SpacedRepetitionViewModel() {
		mText = new MutableLiveData<>();
		mText.setValue("This is spacedRepetition fragment");
	}

	public LiveData<String> getText() {
		return mText;
	}
}