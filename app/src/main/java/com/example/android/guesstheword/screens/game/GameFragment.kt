/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        Log.i("GameFragment", "Pozvan je ViewModelProviders")
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        // bindovanje layouta i viewmodela
        binding.gameViewModel = viewModel

        //za bindovanje layout-a i viewmodela dirketno, tj za bindovanje sa LiveData iz VMa, neophodno mi je ovo
        binding.setLifecycleOwner(this)

        // S obzirom da je u layout file-u postavljen listener pomocu bindovanja sa viewModelom
        // ove metode mi vise ne trebaju ovde, jer sam Fragment nije vise posrednik u komunikaciji
        // view-a i viewModela

//        binding.correctButton.setOnClickListener {
//            viewModel.onCorrect()
//            // zbog bindovanja vise mi ne treba
//            // updateScoreText()
//            // updateWordText()
//        }
//
//        binding.skipButton.setOnClickListener {
//            viewModel.onSkip()
//            // zbog bindovanja vise mi ne treba
//            // updateScoreText()
//            // updateWordText()x
//        }


        // posto sam postavio data binding layouta i view modela, dodavanjem <data> viewModel
        // u sam layout, a zatim za wordTextView postavio android:text = vm.score
        // ovo moze da se izbrise

//        // bindovanje sa property-jem view modela
//        viewModel.score.observe(this, Observer { newScore ->
//            binding.scoreText.text = newScore.toString()
//        })

        // posto sam postavio data binding layouta i view modela, dodavanjem <data> viewModel
        // u sam layout, a zatim za wordTextView postavio android:text = vm.word
        // ovo moze da se izbrise
//        viewModel.word.observe(this, Observer { newWord ->
//            binding.wordText.text = newWord
//        })

        viewModel.eventGameFinish.observe(this, Observer { finished ->
            if (finished) {
                gameFinished()
                // za zaustavljanje eventa, tj da bi se odigrao samo jednom kosristim onGameFinishComplete
                viewModel.onGameFinishComplete()
                buzz(GameViewModel.BuzzType.GAME_OVER.pattern)
            }
        })

        // posto sam postavio data binding layouta i view modela, dodavanjem <data> viewModel
        // u sam layout, a zatim za wordTextView postavio android:text = vm.currentTimeString
        // ovo moze da se izbrise
//        viewModel.currentTime.observe(this, Observer { newTime ->
//            // da formatira long kako treba u vreme koristim DateUtils
//            binding.timerText.text = DateUtils.formatElapsedTime(newTime)
//        })

        return binding.root

    }


    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }


    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0) // Swift nilCoalising ??
        findNavController(this).navigate(action)
    }



}
