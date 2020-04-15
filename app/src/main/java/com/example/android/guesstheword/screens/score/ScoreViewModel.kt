package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore: Int): ViewModel() {

    init {
        Log.i("ScoreViewModel", "Finalni score je ${finalScore}")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("ScoreViewModel", "ScoreViewModel je unisten.")
    }
}