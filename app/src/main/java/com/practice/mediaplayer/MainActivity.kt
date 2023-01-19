package com.practice.mediaplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // TextViews
    private lateinit var timeText : TextView
    private lateinit var  titleText : TextView

    // Buttons
    private lateinit var btnForward: Button
    private lateinit var btnBackward: Button
    private lateinit var btnPlay: Button
    private lateinit var btnPause: Button

    // Seekbar
    private lateinit var seekBar: SeekBar

    // Media Player
    private lateinit var mediaPlayer: MediaPlayer

    // Handlers
    private lateinit var handler : Handler

    // Variables
    private var startTime: Double = 0.0
    private var finalTime: Double = 0.0
    private var forwardTime: Int = 10000
    private var backwardTime: Int = 10000
    private var oneTimeOnly: Int = 0 // To show the duration of the song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Buttons
        btnPlay = findViewById(R.id.play_btn)
        btnPause = findViewById(R.id.pause_btn)
        btnForward = findViewById(R.id.forward_btn)
        btnBackward = findViewById(R.id.back_btn)

        // TextViews
        titleText = findViewById(R.id.song_title)
        timeText = findViewById(R.id.time_left_text)

        // Seekbar
        seekBar = findViewById(R.id.seekBar)

        // Media player - Android allow developers to integrate media by the media player
        // Local media files can be stored in res -> new -> Android Resource Directory -> raw folder
        // Credit: Music by Coma-Media from Pixabay
        mediaPlayer = MediaPlayer.create(this,
                R.raw.bounce
            )

        // Set the audio name as song title
        // For more songs in the future, this attribute should be turned into a method
        titleText.setText(
            resources.getIdentifier(
            "bounce",
            "raw",
                packageName
        ))

        // Seekbar
        seekBar.isClickable = false

        // Buttons
        btnPlay.setOnClickListener{ playMusic() }
        btnPause.setOnClickListener{ pauseMusic() }
        btnForward.setOnClickListener{ forwardMusic() }
        btnBackward.setOnClickListener { backwardMusic() }

        seekBar.progress = startTime.toInt()
    }

    // Play Button
    private fun playMusic () {
        mediaPlayer.start()
        finalTime = mediaPlayer.duration.toDouble()
        startTime = mediaPlayer.currentPosition.toDouble()

        if(oneTimeOnly == 0) {
            seekBar.max = finalTime.toInt()
            oneTimeOnly = 1;
        }

        // Check if it is one time only
        timeText?.text = String.format(
            "%d min, %d, sec",
            TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong()) -
                    TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong())) // Need to minus the first time unit in seconds before displaying

        )
        var updateSongTime: Runnable = object: Runnable {
            override fun run() {
                startTime = mediaPlayer.currentPosition.toDouble()
                timeText.text = String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()))
                )
                seekBar.progress = startTime.toInt()
                handler.postDelayed(this, 1000)
            }
        }
        
    }
    


    // Pause Button
    private fun pauseMusic() {
        mediaPlayer.pause();
    }

    // Forward Button
    private fun forwardMusic() {
        var temp : Int = startTime.toInt()

        if((temp + forwardTime) <= finalTime){
            startTime += forwardTime
            mediaPlayer.seekTo(startTime.toInt())
        } else {
            Toast.makeText(this, "Unable to forward", Toast.LENGTH_SHORT).show()
        }
    }

    // Backward Button
    private fun backwardMusic() {
        var temp : Int = startTime.toInt()

        if((temp - backwardTime) > 0) {
            startTime -= backwardTime
            mediaPlayer.seekTo(startTime.toInt())
        }else {
            Toast.makeText(this, "Unable to go back", Toast.LENGTH_SHORT).show()
        }
    }

}