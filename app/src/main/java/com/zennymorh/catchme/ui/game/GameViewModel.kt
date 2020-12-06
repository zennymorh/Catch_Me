package com.zennymorh.catchme.ui.game

import android.os.CountDownTimer
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class GameViewModel: ViewModel() {
    private var handler: Handler = Handler()
    private lateinit var imageRandomizationRunnable: Runnable
    private lateinit var timer: CountDownTimer

    private val _score  = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _hideAllImages  = MutableLiveData<Unit>()
    val hideAllImages: LiveData<Unit>
        get() = _hideAllImages

    private val _imageIndexToDisplay  = MutableLiveData<Int>()
    val imageIndexToDisplay: LiveData<Int>
        get() = _imageIndexToDisplay

    private val _replay  = MutableLiveData<Unit>()
    val replay: LiveData<Unit>
        get() = _replay

    private val _timeText  = MutableLiveData<String>()
    val timeText: LiveData<String>
        get() = _timeText

    init {
        initCountDown()
        initImageRandomization()
    }

    private fun initCountDown() {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onFinish() {
                _timeText.value = "Time Up"
                _hideAllImages.value = null
                _replay.value = null

                handler.removeCallbacks(imageRandomizationRunnable)
            }

            override fun onTick(millisUntilFinished: Long) {
                _timeText.value = "Time: " + millisUntilFinished / 1000
            }
        }
    }

    private fun initImageRandomization(){
        imageRandomizationRunnable = Runnable {
            _hideAllImages.value = null

            val random = Random()
            val index = random.nextInt(8)
            _imageIndexToDisplay.value = index

            handler.postDelayed(imageRandomizationRunnable,500)
        }
    }

    fun startGame() {
        timer.start()
        handler.post(imageRandomizationRunnable)
        _score.value = 0
    }

    fun pauseGame() {
        timer.cancel()
        handler.removeCallbacks(imageRandomizationRunnable)
    }

    fun increaseScore() {
        _score.value = _score.value?.plus(1)
    }
}