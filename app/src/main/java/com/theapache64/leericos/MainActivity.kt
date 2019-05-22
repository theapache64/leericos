package com.theapache64.leericos

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private var leericos: Leericos? = null
    private var player: MediaPlayer? = null
    private var timer: Timer? = null
    private var tvLyric: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.tvLyric = findViewById(R.id.tv_lyric)

        this.leericos = Leericos.fromAssets(this, "kesha_tik_tok.lrc")
    }

    private fun startTimer() {
        this.timer = Timer().apply {
            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val lrc = leericos!!.get(player!!.currentPosition.toLong())
                    runOnUiThread {
                        tvLyric!!.text = lrc.text
                    }
                }
            }, 0, 100)
        }
    }

    override fun onPause() {
        super.onPause()
        timer?.cancel()
        player?.apply {
            stop()
            release()
        }
    }

    override fun onResume() {
        super.onResume()

        // Play music
        this.player = MediaPlayer.create(
            this,
            Uri.parse("https://raw.githubusercontent.com/theapache64/mp3lrc/master/ke%24ha%20-%20tik%20tok.mp3")
        )
        this.player!!.start()
        this.player!!.setOnCompletionListener {
            finish()
        }

        startTimer()
    }

}
