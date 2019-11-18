package com.zennymorh.catchme

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var score: Int = 0

    var imageArray = ArrayList<ImageView>()
    var handler: Handler = Handler()
    var runnable: Runnable = Runnable {  }
    lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        score = 0

        imageArray = arrayListOf(imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView6, imageView7, imageView8, imageView9)

        start()
    }

    fun start() {
        timer = object : CountDownTimer(30000, 1000) {
            override fun onFinish() {
                timeText.text = "Time Up"
                handler.removeCallbacks(runnable)
                for (image in imageArray) {
                    image.visibility = View.INVISIBLE
                }

                replay()
            }

            override fun onTick(millisUntilFinished: Long) {
                timeText.text = "Time: " + millisUntilFinished / 1000
            }

        }.start()

        hideImages()
    }

    fun replay(){
        val alert = AlertDialog.Builder(this@MainActivity)
        alert.setTitle("Game Over")
        alert.setMessage("Would you like to play again?")
        alert.setPositiveButton("Yes"){dialog: DialogInterface, which: Int -> recreate() }
        alert.setNegativeButton("No"){dialog: DialogInterface, which: Int -> Toast.makeText(applicationContext, "Alright. See you later!", Toast.LENGTH_SHORT).show() }
        alert.show()
    }

    fun hideImages(){

        runnable = object : Runnable{
            override fun run() {
                for (image in imageArray){
                    image.visibility = View.INVISIBLE
                }

                var random = Random()
                var index = random.nextInt(8 - 0)
                imageArray[index].visibility = View.VISIBLE

                handler.postDelayed(runnable,500)
            }
        }
        handler.post(runnable)
    }

    fun increaseScore(view: View){
        score++
        scoreText.text = "Score: " + score
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    // actions on click menu item
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.about -> {
            val intent = Intent(this, AboutActivity::class.java )
            startActivity(intent)
            true
        }
        else -> {
            //This is what the tutorial said I should do. Me sef, i no understand.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        timer.start()
        handler.post(runnable)
        score = 0
        scoreText.text = "Score: " + score
    }

}
