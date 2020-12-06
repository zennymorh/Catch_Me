package com.zennymorh.catchme.ui.game

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import com.zennymorh.catchme.ui.about.AboutActivity
import com.zennymorh.catchme.R
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*

class GameActivity : AppCompatActivity() {

    private var imageArray = ArrayList<ImageView>()

    private val viewModel: GameViewModel by lazy {
        ViewModelProviders.of(this).get(GameViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        imageArray = arrayListOf(
            imageView1, imageView2, imageView3, imageView4, imageView5, imageView6,
            imageView6, imageView7, imageView8, imageView9)

        viewModel.score.observe(this, Observer { newScore ->
            scoreTextView.text = "Score: $newScore"
        })

        viewModel.imageIndexToDisplay.observe(this, Observer { newIndex ->
            showImageAtIndex(newIndex)
        })

        viewModel.hideAllImages.observe(this, Observer {
            hideAllImages()
        })

        viewModel.replay.observe(this, Observer {
            replay()
        })

        viewModel.timeText.observe(this, Observer { newText ->
            timeTextView.text = newText
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGame()
    }

    private fun showImageAtIndex(index: Int) {
        imageArray[index].visibility = View.VISIBLE
    }

    private fun hideAllImages() {
        for (image in imageArray){
            image.visibility = View.INVISIBLE
        }
    }

    private fun replay() {
        val alert = AlertDialog.Builder(this@GameActivity)
        alert.setTitle("Game Over")
        alert.setMessage("Would you like to play again?")
        alert.setPositiveButton("Yes"){ dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            viewModel.pauseGame().also {
                viewModel.startGame()
            }
        }
        alert.setNegativeButton("No"){ _: DialogInterface, _: Int ->
            Toast.makeText(applicationContext, "Alright. See you later!", Toast.LENGTH_SHORT).show()
        }
        alert.show()
    }

    fun onImageViewClicked(view: View) {
        viewModel.increaseScore()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.about -> {
            val intent = Intent(this, AboutActivity::class.java )
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseGame()
    }
}
