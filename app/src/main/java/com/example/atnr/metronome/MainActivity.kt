package com.example.atnr.metronome

import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),OnSeekBarChangeListener {

    //define seek bar and textView for BPM number
    private var bpmView: TextView? = null
    private var seekBarView: SeekBar? = null

    //Initial BPM
    private var bpmVal: Long = 120

    //handler
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //define sound pool
        val soundPool = SoundPool.Builder().setMaxStreams(2).build()
        val soundHigh = soundPool.load(this, R.raw.high_sample, 1)
        val soundLow = soundPool.load(this, R.raw.low_sample,1)

        var beatCount = 0

        bpmView = this.textBPM
        seekBarView = this.seekBarBPM
        seekBarView!!.setOnSeekBarChangeListener(this)

        val playButton = this.buttonPlayStop
        val stopButton = this.buttonStop

        val runnable = object : Runnable {
            override fun run() {

                //Use high tone on the first beat
                if(beatCount==0){
                    soundPool.play(soundHigh, 1.0f, 1.0f, 0, 0, 1.0f)
                }else{
                    soundPool.play(soundLow, 1.0f, 1.0f, 0, 0, 1.0f)
                }

                beatCount++

                //Reset count after forth beat
                if(beatCount>3){
                    beatCount=0
                }

                handler.postDelayed(this, bpmVal)
            }
        }

        //playButton
        playButton.setOnClickListener {
            bpmVal = calcMills()
            handler.post(runnable)
        }

        //stopButton
        stopButton.setOnClickListener {
            handler.removeCallbacks(runnable)
            //soundPool.release();
        }



    }

    private fun calcMills():Long {

        var Str = bpmView!!.text.toString()
        var count: Int = Integer.parseInt(Str)
        var mills = 1000.0 * 60.0 / count.toLong()
        return mills.toLong()

    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        // called when progress is changed
        bpmVal = calcMills()
        bpmView!!.text = progress.toString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        // called when tracking the seekbar is started
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        // called when tracking the seekbar is stopped
    }





}