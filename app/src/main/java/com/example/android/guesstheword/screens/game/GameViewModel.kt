package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel: ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        // kada je igra gotova
        private const val DONE = 0L
        // broj milisekundi u jednoj sekundi
        private  const val ONE_SECOND = 1000L
        // ukupno vreme igre
        private const val COUNTDOWN_TIME = 10000L
    }

    private val timer: CountDownTimer



    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // Za zavrsetak igre
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
            get() = _eventGameFinish

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) {time ->
        DateUtils.formatElapsedTime(time)
    }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Log.i("GameViewModel", "GameViewModel je kreiran!")

        _eventGameFinish.value = false

        // definisan kao singleton i blok Klase CountDownTimer
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished
            }

            override fun onFinish() {
                _eventGameFinish.value = true
            }
        }

        timer.start()

        resetList()

        _score.value = 0
        _word.value = wordList[0]

        // mora posle zadavanja vrednosti wrod property-ju, jer je se na prvi klik wrod nije menjao
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()

        Log.i("GameViewModel", "GameViewModel je unisten!")
        timer.cancel()
    }

    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        // Dodajem odbrojavanje pa ce prvobitna logika biti zakomentarisana
//        if (wordList.isEmpty()) {
//           _eventGameFinish.value = true
//        } else {
//            _word.value = wordList.removeAt(0)
//        }

        // Sa odbrojavnjem
        if (wordList.isEmpty()) {
            resetList()
        }

        _word.value = wordList.removeAt(0)

    }

    fun onSkip() {
        _score.value = (score.value)?.minus(1) // isto sto i ovo samo sa proverom -> score.value!! - 1
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1) // isto sto i ovo samo sa proverom -> score.value!! - 1
        nextWord()
    }

    // za zaustavljanje eventa, tj da bi se odigrao samo jednom kosristim onGameFinishComplete
    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }



}